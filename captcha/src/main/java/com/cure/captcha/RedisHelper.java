package com.cure.captcha;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @title: RedisHelper
 * @description: Redis 操作工具
 * @author: dengmiao
 * @create: 2019-05-23 18:03
 **/
@Slf4j
public class RedisHelper {

    protected RedisTemplate<String, String> redisTemplate;
    protected ValueOperations<String, String> valueHelper;
    protected HashOperations<String, String, String> hashHelper;
    protected ListOperations<String, String> listHelper;
    protected SetOperations<String, String> setHelper;
    protected ZSetOperations<String, String> zSetHelper;
    protected ObjectMapper objectMapper;

    public RedisHelper(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.valueHelper = redisTemplate.opsForValue();
        this.hashHelper = redisTemplate.opsForHash();
        this.listHelper = redisTemplate.opsForList();
        this.setHelper = redisTemplate.opsForSet();
        this.zSetHelper = redisTemplate.opsForZSet();
    }

    /**
     * 设置当前线程操作 redis database，同一个线程内操作多次redis，不同database，
     * 需要调用 {@link RedisHelper#clearCurrentDatabase()} 清除当前线程redis database，从而使用默认的db.
     *
     * @param database redis database
     */
    public void setCurrentDatabase(int database) {
        log.warn("Use default RedisHelper, you'd better use a DynamicRedisHelper instead.");
    }

    /**
     * 清除当前线程 redis database.
     */
    public void clearCurrentDatabase() {
        log.warn("Use default RedisHelper, you'd better use a DynamicRedisHelper instead.");
    }

    //
    // key
    // ------------------------------------------------------------------------------
    /**
     * 判断key是否存在
     *
     * <p>
     * <i>exists key</i>
     *
     * @param key key
     */
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 判断key存储的值类型
     *
     * <p>
     * <i>type key</i>
     *
     * @param key key
     * @return DataType[string、list、set、zset、hash]
     */
    public DataType typeKey(String key){
        return redisTemplate.type(key);
    }

    /**
     * 重命名key. 如果newKey已经存在，则newKey的原值被覆盖
     *
     * <p>
     * <i>rename oldKey newKey</i>
     *
     * @param oldKey oldKeys
     * @param newKey newKey
     */
    public void renameKey(String oldKey, String newKey){
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名.
     *
     * <p>
     * <i>renamenx oldKey newKey</i>
     *
     * @param oldKey oldKey
     * @param newKey newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNx(String oldKey, String newKey){
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key</i>
     *
     * @param key key
     */
    public void delKey(String key){
        redisTemplate.delete(key);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key1 key2 ...</i>
     *
     * @param keys 可传入多个key
     */
    public void delKey(String ... keys){
        Set<String> ks = Stream.of(keys).collect(Collectors.toSet());
        redisTemplate.delete(ks);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key1 key2 ...</i>
     *
     * @param keys key集合
     */
    public void delKey(Collection<String> keys){
        redisTemplate.delete(keys);
    }

    /**
     * 设置key的生命周期，单位秒
     *
     * <p>
     * <i>expire key seconds</i><br>
     * <i>pexpire key milliseconds</i>
     *
     * @param key key
     * @param time 时间数
     * @param timeUnit TimeUnit 时间单位
     */
    public void setExpire(String key, long time, TimeUnit timeUnit){
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 设置key在指定的日期过期
     *
     * <p>
     * <i>expireat key timestamp</i>
     *
     * @param key key
     * @param date 指定日期
     */
    public void setExpireAt(String key, Date date){
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * <p>
     * <i>ttl key</i>
     *
     * @param key key
     * @param timeUnit TimeUnit 时间单位
     * @return 指定时间单位的时间数
     */
    public long getKeyExpire(String key, TimeUnit timeUnit){
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * <p>
     * <i>persist key</i>
     *
     * @param key key
     */
    public void persistKey(String key){
        redisTemplate.persist(key);
    }

    public int delKeysWithPrefix(String keyPrefix) {
        Set<String> keys = this.keys(keyPrefix + '*');
        if(keys == null || keys.isEmpty()) {
            return 0;
        }
        this.delKey(keys);
        return keys.size();
    }

    public Set<String> keys(String pattern){
        return this.redisTemplate.keys(pattern);
    }

    //
    // string
    // ------------------------------------------------------------------------------
    public void strSet(String key, String value, long expire, TimeUnit timeUnit) {
        valueHelper.set(key, value);
        this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

    public void strSet(String key, String value) {
        valueHelper.set(key, value);
    }

    public String strGet(String key) {
        return valueHelper.get(key);
    }

    public String strGet(String key, long expire, TimeUnit timeUnit) {
        String value = valueHelper.get(key);
        this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        return value;
    }

    public <T> T strGet(String key, Class<T> clazz) {
        String value = valueHelper.get(key);
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T strGet(String key, Class<T> clazz, long expire, TimeUnit timeUnit) {
        String value = valueHelper.get(key);
        this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        return value == null ? null : fromJson(value, clazz);
    }

    public String strGet(String key, Long start, Long end) {
        return valueHelper.get(key, start, end);
    }

    public Long strIncrement(String key, Long delta) {
        return valueHelper.increment(key, delta);
    }

    //
    // list
    // ------------------------------------------------------------------------------
    public Long lstLeftPush(String key, String value) {
        return listHelper.leftPush(key, value);
    }

    public Long lstLeftPushAll(String key, Collection<String> values) {
        return listHelper.leftPushAll(key, values);
    }

    public Long lstRightPush(String key, String value) {
        return listHelper.rightPush(key, value);
    }

    public Long lstRightPushAll(String key, Collection<String> values) {
        return listHelper.rightPushAll(key, values);
    }

    public List<String> lstRange(String key, long start, long end) {
        return listHelper.range(key, start, end);
    }

    public List<String> lstAll(String key) {
        return this.lstRange(key, 0, this.lstLen(key));
    }

    public String lstLeftPop(String key) {
        return listHelper.leftPop(key);
    }

    public String lstRightPop(String key) {
        return listHelper.rightPop(key);
    }

    public Long lstLen(String key) {
        return listHelper.size(key);
    }


    public void lstSet(String key, long index, String value) {
        listHelper.set(key, index, value);
    }

    public Long lstRemove(String key, long index, String value) {
        return listHelper.remove(key, index, value);
    }

    public Object lstIndex(String key, long index) {
        return listHelper.index(key, index);
    }

    public void lstTrim(String key, long start, long end) {
        listHelper.trim(key, start, end);
    }

    //
    // set
    // ------------------------------------------------------------------------------
    public Long setAdd(String key, String[] values) {
        return setHelper.add(key, values);
    }

    public Long setIrt(String key, String... values) {
        return setHelper.add(key, values);
    }

    public Set<String> setMembers(String key) {
        return setHelper.members(key);
    }

    public Boolean setIsMember(String key, String o) {
        return setHelper.isMember(key, o);
    }

    public Long setSize(String key) {
        return setHelper.size(key);
    }

    public Set<String> setIntersect(String key, String otherKey) {
        return setHelper.intersect(key, otherKey);
    }

    public Set<String> setUnion(String key, String otherKey) {
        return setHelper.union(key, otherKey);
    }

    public Set<String> setUnion(String key, Collection<String> otherKeys) {
        return setHelper.union(key, otherKeys);
    }

    public Set<String> setDifference(String key, String otherKey) {
        return setHelper.difference(key, otherKey);
    }

    public Set<String> setDifference(String key, Collection<String> otherKeys) {
        return setHelper.difference(key, otherKeys);
    }

    public Long setDel(String key, String value) {
        return setHelper.remove(key, value);
    }

    public Long setRemove(String key, Object[] value) {
        return setHelper.remove(key, value);
    }

    //
    // zSet
    // ------------------------------------------------------------------------------
    public Boolean zSetAdd(String key, String value, double score) {
        return zSetHelper.add(key, value, score);
    }

    public Double zSetScore(String key, String value) {
        return zSetHelper.score(key, value);
    }

    public Double zSetIncrementScore(String key, String value, double delta) {
        return zSetHelper.incrementScore(key, value, delta);
    }

    public Long zSetRank(String key, String value) {
        return zSetHelper.rank(key, value);
    }

    public Long zSetReverseRank(String key, String value) {
        return zSetHelper.reverseRank(key, value);
    }

    public Long zSetSize(String key) {
        return zSetHelper.size(key);
    }

    public Long zSetRemove(String key, String value) {
        return zSetHelper.remove(key, value);
    }

    public Set<String> zSetRange(String key, Long start, Long end) {
        return zSetHelper.range(key, start, end);
    }

    public Set<String> zSetReverseRange(String key, Long start, Long end) {
        return zSetHelper.reverseRange(key, start, end);
    }

    public Set<String> zSetRangeByScore(String key, Double min, Double max) {
        return zSetHelper.rangeByScore(key, min, max);
    }

    public Set<String> zSetReverseRangeByScore(String key, Double min, Double max) {
        return zSetHelper.reverseRangeByScore(key, min, max);
    }

    public Set<String> zSetRangeByScore(String key, Double min, Double max, Long offset, Long count) {
        return zSetHelper.rangeByScore(key, min, max, offset, count);
    }

    public Set<String> zSetReverseRangeByScore(String key, Double min, Double max, Long offset, Long count) {
        return zSetHelper.reverseRangeByScore(key, min, max, offset, count);
    }

    public Long zSetCount(String key, Double min, Double max) {
        return zSetHelper.count(key, min, max);
    }

    //
    // hash
    // ------------------------------------------------------------------------------
    public void hshPut(String key, String hashKey, String value) {
        hashHelper.put(key, hashKey, value);
    }

    public void hshPutAll(String key, Map<String, String> map) {
        hashHelper.putAll(key, map);
    }

    public byte[] hshGetSerial(String key, String hashKey) {
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> {
            try {
                return connection.hGet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey));
            } catch (Exception e) {
                log.error("获取HASH对象序列失败", e);
            }
            return null;
        });
    }


    public Boolean hshPutSerial(String key, String hashKey, byte[] value) {
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            try {
                return connection.hSet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey),
                        value);
            } catch (Exception e) {
                log.error("插入HASH对象序列失败", e);
            }
            return Boolean.FALSE;
        });
    }

    public String hshGet(String key, String hashKey) {
        return hashHelper.get(key, hashKey);
    }

    public List<String> hshMultiGet(String key, Collection<String> hashKeys) {
        return hashHelper.multiGet(key, hashKeys);
    }

    public Map<String, String> hshGetAll(String key) {
        return hashHelper.entries(key);
    }

    public Boolean hshHasKey(String key, String hashKey) {
        return hashHelper.hasKey(key, hashKey);
    }

    public Set<String> hshKeys(String key) {
        return hashHelper.keys(key);
    }

    public List<String> hshVals(String key) {
        return hashHelper.values(key);
    }

    public List<String> hshVals(String key, Collection<String> hashKeys) {
        return hashHelper.multiGet(key, hashKeys);
    }

    public Long hshSize(String key) {
        return hashHelper.size(key);
    }

    public void hshDelete(String key, Object... hashKeys) {
        hashHelper.delete(key, hashKeys);
    }

    public void hshRemove(String key, Object[] hashKeys) {
        hashHelper.delete(key, hashKeys);
    }

    /**
     * Object转成JSON数据
     */
    public <T> String toJson(T object) {
        if(object == null) {
            return StrUtil.EMPTY;
        }
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return StrUtil.EMPTY;
        }
    }

    /**
     * JSON数据，转成Object
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        if(StrUtil.isBlank(json) || clazz == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}

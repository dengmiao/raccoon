package com.cure.limit.core;

import com.cure.limit.common.SpringContextUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.cure.limit.common.RedisLockUtil.*;

/**
 * @description: 令牌桶算法：分布式、Redis
 * @author: dengmiao
 * @create: 2019-04-25 13:03
 **/
public class RateLimiterCloud implements RateLimiter {

    /**
     * 令牌桶容量
     */
    private long size;
    /**
     * 间隔时间：纳秒
     */
    private long period;
    /**
     * 延迟生效时间：毫秒
     */
    private long initialDelay;
    /**
     * 锁过期时间：毫秒
     */
    private final int LOCK_GET_EXPIRES = 10 * 1000;
    /**
     * 实例过期时间：毫秒
     */
    private final int LOCK_PUT_EXPIRES = 10 * 1000;
    /**
     * 读锁
     */
    private String LOCK_GET;
    /**
     * 写锁
     */
    private String LOCK_PUT;
    /**
     * 令牌桶标识
     */
    private String BUCKET;
    /**
     * 记录上一次操作的时间
     */
    private String LOCK_PUT_DATA;
    /**
     * 唯一实例标识
     */
    private final String AppCode = SpringContextUtil.getApplicationName() + SpringContextUtil.getPort() + this.hashCode();
    /**
     * 获取RedisTemplate
     */
    private StringRedisTemplate template = SpringContextUtil.getBean(StringRedisTemplate.class);

    /**
     * @param QPS          每秒并发量,等于0 默认禁止访问
     * @param initialDelay 首次延迟时间：毫秒
     * @param overflow     是否严格控制请求速率和次数
     */
    private RateLimiterCloud(double QPS, long initialDelay, String bucket, boolean overflow) {
        this.size = overflow ? 1 : (QPS < 1 ? 1 : new Double(QPS).longValue());
        //毫秒转纳秒
        this.initialDelay = initialDelay * 1000 * 1000;
        this.period = QPS != 0 ? new Double(1000 * 1000 * 1000 / QPS).longValue() : Integer.MAX_VALUE;
        init(bucket);
        //等于0就不放令牌了
        if (QPS != 0) {
            putScheduled();
        }
    }

    private void init(String bucket) {
        this.BUCKET = bucket;
        this.LOCK_GET = bucket + "$GET";
        this.LOCK_PUT = bucket + "$PUT";
        this.LOCK_PUT_DATA = this.LOCK_PUT + "$DATA";
        //初始化令牌桶为0
        template.opsForValue().set(BUCKET, String.valueOf(0));
    }


    public static RateLimiter of(double QPS, long initialDelay, String bucket, boolean overflow) {
        return new RateLimiterCloud(QPS, initialDelay, bucket, overflow);
    }

    /**
     * 获取令牌,阻塞直到成功
     */
    @Override
    public boolean tryAcquire() {
        //取到锁
        tryLock(template, LOCK_GET, LOCK_GET, LOCK_GET_EXPIRES, TimeUnit.MILLISECONDS);
        try {
            Long s = Long.valueOf(template.opsForValue().get(BUCKET));
            while (s <= 0) {
                //阻塞
                s = Long.valueOf(template.opsForValue().get(BUCKET));
            }
            //拿走令牌
            template.opsForValue().decrement(BUCKET);
            return true;
        } finally {
            //释放锁
            releaseLock(template, LOCK_GET);
        }
    }

    /**
     * 获取令牌,没有令牌立即失败
     */
    @Override
    public boolean tryAcquireFailed() {
        //取到锁
        tryLock(template, LOCK_GET, LOCK_GET, LOCK_GET_EXPIRES, TimeUnit.MILLISECONDS);
        try {
            Long s = Long.valueOf(template.opsForValue().get(BUCKET));
            if (s > 0) {
                //拿走令牌
                template.opsForValue().decrement(BUCKET);
                return true;
            }
            return false;
        } finally {
            //释放锁
            releaseLock(template, LOCK_GET);
        }
    }

    /**
     * 周期性放令牌，控制访问速率
     * 算法：通过抢占机制选举leader，其它候选者对leader进行监督，发现leader懈怠即可将其踢下台。由此进入新一轮的抢占...
     */
    private void putScheduled() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //成为leader
                if (tryLockFailed(template, LOCK_PUT, AppCode) || AppCode.equals(template.opsForValue().get(LOCK_PUT))) {
                    Long s = Long.valueOf(template.opsForValue().get(BUCKET));
                    if (size > s) {
                        template.opsForValue().increment(BUCKET);
                    }
                    //更新时间
                    template.opsForValue().set(LOCK_PUT_DATA, String.valueOf(System.currentTimeMillis()));
                } else { //成为候选者
                    Long s = Long.valueOf(template.opsForValue().get(LOCK_PUT_DATA));
                    if (System.currentTimeMillis() - s > LOCK_PUT_EXPIRES) {
                        //释放锁
                        releaseLock(template, LOCK_PUT);
                    }
                }
            }
        }, initialDelay, period, TimeUnit.NANOSECONDS);
    }
}

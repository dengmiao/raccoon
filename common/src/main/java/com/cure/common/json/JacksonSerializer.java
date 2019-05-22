package com.cure.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @title: JacksonSerializer
 * @description: <p>自定义json序列化器</p>
 * 将Long类型序列化为String
 * 解决超过js的Number安全值(-2^53 ~ 2^53)传至前端出现精度问题
 * 用法: 待序列化的属性加<span>@JsonSerialize(using = CustomSerializer.class)</span>
 *
 * 纳入切面 用于判定rest API是服务于安卓还是web
 * @io.swagger.annotations.ApiOperation 标识App API;admin.**.controller 包且!@io.swagger.annotations.ApiOperation标识Web API
 * 安卓端@JsonSerialize(using = CustomSerializer.class)任序列化位Number;pc端需序列化String
 * @author: dengmiao
 * @create: 2019-05-22 17:03
 **/
@Component
@Aspect
public class JacksonSerializer extends JsonSerializer<Object> {

    /**
     * 序列化标志位
     */
    private static boolean flag = true;

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 待序列化属性本身就是String
        if(o instanceof String) {
            jsonGenerator.writeString(o != null ? o.toString() : null);
        } else {
            // 待序列化属性本身是Number
            if(flag) {
                jsonGenerator.writeString(o != null ? String.valueOf(o) : null);
            } else {
                jsonGenerator.writeNumber((Long) o);
            }
        }
    }
}
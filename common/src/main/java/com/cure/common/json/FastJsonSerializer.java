package com.cure.common.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;

/**
 * @title: FastJsonSerializer
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 16:51
 **/
public class FastJsonSerializer implements ObjectSerializer {

    private final boolean flag = true;

    @Override
    public void write(JSONSerializer serializer, Object o, Object fieldName, Type fieldType, int features) {
        // 待序列化属性本身就是String
        if(o instanceof String) {
            serializer.write(o);
        } else {
            // 待序列化属性本身是Number
            if(flag) {
                serializer.write(o != null ? String.valueOf(o) : null);
            } else {
                serializer.write(o);
            }
        }
    }
}

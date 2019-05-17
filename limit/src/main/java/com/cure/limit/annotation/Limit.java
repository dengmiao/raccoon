package com.cure.limit.annotation;

import java.lang.annotation.*;

/**
 * @title: Limit
 * @description: 限流注解
 * @author: dengmiao
 * @create: 2019-05-17 16:42
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Limit {

    /**
     * 每秒并发量
     */
    double qps() default 20;

    /**
     * 初始延迟时间
     */
    long initialDelay() default 0;

    /**
     * 开启快速失败/阻塞
     */
    boolean failFast() default true;

    /**
     * 是否严格控制请求速率和次数，TRUE即切换为漏桶算法
     */
    boolean overflow() default false;
}

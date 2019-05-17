package com.cure.limit.core;

/**
 * @description: 令牌桶算法
 * @author: dengmiao
 * @create: 2019-04-22 11:32
 **/
public interface RateLimiter {

    String message = "<pre>The specified service is not currently available.</pre>";

    /**
     *
     * @return
     */
    boolean tryAcquire();

    /**
     * 快速失败
     * @return
     */
    boolean tryAcquireFailed();
}

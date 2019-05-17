package com.cure.limit.handler;

import com.cure.limit.annotation.Limit;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @description:
 * @author: dengmiao
 * @create: 2019-04-22 11:35
 **/
public interface CurrentAspectHandler {

    /**
     * CurrentLimiter注解拦截后的反馈
     *
     * @param pjp
     * @param rateLimiter
     * @return
     * @throws Throwable
     */
    Object around(ProceedingJoinPoint pjp, Limit rateLimiter)throws Throwable;
}

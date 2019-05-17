package com.cure.limit.aspect;

import com.cure.limit.annotation.Limit;
import com.cure.limit.common.SpringContextUtil;
import com.cure.limit.core.RateLimiter;
import com.cure.limit.core.RateLimiterCloud;
import com.cure.limit.core.RateLimiterSingle;
import com.cure.limit.handler.CurrentAspectHandler;
import com.cure.limit.interceptor.properties.CurrentProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: dengmiao
 * @create: 2019-04-22 11:48
 **/
@Component
@Aspect
@ConditionalOnProperty(prefix = "current.limiting", name = "part-enabled", havingValue = "true", matchIfMissing = true)
public class CurrentAspect {

    /**
     * 一个方法一个限流器
     */
    private Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    /**
     * 自定义限流拦截处理
     */
    @Autowired(required = false)
    private CurrentAspectHandler handler;

    @Autowired
    private CurrentProperties properties;

    /**
     * 声明切点
     */
    @Pointcut("@annotation(com.cure.limit.annotation.Limit)")
    public void pointcut() {
    }

    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     * @param pjp
     * @param currentLimiter
     * @return
     * @throws Throwable
     */
    @Around("pointcut() && @annotation(currentLimiter)")
    public Object around(ProceedingJoinPoint pjp, Limit currentLimiter) throws Throwable {
        //初始化限流器
        RateLimiter rateLimiter = initCurrentLimiting(pjp,currentLimiter);
        //执行快速失败
        if (currentLimiter.failFast()){
            return tryAcquireFailed(pjp, currentLimiter, rateLimiter);
        }else { //执行阻塞策略
            rateLimiter.tryAcquire();
            return pjp.proceed();
        }
    }

    private Object tryAcquireFailed(ProceedingJoinPoint pjp, Limit currentLimiter, RateLimiter rateLimiter) throws Throwable {
        //取到令牌
        if (rateLimiter.tryAcquireFailed()) {
            return pjp.proceed();
        }else {
            //没取到令牌
            return handler == null ? RateLimiter.message : handler.around(pjp,currentLimiter);
        }
    }

    /**
     * 初始化限流器
     * 为了提高性能，不加同步锁，所以存在初始的误差。
     */
    private RateLimiter initCurrentLimiting(ProceedingJoinPoint pjp, Limit currentLimiter) {
        String key = pjp.getSignature().toLongString();
        if (!map.containsKey(key)) {
            if (properties.isCloudEnabled()){
                map.put(key, RateLimiterCloud.of(currentLimiter.qps(),currentLimiter.initialDelay(), SpringContextUtil.getApplicationName()+key,currentLimiter.overflow()));
            }else {
                map.put(key, RateLimiterSingle.of(currentLimiter.qps(), currentLimiter.initialDelay(),currentLimiter.overflow()));
            }
        }
        return map.get(key);
    }
}

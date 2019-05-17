package com.cure.limit.interceptor;

import com.cure.limit.common.SpringContextUtil;
import com.cure.limit.core.RateLimiter;
import com.cure.limit.core.RateLimiterCloud;
import com.cure.limit.core.RateLimiterSingle;
import com.cure.limit.handler.CurrentInterceptorHandler;
import com.cure.limit.interceptor.properties.CurrentProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: dengmiao
 * @create: 2019-04-22 11:40
 **/
public class DefaultCurrentInterceptor implements HandlerInterceptor {

    private RateLimiter rateLimiter;
    private boolean failFast;
    private CurrentInterceptorHandler interceptorHandler;

    public DefaultCurrentInterceptor(CurrentProperties properties, CurrentInterceptorHandler handler) {
        this.failFast = properties.isFailFast();
        this.interceptorHandler = handler;
        if (properties.isCloudEnabled()){
            rateLimiter = RateLimiterCloud.of(properties.getQps(), properties.getInitialDelay(), SpringContextUtil.getApplicationName(),properties.isOverflow());
        }else {
            rateLimiter = RateLimiterSingle.of(properties.getQps(), properties.getInitialDelay(),properties.isOverflow());
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //执行快速失败
        if (failFast) {
            return tryAcquireFailed(request, response, handler);
        }else { //执行阻塞策略
            return rateLimiter.tryAcquire();
        }
    }

    private boolean tryAcquireFailed(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //取到令牌
        if (rateLimiter.tryAcquireFailed()){
            return true;
        }else { //没取到令牌
            if (interceptorHandler == null) {
                response.getWriter().print(RateLimiter.message);
            } else {
                interceptorHandler.preHandle(request, response, handler);
            }
            return false;
        }
    }
}

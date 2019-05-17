package com.cure.limit.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: dengmiao
 * @create: 2019-04-22 11:39
 **/
public interface CurrentInterceptorHandler {

    /**
     * 拦截器拦截后的反馈
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}

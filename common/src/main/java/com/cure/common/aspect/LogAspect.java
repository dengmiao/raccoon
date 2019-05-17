package com.cure.common.aspect;

import com.cure.common.toolkit.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @title: LogAspect
 * @description: 日志切面
 * @author: dengmiao
 * @create: 2019-05-17 16:30
 **/
@Slf4j
@Order(value = 0)
@Aspect
@Component
public class LogAspect {

    /**
     * 配置 controller 切面
     */
    @Pointcut(value = "execution(* *.corgi.*.*.*(..)) || execution(* *.corgi..*.controller.*.*(..)) || execution(* *.corgi.base.base.BaseController.*(..))")
    public void log() {

    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("log()")
    public void logDeBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        log.info("----------------------------------------------------------");
        log.info("用户代理:{}", UserAgentUtil.getUserAgent(request));
        log.info("请求路径:{}", request.getRequestURL().toString());
        log.info("请求类型:{}", request.getMethod());
        log.info("客户端IP:{}", request.getRemoteAddr());
        log.info("请求方法:{}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("请求参数:{}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 后置通知
     * @param returnValue
     */
    @AfterReturning(pointcut = "log()", returning = "returnValue")
    public void logDoAfterReturning(Object returnValue) {
        if (StringUtils.isEmpty(returnValue)) {
            returnValue = "";
        }
        log.info("请求响应:{}", returnValue);
        log.info("----------------------------------------------------------");
    }

    @Pointcut("@annotation(com.cure.common.annotation.Log)")
    public void logPointCut() {

    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfter(JoinPoint joinPoint) {

    }

    /**
     * LogAnnotation 日志处理
     *
     * @param joinPoint {@link JoinPoint}
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) {

    }
}

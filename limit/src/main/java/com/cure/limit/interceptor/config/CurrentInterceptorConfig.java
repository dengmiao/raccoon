package com.cure.limit.interceptor.config;

import com.cure.limit.handler.CurrentInterceptorHandler;
import com.cure.limit.handler.CurrentRuleHandler;
import com.cure.limit.interceptor.CustomCurrentInterceptor;
import com.cure.limit.interceptor.DefaultCurrentInterceptor;
import com.cure.limit.interceptor.properties.CurrentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: spring boot 2.0 拦截器
 * @author: dengmiao
 * @create: 2019-04-22 11:46
 **/
@Configuration
@ConditionalOnProperty(prefix = "current.limiting", name = "enabled", havingValue = "true")
public class CurrentInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CurrentProperties properties;

    @Autowired(required = false)
    private CurrentInterceptorHandler handler;

    @Autowired(required = false)
    private CurrentRuleHandler rule;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //是否自定义规则
        if (rule==null) {
            registry.addInterceptor(new DefaultCurrentInterceptor(properties, handler)).addPathPatterns("/**");
        }else {
            registry.addInterceptor(new CustomCurrentInterceptor(properties, handler,rule)).addPathPatterns("/**");
        }
    }
}

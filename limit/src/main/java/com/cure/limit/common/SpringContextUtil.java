package com.cure.limit.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description: 令牌桶算法：分布式、Redis
 * @author: dengmiao
 * @create: 2019-04-25 13:03
 **/
@Component(value = "limitSpringContextUtil")
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    private static String applicationName;
    private static String port;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        SpringContextUtil.applicationName = applicationContext.getId();
        SpringContextUtil.port = applicationContext.getEnvironment().getProperty("server.port");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String getPort() {
        return port;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<?> clz) throws BeansException {
        return (T) applicationContext.getBean(clz);
    }
}

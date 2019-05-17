package com.cure.limit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @title: SpringBootStarterLimitingAutoConfiguration
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 16:41
 **/
@Configuration
@ConditionalOnWebApplication
@ComponentScan
public class SpringBootStarterLimitingAutoConfiguration {
}

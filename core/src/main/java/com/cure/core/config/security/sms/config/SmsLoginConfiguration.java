package com.cure.core.config.security.sms.config;

import com.cure.captcha.CaptchaMessageHelper;
import com.cure.core.config.security.UserDetailsServiceImpl;
import com.cure.core.config.security.sms.SmsAuthenticationDetailsSource;
import com.cure.core.config.security.sms.SmsAuthenticationFailureHandler;
import com.cure.core.config.security.sms.SmsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: SmsLoginConfiguration
 * @description: 短信登录配置
 * @author: dengmiao
 * @create: 2019-05-24 09:40
 **/
@Configuration
public class SmsLoginConfiguration {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CaptchaMessageHelper captchaMessageHelper;

    /*@Bean
    public SmsAuthenticationFailureHandler smsAuthenticationFailureHandler() {
        return new SmsAuthenticationFailureHandler();
    }*/

    @Bean
    public SmsAuthenticationDetailsSource smsAuthenticationDetailsSource() {
        return new SmsAuthenticationDetailsSource();
    }

    @Bean
    public SmsAuthenticationProvider smsAuthenticationProvider() {
        return new SmsAuthenticationProvider(userDetailsService, captchaMessageHelper);
    }
}

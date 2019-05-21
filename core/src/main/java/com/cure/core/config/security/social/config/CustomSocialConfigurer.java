package com.cure.core.config.security.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: CustomSocialConfigurer
 * @description: social 配置器，支持设置Social过滤器处理地址
 * @author: dengmiao
 * @create: 2019-05-21 15:22
 **/
@Configuration
public class CustomSocialConfigurer extends SpringSocialConfigurer {

    private static final String DEFAULT_FILTER_PROCESSES_URL = "/openid";

    private String filterProcessesUrl = DEFAULT_FILTER_PROCESSES_URL;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    private AuthenticationSuccessHandler successHandler;

    private AuthenticationFailureHandler failureHandler;

    public CustomSocialConfigurer() { }

    public CustomSocialConfigurer(String filterProcessesUrl) {
        Assert.notNull(filterProcessesUrl, "social filterProcessesUrl should not be null.");
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter =  (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        if (this.successHandler != null) {
            filter.setAuthenticationSuccessHandler(this.successHandler);
        }
        if (this.failureHandler != null) {
            filter.setAuthenticationFailureHandler(this.failureHandler);
        }
        if (this.authenticationDetailsSource != null) {
            filter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
        }
        return (T) filter;
    }

    public CustomSocialConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public CustomSocialConfigurer failureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public CustomSocialConfigurer authenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return this;
    }
}

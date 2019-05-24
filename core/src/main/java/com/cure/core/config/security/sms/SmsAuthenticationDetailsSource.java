package com.cure.core.config.security.sms;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: SmsAuthenticationDetailsSource
 * @description: 自定义获取AuthenticationDetails 用于封装传进来的短信验证码
 * @author: dengmiao
 * @create: 2019-05-24 09:15
 **/
public class SmsAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new SmsAuthenticationDetails(request);
    }
}

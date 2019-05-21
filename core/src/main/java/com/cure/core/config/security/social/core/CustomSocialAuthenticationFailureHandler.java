package com.cure.core.config.security.social.core;

import com.cure.common.toolkit.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @title: CustomSocialAuthenticationFailureHandler
 * @description: Social 登录失败处理器
 * @author: dengmiao
 * @create: 2019-05-21 16:09
 **/
public class CustomSocialAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"登录失败，其他内部错误"));
    }
}

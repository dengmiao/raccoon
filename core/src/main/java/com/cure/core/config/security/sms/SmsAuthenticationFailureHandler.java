package com.cure.core.config.security.sms;

import com.cure.common.toolkit.MessageAccessor;
import com.cure.core.config.auto.CureProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @title: SmsAuthenticationFailureHandler
 * @description: 登录失败处理器
 * @author: dengmiao
 * @create: 2019-05-24 09:16
 **/
@Component
public class SmsAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private CureProperties securityProperties;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String mobile = httpServletRequest.getParameter("mobile");
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            session.setAttribute("username", mobile);
            session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
                    MessageAccessor.getMessage(e.getMessage(), e.getMessage()));
        }

        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, securityProperties.getLoginPage() +  "?type=mobile&username=" + mobile);
        // 返回失败的json
    }
}

package com.cure.core.config.security.social.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @title: CustomSocialAuthenticationSuccessHandler
 * @description: Social登录认证成功处理器
 * @author: dengmiao
 * @create: 2019-05-21 15:37
 **/
public class CustomSocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // 将 CustomSocialUserDetails 转换成 CustomUserDetails
        if (authentication instanceof SocialAuthenticationToken
                && authentication.getPrincipal() instanceof CustomSocialUserDetails) {
            CustomSocialUserDetails socialUserDetails = (CustomSocialUserDetails) authentication.getPrincipal();
            CustomUserDetails userDetails = new CustomUserDetails(
                    socialUserDetails.getUsername(),
                    socialUserDetails.getPassword(),
                    socialUserDetails.getUserId(),
                    socialUserDetails.getRealname(),
                    socialUserDetails.getAuthorities()
            );
            SocialAuthenticationToken socialAuthenticationToken = (SocialAuthenticationToken) authentication;
            socialAuthenticationToken.setDetails(userDetails);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

package com.cure.core.config.security.social.core;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.security.SocialUserDetails;

import java.util.Collection;

/**
 * @title: CustomSocialUserDetails
 * @description: 定制 SocialUserDetails 封装 Social 登录用户信息
 * @author: dengmiao
 * @create: 2019-05-21 15:49
 **/
@Data
public class CustomSocialUserDetails extends User implements SocialUserDetails {

    private String userId;

    private String realname;

    public CustomSocialUserDetails(String username, String password, String userId, String realname,
                                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.realname = realname;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}

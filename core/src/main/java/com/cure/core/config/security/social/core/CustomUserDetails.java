package com.cure.core.config.security.social.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @title: CustomUserDetails
 * @description: 定制的UserDetail对象
 * @author: dengmiao
 * @create: 2019-05-21 15:57
 **/
@Data
@EqualsAndHashCode
public class CustomUserDetails extends User {

    private String userId;

    private String realname;

    public CustomUserDetails(String username, String password, String userId, String realname, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.realname = realname;
    }
}

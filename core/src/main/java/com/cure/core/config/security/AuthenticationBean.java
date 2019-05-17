package com.cure.core.config.security;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 登录表单实体
 * @author: dengmiao
 * @create: 2019-04-09 11:12
 **/
@Getter
@Setter
public class AuthenticationBean {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 记住我
     */
    private boolean rememberMe;

    /**
     * 登录类型 account
     */
    private String type;
}

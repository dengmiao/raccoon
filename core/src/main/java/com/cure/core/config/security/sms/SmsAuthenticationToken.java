package com.cure.core.config.security.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @title: SmsAuthenticationToken
 * @description: 短信认证用到的 Authentication，封装登录信息。 认证前，放入手机号；认证成功之后，放入用户信息。
 * <p>参考 {@link UsernamePasswordAuthenticationToken}</p>
 * @author: dengmiao
 * @create: 2019-05-23 17:04
 **/
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 手机号
     */
    private final Object principal;

    public SmsAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
}

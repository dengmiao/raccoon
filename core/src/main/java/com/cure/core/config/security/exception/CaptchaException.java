package com.cure.core.config.security.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @title: CaptchaException
 * @description: 验证码异常
 * @author: dengmiao
 * @create: 2019-05-24 09:12
 **/
public class CaptchaException extends InternalAuthenticationServiceException {

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(String message) {
        super(message);
    }
}

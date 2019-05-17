package com.cure.core.common.exception;

import lombok.Data;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @description:
 * @author: dengmiao
 * @create: 2019-04-08 14:07
 **/
@Data
public class LoginFailLimitException extends InternalAuthenticationServiceException {

    private String msg;

    public LoginFailLimitException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    public LoginFailLimitException(String message) {
        super(message);
        this.msg = message;
    }
}

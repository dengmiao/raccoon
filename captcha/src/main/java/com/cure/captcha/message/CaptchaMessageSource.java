package com.cure.captcha.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @title: CaptchaMessageSource
 * @description:
 * @author: dengmiao
 * @create: 2019-05-23 17:52
 **/
public class CaptchaMessageSource {

    private CaptchaMessageSource() {}

    private static final MessageSource messageSource;

    static {
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setBasename("com.cure.captcha.messages");
        bundleMessageSource.setDefaultEncoding("UTF-8");
        bundleMessageSource.setUseCodeAsDefaultMessage(true);
        messageSource = bundleMessageSource;
    }

    /**
     * @param code code
     * @return messages
     */
    public static String getMessage(String code) {
        return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    /**
     * @param code code
     * @param args args
     * @return messages
     */
    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
    }
}

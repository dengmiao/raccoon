package com.cure.captcha.message;

import org.springframework.context.MessageSource;
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
        bundleMessageSource.setBasename("com.lyyzoo.sunny.captcha.messages");
        bundleMessageSource.setDefaultEncoding("UTF-8");
        bundleMessageSource.setUseCodeAsDefaultMessage(true);
        messageSource = bundleMessageSource;
    }

    /**
     * @param code code
     * @return message
     */
    public static String getMessage(String code) {
        return messageSource.getMessage(code, null, code, LanguageHelper.locale());
    }

    /**
     * @param code code
     * @param args args
     * @return message
     */
    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, code, LanguageHelper.locale());
    }
}

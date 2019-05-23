package com.cure.captcha;

import lombok.Data;

/**
 * @title: CaptchaResult
 * @description: 验证码结果封装
 * @author: dengmiao
 * @create: 2019-05-23 17:43
 **/
@Data
public class CaptchaResult {

    public static final String FIELD_CAPTCHA = "captcha";
    public static final String FIELD_CAPTCHA_KEY = "captchaKey";
    public static final String FIELD_LAST_CHECK_KEY = "lastCheckKey";

    /**
     * 验证码
     */
    private String captcha;
    /**
     * 缓存KEY
     */
    private String captchaKey;
    /**
     * 前置验证KEY
     */
    private String lastCheckKey;
    /**
     * 消息
     */
    private String message;
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 清除验证码
     */
    public void clearCaptcha() {
        this.captcha = null;
    }
}

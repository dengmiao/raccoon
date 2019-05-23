package com.cure.core.config.security.sms;

import com.cure.captcha.CaptchaResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: SmsAuthenticationDetails
 * @description: 封装短信验证码
 * @author: dengmiao
 * @create: 2019-05-23 17:21
 **/
@Data
@EqualsAndHashCode
public class SmsAuthenticationDetails extends WebAuthenticationDetails {

    private String inputCaptcha;
    private String captchaKey;

    public SmsAuthenticationDetails(HttpServletRequest request) {
        super(request);
        captchaKey = request.getParameter(CaptchaResult.FIELD_CAPTCHA_KEY);
        inputCaptcha = request.getParameter(CaptchaResult.FIELD_CAPTCHA);
    }
}

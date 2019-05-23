package com.cure.captcha;

import com.cure.captcha.autoconfigure.CaptchaProperties;
import com.cure.captcha.message.CaptchaMessageSource;
import com.cure.common.toolkit.RedisHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * @title: CaptchaImageHelper
 * @description: 图片验证码输出
 * @author: dengmiao
 * @create: 2019-05-23 17:37
 **/
@Slf4j
public class CaptchaImageHelper {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RedisHelper redisHelper;

    /**
     * 生成验证码并输出图片到指定输出流，验证码的key为UUID，设置到Cookie中，key和验证码将缓存到Redis中
     *
     * @param response HttpServletResponse
     * @param captchaCachePrefix 缓存验证码的前缀
     */
    public void generateAndWriteCaptchaImage(HttpServletResponse response, String captchaCachePrefix) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        ServletOutputStream out = null;
        try {
            String captcha = captchaProducer.createText();

            String captchaKey = CaptchaGenerator.generateCaptchaKey();
            Cookie cookie = new Cookie(CaptchaResult.FIELD_CAPTCHA_KEY, captchaKey);
            cookie.setPath(StringUtils.defaultIfEmpty("/", "/"));
            cookie.setMaxAge(-1);
            response.addCookie(cookie);

            // cache
            redisHelper.strSet(captchaCachePrefix + ":captcha:" + captchaKey, captcha, captchaProperties.getImage().getExpire(), TimeUnit.MINUTES);

            // output
            BufferedImage bi = captchaProducer.createImage(captcha);
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (Exception e) {
            log.info("create captcha fail: {}", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.info("captcha output close fail: {}", e);
                }
            }
        }
    }

    /**
     * 校验验证码
     *
     * @param request HttpServletRequest
     * @param captcha captcha
     * @param captchaCachePrefix captcha cache prefix
     */
    public CaptchaResult checkCaptcha(HttpServletRequest request, String captcha, String captchaCachePrefix) {
        Cookie captchaKeyCookie = WebUtils.getCookie(request, CaptchaResult.FIELD_CAPTCHA_KEY);
        if (captchaKeyCookie == null) {
            // throw new Exception("captcha key not null");
        }
        CaptchaResult captchaResult = new CaptchaResult();
        if (StringUtils.isBlank(captcha)) {
            captchaResult.setSuccess(false);
            captchaResult.setMessage(CaptchaMessageSource.getMessage("captcha.validate.captcha.notnull"));
            return captchaResult;
        }
        String captchaKey = captchaKeyCookie.getValue();
        String cacheCaptcha = redisHelper.strGet(captchaCachePrefix + ":captcha:" + captchaKey);
        redisHelper.delKey(captchaCachePrefix + ":captcha:" + captchaKey);
        if (!StringUtils.equalsIgnoreCase(cacheCaptcha, captcha)) {
            captchaResult.setSuccess(false);
            captchaResult.setMessage(CaptchaMessageSource.getMessage("captcha.validate.captcha.incorrect"));
            return captchaResult;
        }
        captchaResult.setSuccess(true);
        return captchaResult;
    }

    /**
     * 从request cookie 中获取验证码
     *
     * @param request HttpServletRequest
     * @param captchaCachePrefix captcha cache prefix
     */
    public String getCaptcha(HttpServletRequest request, String captchaCachePrefix) {
        Cookie captchaKeyCookie = WebUtils.getCookie(request, CaptchaResult.FIELD_CAPTCHA_KEY);
        if (captchaKeyCookie == null) {
            return null;
        }
        String captchaKey = captchaKeyCookie.getValue();
        String captcha = redisHelper.strGet(captchaCachePrefix + ":captcha:" + captchaKey);
        redisHelper.delKey(captchaCachePrefix + ":captcha:" + captchaKey);
        return captcha;
    }

    /**
     * 获取验证码
     *
     * @param captchaCachePrefix 缓存前缀
     * @param captchaKey 验证码KEY
     * @return 验证码
     */
    public String getCaptcha(String captchaCachePrefix, String captchaKey) {
        String captcha = redisHelper.strGet(captchaCachePrefix + ":captcha:" + captchaKey);
        redisHelper.delKey(captchaCachePrefix + ":captcha:" + captchaKey);
        return captcha;
    }
}

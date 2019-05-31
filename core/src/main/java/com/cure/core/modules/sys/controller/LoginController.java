package com.cure.core.modules.sys.controller;

import com.cure.captcha.CaptchaImageHelper;
import com.cure.captcha.CaptchaMessageHelper;
import com.cure.captcha.CaptchaResult;
import com.cure.common.toolkit.MessageAccessor;
import com.cure.common.vo.Result;
import com.cure.core.common.toolkit.SecurityUtil;
import com.cure.core.config.security.SecurityConstant;
import com.cure.core.modules.sys.entity.SysUser;
import com.cure.core.modules.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @title: SecurityController
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 20:54
 **/
@Slf4j
@Controller
public class LoginController {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private static final String LOGIN_PAGE = "login";

    private static final String INDEX_PAGE = "index";

    private static final String FIELD_ERROR_MSG = "errorMsg";
    private static final String FIELD_ENABLE_CAPTCHA = "enableCaptcha";

    @Autowired
    private CaptchaImageHelper captchaImageHelper;

    @Autowired
    private CaptchaMessageHelper captchaMessageHelper;

    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping(value = {"/index", "/"})
    public String index() {
        return INDEX_PAGE;
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model, String username, String type) {
        // 错误信息
        String errorMsg = (String) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (StringUtils.isNotBlank(errorMsg)) {
            model.addAttribute(FIELD_ERROR_MSG, errorMsg);
        }
        // 用户名
        if (StringUtils.isNotBlank(username)) {
            model.addAttribute(SysUser.FIELD_USERNAME, username);
            SysUser user = sysUserService.findByUsernameOrPhone(username, null);
        }
        // 登录类型
        type = StringUtils.defaultIfBlank(type, "account");
        model.addAttribute("type", type);

        return LOGIN_PAGE;
    }

    @GetMapping("/public/captcha.jpg")
    public void captcha(HttpServletResponse response) {
        captchaImageHelper.generateAndWriteCaptchaImage(response, SecurityConstant.SECURITY_KEY);
    }

    @GetMapping("/public/mobile/captcha")
    @ResponseBody
    public Result mobileCaptcha(@RequestParam String mobile) {
        SysUser sysUser = sysUserService.findByUsernameOrPhone(null, mobile);
        if (sysUser == null) {
            return Result.error(MessageAccessor.getMessage("user.error.login.mobile.not-register"));
        }
        CaptchaResult captchaResult = captchaMessageHelper.generateMobileCaptcha(mobile, SecurityConstant.SECURITY_KEY);
        if (captchaResult.isSuccess()) {
            // 模拟发送验证码
            log.info("【Cure】 您的短信验证码是 {}。若非本人发送，请忽略此短信。", captchaResult.getCaptcha());
            captchaResult.clearCaptcha();
            return Result.ok(captchaResult);
        }

        return Result.error(captchaResult.getMessage());
    }

    /**
     * 调换到注册或绑定三方账号页面
     */
    @GetMapping("/signup")
    public String signup() {

        return "register-or-bind";
    }

    /**
     * 绑定三方账号
     */
    @PostMapping("/bind")
    public String bind(String username, String password, Model model, HttpServletRequest request) {
        try {
            sysUserService.bindProvider(username, password, request);
        } catch (Exception e) {
            model.addAttribute(FIELD_ERROR_MSG, MessageAccessor.getMessage(e.getMessage()));
        }
        return INDEX_PAGE;
    }

    /**
     * 调换到注册页面
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/user/self")
    @ResponseBody
    public Result self() {
        SysUser sysUser = SecurityUtil.getLoginUser();
        return Result.ok(sysUser);
    }
}

package com.cure.core.config.security.sms;

import com.cure.captcha.CaptchaResult;
import com.cure.core.config.security.SecurityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

/**
 * @title: SmsAuthenticationProvider
 * @description: 短信登录认证器
 * <p>参考 {@link AbstractUserDetailsAuthenticationProvider}，{@link DaoAuthenticationProvider}</p>
 * @author: dengmiao
 * @create: 2019-05-23 17:14
 **/
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SmsAuthenticationToken.class, authentication,
                "Only SmsAuthenticationToken is supported");

        String mobile = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

        UserDetails user = retrieveUser(mobile, (SmsAuthenticationToken) authentication);
        Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");

        additionalAuthenticationChecks(user, (SmsAuthenticationToken) authentication);

        return createSuccessAuthentication(user, authentication, user);
    }

    /**
     * 只有 {@link SmsAuthenticationToken} 类型才使用该认证器
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected UserDetails retrieveUser(String mobile, SmsAuthenticationToken authentication)
            throws AuthenticationException {

        return getUserDetailsService().loadUserByUsername(mobile);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, SmsAuthenticationToken authentication)
            throws AuthenticationException {
        Assert.isInstanceOf(SmsAuthenticationDetails.class, authentication.getDetails());
        SmsAuthenticationDetails details = (SmsAuthenticationDetails) authentication.getDetails();
        String mobile = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        // 检查验证码
        String inputCaptcha = details.getInputCaptcha();
        String captchaKey = details.getCaptchaKey();
        if (StringUtils.isAnyEmpty(inputCaptcha, captchaKey)) {
            throw new CaptchaException("user.error.login.mobile-captcha.null");
        }
        CaptchaResult captchaResult = captchaMessageHelper.checkCaptcha(captchaKey, inputCaptcha, mobile,
                SecurityConstant.SECURITY_KEY, false);
        authentication.setDetails(null);

        if (!captchaResult.isSuccess()) {
            throw new CaptchaException(captchaResult.getMessage());
        }
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        SmsAuthenticationToken result =
                new SmsAuthenticationToken(principal, authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }
}

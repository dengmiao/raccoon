package com.cure.core.modules.sys.service.impl;

import com.cure.common.exception.CommonException;
import com.cure.core.modules.sys.entity.SysUser;
import com.cure.core.modules.sys.repository.SysUserRepository;
import com.cure.core.modules.sys.service.ISysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: SysUserServiceImpl
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 18:01
 **/
@Service
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserRepository sysUserRepository;

    private final ProviderSignInUtils providerSignInUtils;

    private final PasswordEncoder passwordEncoder;

    public SysUserServiceImpl(final SysUserRepository sysUserRepository, final ProviderSignInUtils providerSignInUtils, final PasswordEncoder passwordEncoder) {
        this.sysUserRepository = sysUserRepository;
        this.providerSignInUtils = providerSignInUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SysUser findByUsernameOrPhone(String username, String phone) {
        return sysUserRepository.findByUsernameOrPhone(username, phone);
    }

    @Override
    public void bindProvider(String username, String password, HttpServletRequest request) {
        // login
        SysUser user = findByUsernameOrPhone(username, null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new CommonException("user.error.login.username-or-password.error");
        }

        providerSignInUtils.doPostSignUp(user.getId().toString(), new ServletWebRequest(request));
    }
}

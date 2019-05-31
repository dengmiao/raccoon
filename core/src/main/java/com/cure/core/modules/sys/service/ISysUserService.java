package com.cure.core.modules.sys.service;

import com.cure.core.modules.sys.entity.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: ISysUserService
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 18:00
 **/
public interface ISysUserService {

    /**
     * 按用户名或电话查找
     * @param username
     * @param phone
     * @return
     */
    SysUser findByUsernameOrPhone(String username, String phone);

    /**
     * 绑定第三方账号
     * @param username
     * @param password
     * @param request
     */
    void bindProvider(String username, String password, HttpServletRequest request);
}

package com.cure.core.modules.sys.service;

import com.cure.core.modules.sys.entity.SysUser;

/**
 * @title: ISysUserService
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 18:00
 **/
public interface ISysUserService {

    /**
     * 按用户名查找
     * @param username
     * @return
     */
    SysUser findByUsername(String username);
}

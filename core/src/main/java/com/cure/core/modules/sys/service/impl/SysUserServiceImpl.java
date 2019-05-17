package com.cure.core.modules.sys.service.impl;

import com.cure.core.modules.sys.entity.SysUser;
import com.cure.core.modules.sys.repository.SysUserRepository;
import com.cure.core.modules.sys.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * @title: SysUserServiceImpl
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 18:01
 **/
@Service
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserRepository sysUserRepository;

    public SysUserServiceImpl(final SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    @Override
    public SysUser findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}

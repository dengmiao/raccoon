package com.cure.core.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cure.common.base.IBaseRepository;
import com.cure.core.modules.sys.entity.SysPermission;
import com.cure.core.modules.sys.mapper.SysPermissionMapper;
import com.cure.core.modules.sys.repository.SysPermissionRepository;
import com.cure.core.modules.sys.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: SysPermissionServiceImpl
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 20:10
 **/
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Override
    public IBaseRepository<SysPermission, Long> getRepository() {
        return sysPermissionRepository;
    }
}

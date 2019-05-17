package com.cure.core.modules.sys.repository;

import com.cure.common.base.IBaseRepository;
import com.cure.core.modules.sys.entity.SysUser;
import org.springframework.stereotype.Repository;

/**
 * @title: SysUserRepository
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 18:04
 **/
@Repository
public interface SysUserRepository extends IBaseRepository<SysUser, Long> {

    /**
     * 按用户名查找
     * @param username
     * @return
     */
    SysUser findByUsername(String username);
}

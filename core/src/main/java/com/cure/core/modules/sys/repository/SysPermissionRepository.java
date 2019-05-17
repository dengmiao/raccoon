package com.cure.core.modules.sys.repository;

import com.cure.common.base.IBaseRepository;
import com.cure.core.modules.sys.entity.SysPermission;
import org.springframework.stereotype.Repository;

/**
 * @title: SysPermissionRepository
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 20:19
 **/
@Repository
public interface SysPermissionRepository extends IBaseRepository<SysPermission, Long> {
}

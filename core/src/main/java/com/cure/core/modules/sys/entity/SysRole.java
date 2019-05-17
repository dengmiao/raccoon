package com.cure.core.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cure.common.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: demo
 * @description: 系统角色
 * @author: dengmiao
 * @create: 2019-04-06 15:51
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sys_role")
@TableName("sys_role")
@Where(clause = "del_flag = 0")
public class SysRole extends BaseEntity<Long> {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 数据范围
     * (1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置)
     */
    private String dataScope;
}

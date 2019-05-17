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
 * @description: 权限资源
 * @author: dengmiao
 * @create: 2019-04-06 14:30
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sys_permission")
@TableName("sys_permission")
@Where(clause = "del_flag = 0")
public class SysPermission extends BaseEntity<Long> {

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 组件
     */
    private String component;

    /**
     * 路径
     */
    private String url;
    /**
     * 一级菜单跳转地址
     */
    private String redirect;

    /**
     * 菜单排序
     */
    private Integer sortNo;

    /**
     * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
     */
    private Integer menuType;

    /**
     * 是否叶子节点: 1:是 0:不是
     */
    private Integer isLeaf;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否隐藏路由菜单: 0否,1是（默认值0）
     */
    private boolean hidden;

    /**
     * alwaysShow
     */
    private boolean alwaysShow;
}

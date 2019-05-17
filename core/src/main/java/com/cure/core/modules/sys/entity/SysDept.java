package com.cure.core.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cure.common.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @description: 组织机构
 * @author: dengmiao
 * @create: 2019-04-11 14:26
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sys_dept")
@TableName(value = "sys_dept")
@org.hibernate.annotations.Table(appliesTo = "sys_dept",comment = "组织机构")
@Where(clause = "del_flag = 0")
public class SysDept extends BaseEntity<Long> {

    /**
     * 父机构ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint(20) DEFAULT NULL COMMENT '父机构ID'")
    private Long parentId;

    /**
     * 父ID集合
     */
    @Column(name = "parent_ids", columnDefinition = "varchar(1000) DEFAULT NULL COMMENT '父ID集合'")
    private String parentIds;

    /**
     * 机构/部门名称
     */
    @Column(name = "depart_name", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '机构/部门名称'")
    private String departName;

    /**
     * 英文名
     */
    @Column(name = "depart_name_en", columnDefinition = "varchar(100) DEFAULT NULL COMMENT '英文名'")
    private String departNameEn;

    /**
     * 缩写
     */
    @Column(name = "depart_name_abbr", columnDefinition = "varchar(100) DEFAULT NULL COMMENT '缩写'")
    private String departNameAbbr;

    /**
     * 排序
     */
    @Column(name = "depart_order", columnDefinition = "int(11) DEFAULT 0 COMMENT '排序'")
    private Integer departOrder;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "text DEFAULT NULL COMMENT '描述'")
    private String description;

    /**
     * 机构类型
     */
    @Column(name = "org_type", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '机构类型'")
    private String orgType;

    /**
     * 机构编码
     */
    @Column(name = "org_code", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '机构编码'")
    private String orgCode;

    /**
     * 手机号
     */
    @Column(name = "mobile", columnDefinition = "varchar(50) DEFAULT NULL COMMENT '手机号'")
    private String mobile;

    /**
     * 传真
     */
    @Column(name = "fax", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '传真'")
    private String fax;

    /**
     * 地址
     */
    @Column(name = "address", columnDefinition = "varchar(1000) DEFAULT NULL COMMENT '地址'")
    private String address;

    /**
     * 备注
     */
    @Column(name = "memo", columnDefinition = "text DEFAULT NULL COMMENT '备注'")
    private String memo;

    /**
     * 状态（1启用，0不启用）
     */
    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1 COMMENT '状态（1启用，0不启用）'", length = 1)
    private Integer status;
}

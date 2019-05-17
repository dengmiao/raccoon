package com.cure.common.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.cure.common.json.FastJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: demo
 * @description: 实体基类
 * @author: dengmiao
 * @create: 2019-04-06 14:23
 **/
@Data
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JSONField(serializeUsing = FastJsonSerializer.class)
    @Column(name = "id")
    private T id;

    /**
     * 创建人
     */
    @Column(name = "create_by", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '创建人'")
    private String createBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_by", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '更新人'")
    private String updateBy;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", columnDefinition = "datetime COMMENT '更新时间'")
    private Date updateTime;

    /**
     * 删除状态 0正常 1已删除
     */
    @Column(name = "del_flag", columnDefinition = "int(1) DEFAULT NULL COMMENT '删除标识位 0正常 1已删除'", length = 1)
    @TableLogic
    private Integer delFlag;
}

package com.cure.common.vo;

import lombok.Data;

/**
 * @title: PageVo
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:06
 **/
@Data
public class PageVo {

    /**
     * 页号
     */
    private Integer pageNo = 1;

    /**
     * 页面大小
     */
    private Integer PageSize = 10;

    /**
     * 排序字段
     */
    private String column;

    /**
     * 排序方式 asc/desc
     */
    private String order = "asc";
}

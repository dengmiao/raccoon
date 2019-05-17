package com.cure.common.base;

import com.cure.common.base.provider.CustomMapperProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * @title: CustomMapper
 * @description: 自定义mapper
 * @author: dengmiao
 * @create: 2019-05-17 17:00
 **/
public interface CustomMapper<E extends BaseEntity, ID extends Serializable> {

    /**
     * where快查
     * @param clazz class
     * @param where 快查的where体
     * @return
     */
    @SelectProvider(type = CustomMapperProvider.class, method = "selectListWhere")
    List<E> selectListWhere(Class clazz, @Param("where") String where);
}

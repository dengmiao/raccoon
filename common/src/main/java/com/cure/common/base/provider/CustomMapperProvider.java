package com.cure.common.base.provider;

import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Table;

/**
 * @title: CustomMapperProvider
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:01
 **/
public class CustomMapperProvider {

    /**
     * 构造查询sql
     * @param clazz
     * @param where
     * @return
     * @throws Exception
     */
    public String selectListWhere(Class clazz, final String where) throws Exception {
        String tableName = null;
        if(clazz.isAnnotationPresent(TableName.class)) {
            TableName table = (TableName) clazz.getAnnotation(TableName.class);
            tableName = table.value();
        } else if(clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        } else {
            throw new Exception("unknown table name");
        }
        SQL sql = new SQL();
        sql.SELECT("*").FROM(tableName).WHERE(where);
        return sql.toString();
    }
}

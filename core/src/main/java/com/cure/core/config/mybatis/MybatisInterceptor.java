package com.cure.core.config.mybatis;

import com.cure.common.toolkit.ObjectUtil;
import com.cure.common.toolkit.ReflectUtil;
import com.cure.core.common.toolkit.SecurityUtil;
import com.cure.core.modules.sys.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import static com.cure.common.constant.CommonConstant.MetaField.*;

/**
 * @title: MybatisInterceptor
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:35
 **/
@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------=> {}", sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.info("------sqlCommandType------=> {}", sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            Field[] fields = ReflectUtil.getFields(parameter.getClass());
            for (Field field : fields) {
                log.debug("------field.name------=> {}", field.getName());
                try {
                    // 注入创建人
                    if (CREATE_BY.getProp().equals(field.getName())) {
                        fillUserId(field, parameter);
                    }
                    // 注入创建时间
                    else if (CREATE_TIME.getProp().equals(field.getName())) {
                        fillDate(field, parameter);
                    }
                    // 注入删除字段
                    else if (DEL_FLAG.getProp().equals(field.getName())) {
                        fillDelFlag(field, parameter);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields;
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
                parameter = p.get("param1");
            }
            fields = ReflectUtil.getFields(parameter.getClass());

            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if (UPDATE_BY.getProp().equals(field.getName())) {
                        fillUserId(field, parameter);
                    }
                    if (UPDATE_TIME.getProp().equals(field.getName())) {
                        fillDate(field, parameter);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 填充userId to createBy/updateBy
     * @param field
     * @param parameter
     * @throws IllegalAccessException
     */
    private void fillUserId(Field field, Object parameter) throws IllegalAccessException {
        field.setAccessible(true);
        Object localUserId = field.get(parameter);
        field.setAccessible(false);
        if (localUserId == null || localUserId.equals("")) {
            String userId = "1";
            // 获取登录用户信息
            SysUser sysUser = SecurityUtil.getLoginUser();
            if (sysUser != null) {
                // 登录账号
                userId = String.valueOf(sysUser.getId());
            }
            if (!ObjectUtil.isEmpty(userId)) {
                field.setAccessible(true);
                field.set(parameter, userId);
                field.setAccessible(false);
            }
        }
    }

    /**
     * 填充写入时间 to createTime/updateTime
     * @param field
     * @param parameter
     * @throws IllegalAccessException
     */
    private void fillDate(Field field, Object parameter) throws IllegalAccessException {
        field.setAccessible(true);
        Object localDate = field.get(parameter);
        field.setAccessible(false);
        if (localDate == null || localDate.equals("")) {
            field.setAccessible(true);
            field.set(parameter, new Date());
            field.setAccessible(false);
        }
    }

    /**
     * 注入删除字段默认值
     * @param field
     * @param parameter
     * @throws IllegalAccessException
     */
    private void fillDelFlag(Field field, Object parameter) throws IllegalAccessException {
        field.setAccessible(true);
        Object localData = field.get(parameter);
        field.setAccessible(false);
        if(localData == null || localData.equals("")) {
            field.setAccessible(true);
            field.set(parameter, DEL_FLAG.getDefaultValue());
            field.setAccessible(false);
        }
    }
}

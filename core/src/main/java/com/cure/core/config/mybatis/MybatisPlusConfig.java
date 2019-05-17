package com.cure.core.config.mybatis;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: MybatisPlusConfig
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:58
 **/
@Configuration
@MapperScan({"com.cure.**.mapper"})
public class MybatisPlusConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        return interceptor;
    }

    /**
     * 配置数据权限过滤带有逻辑删除
     * @return
     */
    @Bean
    public ISqlInjector dataScopeSqlInjector() {
        return new LogicSqlInjector();
    }

}

package com.cure.core.config.auto;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @title: CureAutoConfiguration
 * @description: 自动配置
 * @author: dengmiao
 * @create: 2019-05-17 17:53
 **/
@Data
@Log4j2
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = CureProperties.class)
public class CureAutoConfiguration {

    /**
     * 注入 CureProperties
     */
    private final CureProperties corgiProperties;

    /**
     * @param properties {@link CureProperties}
     */
    public CureAutoConfiguration(CureProperties properties) {
        this.corgiProperties = properties;

    }
}

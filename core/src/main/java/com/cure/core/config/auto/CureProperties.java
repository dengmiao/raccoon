package com.cure.core.config.auto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.cure.core.config.auto.CureProperties.DEFAULT_PREFIX;


/**
 * @title: CureProperties
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:52
 **/
@Data
@ConfigurationProperties(value = DEFAULT_PREFIX)
public class CureProperties {

    /**
     * 自动配置默认前缀
     */
    protected static final String DEFAULT_PREFIX = "cure.auto";

    /**
     * spring security 忽略鉴权的url集合
     */
    private final List<String> ignoreUrls;
}

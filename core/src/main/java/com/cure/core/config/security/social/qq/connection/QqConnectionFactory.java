package com.cure.core.config.security.social.qq.connection;

import com.cure.core.config.security.social.qq.api.QqApi;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @title: QqConnectionFactory
 * @description: qq Connection 工厂
 * @author: dengmiao
 * @create: 2019-05-21 13:47
 **/
public class QqConnectionFactory extends OAuth2ConnectionFactory<QqApi> {

    public QqConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QqServiceProvider(appId, appSecret), new QqApiAdapter());
    }
}

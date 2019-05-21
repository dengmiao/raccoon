package com.cure.core.config.security.social.qq.connection;

import com.cure.core.config.security.social.qq.api.DefaultQqApi;
import com.cure.core.config.security.social.qq.api.QqApi;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @title: QqServiceProvider
 * @description: qq服务提供商
 * @author: dengmiao
 * @create: 2019-05-21 11:42
 **/
public class QqServiceProvider extends AbstractOAuth2ServiceProvider<QqApi> {

    /**
     * 获取授权码地址(引导用户跳转到这个地址上去授权)
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 获取令牌地址
     */
    private static final String URL_GET_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    private String appId;

    public QqServiceProvider(String appId, String appSecret) {
        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_GET_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QqApi getApi(String accessToken) {
        return new DefaultQqApi(accessToken, appId);
    }
}

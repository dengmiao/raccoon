package com.cure.core.config.security.social.qq.api;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * @title: DefaultQqApi
 * @description:
 * @author: dengmiao
 * @create: 2019-05-21 10:21
 **/
@Slf4j
public class DefaultQqApi extends AbstractOAuth2ApiBinding implements QqApi {

    /**
     * QQ 获取 openId 的地址
     */
    private static final String URL_GET_OPEN_ID = "https://graph.qq.com/oauth2.0/me?access_token={accessToken}";

    /**
     * QQ 获取用户信息的地址
     */
    private static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key={appId}&openid={openId}";

    /**
     * 客户端 appId
     */
    private String appId;

    /**
     * openId
     */
    private String openId;

    private ObjectMapper mapper = new ObjectMapper();

    public DefaultQqApi(String accessToken, String appId) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        String result = getRestTemplate().getForObject(URL_GET_OPEN_ID, String.class, accessToken);

        log.debug(result);
        this.openId = StrUtil.subBetween(result, "\"openid\":", "\"}");
    }

    @Override
    public QqUserInfo getUserInfo() {
        String result = getRestTemplate().getForObject(URL_GET_USER_INFO, String.class, appId, openId);

        System.out.println(result);
        QqUserInfo qqUserInfo = null;
        try {
            qqUserInfo = mapper.readValue(result, QqUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取用户信息失败", e);
        }
        if(qqUserInfo == null) {
            // 抛异常
        }
        qqUserInfo.setOpenId(openId);
        return qqUserInfo;
    }
}

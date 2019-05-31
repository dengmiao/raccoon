package com.cure.core.config.security.social.qq.connection;

import com.cure.core.config.security.social.qq.api.QqApi;
import com.cure.core.config.security.social.qq.api.QqUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @title: QqApiAdapter
 * @description: qq api适配器
 * @author: dengmiao
 * @create: 2019-05-21 13:35
 **/
public class QqApiAdapter implements ApiAdapter<QqApi> {

    /**
     * 测试api连接是否可用
     * @param qqApi
     * @return
     */
    @Override
    public boolean test(QqApi qqApi) {
        return true;
    }

    /**
     * QQApi 与 Connection 做适配
     * @param qqApi
     * @param connectionValues
     */
    @Override
    public void setConnectionValues(QqApi qqApi, ConnectionValues connectionValues) {
        QqUserInfo userInfo = qqApi.getUserInfo();
        connectionValues.setDisplayName(userInfo.getNickname());
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        connectionValues.setProfileUrl(null);
        connectionValues.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QqApi qqApi) {
        return null;
    }

    @Override
    public void updateStatus(QqApi qqApi, String s) {

    }
}

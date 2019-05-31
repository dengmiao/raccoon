package com.cure.core.config.security.social.qq.connection;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @title: QqOauth2Template
 * @description: QQ定制 OAuth2Template
 * @author: dengmiao
 * @create: 2019-05-21 16:47
 **/
@Slf4j
public class QqOauth2Template extends OAuth2Template {

    public QqOauth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        // 设置带上 client_id、client_secret
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 解析 QQ 返回的令牌
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        // 返回格式：access_token=FE04********CCE2&expires_in=7776000&refresh_token=88E4***********BE14
        String result = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        if (StrUtil.isBlank(result)) {
            throw new RestClientException("access token endpoint returned empty result");
        }
        log.debug("==> get qq access_token: " + result);
        String[] arr = StrUtil.split(result, "&");
        String accessToken = "", expireIn = "", refreshToken = "";
        for (String s : arr) {
            if (s.contains("access_token")) {
                accessToken = s.split("=")[1];
            } else if (s.contains("expires_in")) {
                expireIn = s.split("=")[1];
            } else if (s.contains("refresh_token")) {
                refreshToken = s.split("=")[1];
            }
        }
        return createAccessGrant(accessToken, null, refreshToken, Long.valueOf(expireIn), null);
    }

    /**
     * QQ 响应 ContentType=text/html;因此需要加入 text/html; 的处理器
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charsets.UTF_8));
        return restTemplate;
    }
}

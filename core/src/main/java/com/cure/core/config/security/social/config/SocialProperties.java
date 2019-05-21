package com.cure.core.config.security.social.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @title: SocialProperties
 * @description:
 * @author: dengmiao
 * @create: 2019-05-21 14:32
 **/
@Data
@ConfigurationProperties(prefix = SocialProperties.PREFIX)
public class SocialProperties {

    protected static final String PREFIX = "cure.social";

    private Qq qq = new Qq();

    private WeChat weChat = new WeChat();

    /**
     * Social 注册或绑定页面
     */
    private String signUpPage = "/signup";

    @Data
    public static class Qq {
        /**
         * 服务商ID
         */
        private String providerId = "qq";
        /**
         * appId
         */
        private String appId;
        /**
         * appSecret
         */
        private String appSecret;
    }

    @Data
    public static class WeChat {
        /**
         * 服务商ID
         */
        private String providerId = "wechat";
        /**
         * appId
         */
        private String appId;
        /**
         * appSecret
         */
        private String appSecret;
    }
}

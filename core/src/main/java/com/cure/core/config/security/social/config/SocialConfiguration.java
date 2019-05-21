package com.cure.core.config.security.social.config;

import cn.hutool.core.util.StrUtil;
import com.cure.core.config.security.social.core.CustomSocialAuthenticationSuccessHandler;
import com.cure.core.config.security.social.qq.connection.QqConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;

/**
 * @title: SocialConfiguration
 * @description:
 * @author: dengmiao
 * @create: 2019-05-21 13:52
 **/
@Configuration
@EnableSocial
@EnableConfigurationProperties(SocialProperties.class)
public class SocialConfiguration extends SocialConfigurerAdapter {

    @Autowired
    private SocialProperties properties;

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository =  new JdbcUsersConnectionRepository(
        dataSource, connectionFactoryLocator, Encryptors.noOpText());
        // 表前缀
        repository.setTablePrefix("");
        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        // QQ
        SocialProperties.Qq qq = properties.getQq();
        if (!StrUtil.hasBlank(qq.getAppId(), qq.getAppSecret())) {
            connectionFactoryConfigurer.addConnectionFactory(
                    new QqConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret()));
        }

        // 微信
        SocialProperties.WeChat weChat = properties.getWeChat();
        if (!StrUtil.hasBlank(weChat.getAppId(), weChat.getAppSecret())) {
            // connectionFactoryConfigurer.addConnectionFactory(
                    // new WechatConnectionFactory(wechat.getProviderId(), wechat.getAppId(), wechat.getAppSecret()));
        }
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public CustomSocialAuthenticationSuccessHandler socialAuthenticationSuccessHandler() {
        return new CustomSocialAuthenticationSuccessHandler();
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator,
                                                   UsersConnectionRepository connectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }
}

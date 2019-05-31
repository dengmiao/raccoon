package com.cure.core.config.security;

import com.cure.core.config.auto.CureProperties;
import com.cure.core.config.security.jwt.AuthenticationFailHandler;
import com.cure.core.config.security.jwt.AuthenticationSuccessHandler;
import com.cure.core.config.security.jwt.JwtAuthenticationFilter;
import com.cure.core.config.security.jwt.RestAccessDeniedHandler;
import com.cure.core.config.security.permission.MyFilterSecurityInterceptor;
import com.cure.core.config.security.sms.SmsAuthenticationDetailsSource;
import com.cure.core.config.security.sms.SmsAuthenticationFailureHandler;
import com.cure.core.config.security.sms.SmsAuthenticationProvider;
import com.cure.core.config.security.sms.config.SmsLoginConfigurer;
import com.cure.core.config.security.social.config.CustomSocialConfigurer;
import com.cure.core.config.security.social.config.SocialProperties;
import com.cure.core.config.security.social.core.CustomSocialAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @description: spring security 核心配置类
 * @author: dengmiao
 * @create: 2019-04-04 16:44
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${cure.token.redis}")
    private Boolean tokenRedis;

    @Value("${cure.tokenExpireTime}")
    private Integer tokenExpireTime;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailHandler failHandler;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 外部配置
     */
    @Autowired
    private CureProperties cureProperties;

    @Autowired(required = false)
    private SmsAuthenticationFailureHandler smsAuthenticationFailureHandler;
    @Autowired(required = false)
    private SmsAuthenticationDetailsSource smsAuthenticationDetailsSource;
    @Autowired(required = false)
    private SmsAuthenticationProvider smsAuthenticationProvider;

    @Autowired(required = false)
    private CustomSocialAuthenticationSuccessHandler socialAuthenticationSuccessHandler;

    @Autowired
    private SocialProperties socialProperties;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 配置短信登录
        SmsLoginConfigurer smsLoginConfigurer = new SmsLoginConfigurer();
        smsLoginConfigurer
                .authenticationDetailsSource(smsAuthenticationDetailsSource)
                .successHandler(successHandler)
                .failureHandler(smsAuthenticationFailureHandler)
        ;
        http.apply(smsLoginConfigurer);
        http.authenticationProvider(smsAuthenticationProvider);

        // 配置社交登录
        CustomSocialConfigurer socialConfigurer = new CustomSocialConfigurer();
        socialConfigurer
                .successHandler(socialAuthenticationSuccessHandler)
                .signupUrl(socialProperties.getSignUpPage())
        ;
        http.apply(socialConfigurer);

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

        //除配置文件忽略路径其它所有请求都需经过认证和授权
        for(String url : cureProperties.getIgnoreUrls()){
            registry.antMatchers(url).permitAll();
        }

        registry.and()
                //表单登录方式
                .formLogin()
                .loginPage(cureProperties.getLoginPage())
                //登录请求url
                .loginProcessingUrl(cureProperties.getProcessingUrl())
                .permitAll()
                //成功处理类
                .successHandler(successHandler)
                //失败
                .failureHandler(failHandler)
                .and()
                //允许网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                //任何请求
                .anyRequest()
                //需要身份认证
                .authenticated()
                .and()
                //关闭跨站请求防护
                .csrf().disable()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                //添加自定义权限过滤器
                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                //添加JWT过滤器 除已配置的其它请求都需经过此过滤器
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenRedis, tokenExpireTime, redisTemplate))
                // spring security就支持表单登录和异步json登录了
                .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failHandler);
        filter.setFilterProcessesUrl("/login");

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

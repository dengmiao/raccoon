package com.cure.core.config.security;

import cn.hutool.core.util.StrUtil;
import com.cure.common.annotation.Log;
import com.cure.core.config.security.social.core.CustomSocialUserDetails;
import com.cure.core.modules.sys.entity.SysUser;
import com.cure.core.modules.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: demo
 * @description:
 * @author: dengmiao
 * @create: 2019-04-07 14:34
 **/
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 表单登录
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Log(model = "LOGIN", action = "/login")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String flagKey = "loginFailFlag:"+username;
        String value = redisTemplate.opsForValue().get(flagKey);
        Long timeRest = redisTemplate.getExpire(flagKey, TimeUnit.MINUTES);
        if(StrUtil.isNotBlank(value)){
            //超过限制次数
            throw new RuntimeException("登录错误次数超过限制，请"+timeRest+"分钟后再试");
        }
        SysUser user = iSysUserService.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new SecurityUserDetails(user);
    }

    /**
     * 社交登录
     * @param userId 用户唯一标识 id或username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        SysUser user = iSysUserService.findByUsername(userId);
        if(user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new CustomSocialUserDetails(user.getUsername(), user.getPassword(),
                String.valueOf(user.getId()), user.getRealname(),
                // 权限
                null);
    }
}

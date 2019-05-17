package com.cure.core.config.security;

import cn.hutool.core.util.StrUtil;
import com.cure.common.annotation.Log;
import com.cure.core.modules.sys.entity.SysUser;
import com.cure.core.modules.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ISysUserService iSysUserService;

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
}

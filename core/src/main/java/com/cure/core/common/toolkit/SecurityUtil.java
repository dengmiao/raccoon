package com.cure.core.common.toolkit;

import com.cure.common.toolkit.SpringContextUtil;
import com.cure.core.config.security.SecurityUserDetails;
import com.cure.core.config.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

/**
 * @title: SecurityUtil
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:37
 **/
public class SecurityUtil {

    public static SecurityUserDetails getLoginUser() {
        refresh();
        //获取当前用户信息
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (StringUtils.isEmpty(details)) {
            details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        if (details instanceof UserDetails) {
            return ((SecurityUserDetails) details);
        } else {
            details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ((SecurityUserDetails) details);
        }
    }

    /**
     * 刷新当前登录用户的 LoginUserDetails
     *
     */
    public static void refresh() {
        UserDetailsService detailsService = getUserDetailsService();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //重新查询载入 SecurityUserDetails
        SecurityUserDetails userDetails = (SecurityUserDetails) detailsService
                .loadUserByUsername(((User) auth.getPrincipal()).getUsername());
        //设置权限
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
                auth.getCredentials(), userDetails.getAuthorities());

        //设置setDetails
        newAuth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /**
     * 获取 UserDetailsService
     * @return {@link UserDetailsService}
     */
    private static UserDetailsService getUserDetailsService() {
        return (UserDetailsService) SpringContextUtil.getBean(UserDetailsServiceImpl.class);
    }

    /**
     * 获取  用户ID
     * @return
     */
    public static Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((SecurityUserDetails) principal).getId();
        }
        return null;
    }
}

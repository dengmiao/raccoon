package com.cure.core.config.security;

import cn.hutool.core.util.StrUtil;
import com.cure.core.modules.sys.entity.SysPermission;
import com.cure.core.modules.sys.entity.SysRole;
import com.cure.core.modules.sys.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: demo
 * @description:
 * @author: dengmiao
 * @create: 2019-04-07 14:37
 **/
@Slf4j
public class SecurityUserDetails extends SysUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(SysUser user) {
        if(user!=null) {
            this.setId(user.getId());
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<SysPermission> permissions = this.getPermissions();
        // 添加请求权限
        if(permissions!=null&&permissions.size()>0){
            for (SysPermission permission : permissions) {
                /*if(CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                        && StrUtil.isNotBlank(permission.getName())
                        &&StrUtil.isNotBlank(permission.getUrl())) {

                    authorityList.add(new SimpleGrantedAuthority(permission.getName()));
                }*/
                authorityList.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        // 添加角色
        List<SysRole> roles = this.getRoles();
        if(roles!=null&&roles.size()>0){
            // lambda表达式
            roles.forEach(item -> {
                if(StrUtil.isNotBlank(item.getRoleName())){
                    authorityList.add(new SimpleGrantedAuthority(item.getRoleName()));
                }
            });
        }
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getStatus() != null && this.getStatus().intValue() == 1 ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus() != null && this.getStatus().intValue() == 1 ? true : false;
    }
}

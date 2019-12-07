package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class SecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByName(username);
        if (user==null){
            //说明通过这个名字在数据库没有查到相应的user对象
            return null;
        }
        //不是空的话,就动态给这个user授权
        List<GrantedAuthority>list=new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (roles!=null){
            //如果角色不为空,遍历角色
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                //角色授权完成之后,还要把该角色下的权限授予用户
                Set<Permission> permissions = role.getPermissions();
                if (permissions!=null){
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }

        //这样授权角色和授权权限都完成了,还要让框架根据用户传的用户名和密码判断是否登录成功,顺便把权限的集合list传入
        org.springframework.security.core.userdetails.User securityUser=new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return securityUser;
    }

}

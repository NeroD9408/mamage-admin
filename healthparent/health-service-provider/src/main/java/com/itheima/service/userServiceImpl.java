package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class userServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public User findByName(String name) {
        User user = userDao.findByName(name);
        if (user==null){
            return null;
        }
        //如果用户不是null,用户存在,那除了基本信息之外,还得查出他对应的角色,以及角色下对应的权限,先查角色
        Set<Role> roles=roleDao.findRolesByUserId(user.getId());
        for (Role role : roles) {
            //遍历角色集合,获取每一个角色,用角色的id查询对应的权限
            Set<Permission> permissions=permissionDao.findPermissionsByRoleId(role.getId());
            role.setPermissions(permissions);
        }

        user.setRoles(roles);
        return user;
    }
}

package com.itheima.service;


import com.itheima.pojo.Role;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;

import java.util.List;
import java.util.Map;

public interface RoleService {

    PageResult findPage(QueryPageBean queryPageBean);

    List<Role> findRoleMenus();

    void addRole(Map req);

    Role findRoleById(Integer rid);

    void updateRole(Map req);

    Result deleteRole(Integer rid);
}

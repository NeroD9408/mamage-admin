package com.itheima.service;

import com.itheima.pojo.Permission;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

import java.util.List;

public interface PermissionService {

    PageResult findPage(QueryPageBean queryPageBean);

    List<Permission> findAllPermission();

    void add(Permission permission);

    Permission findById(Integer pid);

    void delete(Integer pid);

    void update(Permission permission);
}

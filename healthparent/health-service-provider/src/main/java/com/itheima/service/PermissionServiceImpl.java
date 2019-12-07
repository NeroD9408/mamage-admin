package com.itheima.service;

import com.itheima.pojo.Permission;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.dao.PermissionMapper;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Permission> page = permissionMapper.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Permission> findAllPermission() {
        return permissionMapper.findAllPermission();
    }

    @Override
    public void add(Permission permission) {
        permissionMapper.add(permission);
    }

    @Override
    public Permission findById(Integer pid) {
        return permissionMapper.findById(pid);
    }

    @Override
    public void delete(Integer pid) {
        //先清空关系表中关联关系
        permissionMapper.clearByPid(pid);
        //删除权限信息
        permissionMapper.delete(pid);
    }

    @Override
    public void update(Permission permission) {
        permissionMapper.update(permission);
    }

}

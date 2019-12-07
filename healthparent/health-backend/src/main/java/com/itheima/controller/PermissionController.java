package com.itheima.controller;

import com.itheima.pojo.Permission;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.PermissionService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult page = null;
        try {
            page = permissionService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @RequestMapping("/findAllPermission")
    public Result findAllPermission() {
        try {
            List<Permission> permissionList = permissionService.findAllPermission();
            return new Result(true, "", permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取权限列表失败");
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
            return new Result(true, "添加权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加权限失败");
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer pid) {
        try {
            Permission permission = permissionService.findById(pid);
            return new Result(true, "获取权限信息成功", permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取权限信息失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Permission permission) {
        try {
            permissionService.update(permission);
            return new Result(true, "修改权限信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改权限信息失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer pid) {
        try {
            permissionService.delete(pid);
            return new Result(true, "删除权限信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除权限信息失败");
        }
    }
}

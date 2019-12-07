package com.itheima.controller;

import com.itheima.pojo.Role;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.RoleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult roleList = null;
        try {
            roleList = roleService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleList;
    }

    @RequestMapping("/findRoleMenus")
    public Result findRoleMenus() {
        try {
            List<Role> roleList = roleService.findRoleMenus();
            return new Result(true, "获取用户菜单成功", roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取用户菜单错误");
        }
    }

    @RequestMapping("/findRoleById")
    public Result findRoleById(Integer rid) {
        try {
            Role role = roleService.findRoleById(rid);
            return new Result(true, "", role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取角色信息失败");
        }
    }

    @RequestMapping("/addRole")
    public Result addRole(@RequestBody Map req) {
        System.out.println(req.size());
        try {
            roleService.addRole(req);
            return new Result(true, "添加角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加角色失败");
        }
    }

    @RequestMapping("/updateRole")
    public Result updateRole(@RequestBody Map req) {
        System.out.println(req.size());
        try {
            roleService.updateRole(req);
            return new Result(true, "修改角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改角色失败");
        }
    }

    @RequestMapping("/deleteRole")
    public Result deleteRole(Integer rid) {
        return roleService.deleteRole(rid);
    }
    
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Role> roleList = roleService.findRoleMenus();
            return new Result(true, "", roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取角色失败");
        }
    }

}

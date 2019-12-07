package com.itheima.service;

import com.itheima.pojo.Role;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.dao.MenuMapper;
import com.itheima.dao.RoleMapper;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        //首先获取所有的角色信息以及角色所包含的权限信息和菜单信息
        Page<Role> roles = roleMapper.findAllRoleDetail(queryString);
        List<Role> result = roles.getResult();
        PageInfo pi = new PageInfo(result);
        System.out.println(pi.getTotal());
        System.out.println(pi.getSize());
        return new PageResult(pi.getTotal(), result);
    }

    @Override
    public List<Role> findRoleMenus() {
        return roleMapper.findRoleMenu();
    }

    @Override
    public void addRole(Map req) {
        //获取角色信息
        Map map = (Map) req.get("role");
        String name = (String) map.get("name");
        String keyword = (String) map.get("keyword");
        String description = (String) map.get("description");
        Role role = new Role(name, keyword, description);

        //获取权限列表
        List<Integer> permissionIds = (List<Integer>) req.get("permissionIds");
        //获取菜单path字段的id
        List<String> menuIds = (List<String>) req.get("menuIds");
        Set<String> menuids = new LinkedHashSet<>();
        //修改角色
        roleMapper.addRole(role);
        //获取新添加角色的id
        Integer rid = role.getId();
        //添加角色权限
        for (Integer pid : permissionIds) {
            roleMapper.addRolePermission(pid, rid);
        }

        //添加角色菜单，首先根据获取到的path查询到菜单的id
        for (String menuId : menuIds) {
            menuids.add(menuId);
        }
        Set<Integer> mids = menuMapper.findIdByPath(menuids);
        //添加角色和菜单id到中间表
        for (Integer mid : mids) {
            roleMapper.addRoleMenu(mid, rid);
        }
    }

    @Override
    public Role findRoleById(Integer rid) {
        return roleMapper.findRoleById(rid);
    }

    @Override
    public void updateRole(Map req) {
        //获取角色信息
        Map map = (Map) req.get("role");
        String name = (String) map.get("name");
        String keyword = (String) map.get("keyword");
        String description = (String) map.get("description");
        Integer id = (Integer) map.get("id");
        Role role = new Role(name, keyword, description);
        role.setId(id);
        //获取权限列表
        List<Integer> permissionIds = (List<Integer>) req.get("permissionIds");
        //获取菜单path字段的id
        List<String> menuIds = (List<String>) req.get("menuIds");
        //修改角色
        roleMapper.updateRole(role);
        //获取新添加角色的id
        Integer rid = role.getId();
        //清空角色的权限
        roleMapper.clearPermissionByRid(rid);
        //清空角色的菜单
        roleMapper.clearRoleMenuByRid(rid);
        //如果用户勾选了权限，在进行添加
        if (permissionIds!=null && permissionIds.size() > 0) {
            //添加角色权限
            for (Integer pid : permissionIds) {
                roleMapper.addRolePermission(pid, rid);
            }
        }
        //如果用户勾选了菜单在进行添加
        if (menuIds != null && menuIds.size() > 0) {
            Set<String> menuids = new LinkedHashSet<>();
            //添加角色菜单，首先根据获取到的path查询到菜单的id
            for (String menuId : menuIds) {
                menuids.add(menuId);
            }
            //通过menu的path获取menu的id
            Set<Integer> mids = menuMapper.findIdByPath(menuids);
            //添加角色和菜单id到中间表
            for (Integer mid : mids) {
                roleMapper.addRoleMenu(mid, rid);
            }
        }
    }

    @Override
    public Result deleteRole(Integer rid) {
        //直接在role表中删除数据，如果删除失败则说明该角色还有未删除的菜单或权限，拒绝删除
        try {
            roleMapper.deleteRole(rid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "该角色存在关联权限或菜单,禁止删除");
        }
        return new Result(true, "删除成功");
    }

}

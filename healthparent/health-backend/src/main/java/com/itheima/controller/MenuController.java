package com.itheima.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Menu;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.MenuService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @RequestMapping("/getMenuList")
    public Result getMenuList(Integer uid) {
        List<Map> menuList = menuService.getMenuList(uid);
        return new Result(true, "", menuList);
    }

    @RequestMapping("/findAllMenu")
    public Result findAllPermission() {
        try {
            List<Menu> menuList = menuService.findAllMenu();
            return new Result(true, "", menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取菜单列表失败");
        }

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            return menuService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Menu> menuList = menuService.findAll();
            return new Result(true, "", menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取菜单信息失败");
        }
    }

    @RequestMapping("/addOneMenu")
    public Result addOneMenu(@RequestBody Menu menu) {
        try {
            menuService.addOneMenu(menu);
            return new Result(true, "新增一级菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增一级菜单失败");
        }
    }

    @RequestMapping("/getMaxPath")
    public Result getMaxPath() {
        try {
            Integer maxPath = menuService.getMaxPath();
            return new Result(true, "", maxPath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取最大path失败");
        }
    }

    @RequestMapping("/addMenu")
    public Result addMenu(@RequestBody Menu menu) {
        try {
            menuService.addMenu(menu);
            return new Result(true, "新增菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增菜单失败");
        }
    }

    @RequestMapping("/findNodePath")
    public Result findNodePath(Integer id) {
        try {
            String maxPath = menuService.findNodePath(id);
            if (maxPath == null) {
                maxPath = "0";
            }
            System.out.println(maxPath);
            return new Result(true, "", maxPath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询失败", 0);
        }
    }

    @RequestMapping("/editParentMenu")
    public Result editParentMenu(@RequestBody Menu menu) {
        try {
            System.out.println(menu.getId());
            System.out.println(menu.getName());
            System.out.println(menu.getPath());
            menuService.editParentMenu(menu);
            return new Result(true, "修改菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改菜单失败");
        }
    }

    @RequestMapping("/editMenu")
    public Result editMenu(@RequestBody Menu menu) {
        try {
            menuService.editMenu(menu);
            return new Result(true, "修改菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改菜单失败");
        }
    }

    @RequestMapping("/canDeleteMenu")
    public Result canDeleteMenu(Integer id) {
        boolean flag = false;
        try {
            flag = menuService.canDeleteMenu(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            return new Result(true, "");
        } else {
            return new Result(false, "该菜单仍被使用，禁止删除");
        }
    }

    @RequestMapping("/deleteMenu")
    public Result deleteMenu(@RequestBody Menu menu) {
        try {
            menuService.deleteMenu(menu);
            return new Result(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "菜单删除失败");
        }
    }

    @RequestMapping("/findOnePath")
    public Result findOnePath() {
        try {
            Integer path = menuService.findOnePath();
            return new Result(true, "", path);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取失败");
        }
    }

    @RequestMapping("/editOneLevelMenu")
    public Result editOneLevelMenu(@RequestBody Menu[] menus) {
        menuService.editOneLevelMenu(menus);
        return new Result(true, "");
    }
}

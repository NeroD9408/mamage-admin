package com.itheima.utils;

import com.itheima.pojo.Menu;
import com.itheima.dao.MenuMapper;

import java.util.*;

public class GetMenuListUtils {

    //获取角色对应的菜单列表
    public static List<Map> getMenuList(MenuMapper menuMapper, Integer uid) {
        List<Map> menuList = new ArrayList<>();
        //根据角色id查询到角色对应的menu列表
        LinkedHashSet<Menu> topMenu = menuMapper.findTopMenu(uid);
        if (topMenu != null && topMenu.size() > 0) {
            for (Menu menu : topMenu) {
                Map topMap = new HashMap();
                topMap.put("path", menu.getPath());
                topMap.put("title", menu.getName());
                topMap.put("icon", menu.getIcon());
                topMap.put("linkUrl", menu.getLinkUrl());
                //一级菜单id
                Integer id = menu.getId();
                List<Map> child = getChild(menuMapper, id, uid);
                topMap.put("children", child);
                menuList.add(topMap);
            }
        }
        return menuList;
    }

    //获取子菜单列表
    private static List<Map> getChild(MenuMapper menuMapper, Integer parentId, Integer uid) {
        List<Map> children = new ArrayList<>();
        LinkedHashSet<Menu> childMenus = menuMapper.findUserChildMenu(parentId, uid);
        if (childMenus != null && childMenus.size() > 0) {
            for (Menu menu : childMenus) {
                Map childMap = new HashMap();
                childMap.put("path", menu.getPath());
                childMap.put("title", menu.getName());
                childMap.put("linkUrl", menu.getLinkUrl());
                Integer id = menu.getId();
                List<Map> child = getChild(menuMapper, id, uid);
                childMap.put("children", child);
                children.add(childMap);
            }
        }
        return children;
    }
}

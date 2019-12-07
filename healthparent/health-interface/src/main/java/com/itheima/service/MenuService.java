package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface MenuService {

    List<Map> getMenuList(Integer id);

    List<Menu> findAllMenu();

    PageResult findPage(QueryPageBean queryPageBean);

    List<Menu> findAll();

    void addOneMenu(Menu menu);

    Integer getMaxPath();

    void addMenu(Menu menu);

    String findNodePath(Integer id);

    void editParentMenu(Menu menu);

    void editMenu(Menu menu);

    boolean canDeleteMenu(Integer id);

    void deleteMenu(Menu menu);

    Integer findOnePath();

    void editOneLevelMenu(Menu[] menus);

}

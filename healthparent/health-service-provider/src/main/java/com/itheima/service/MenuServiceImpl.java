package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.entity.QueryPageBean;
import com.itheima.dao.MenuMapper;
import com.itheima.utils.GetMenuListUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Map> getMenuList(Integer uid) {
        List<Map> menuList = GetMenuListUtils.getMenuList(menuMapper, uid);
        return menuList;
    }

    @Override
    public List<Menu> findAllMenu() {
        return menuMapper.findAllMenu();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Menu> page = menuMapper.findPage(queryString);
        System.out.println(page.getPages());
        System.out.println(page.getTotal());

        return new PageResult(page.getTotal(),page);
    }

    @Override
    public List<Menu> findAll() {
        return menuMapper.findAll();
    }

    @Override
    public void addOneMenu(Menu menu) {
        menuMapper.addOneMenu(menu);
    }

    @Override
    public Integer getMaxPath() {
        return menuMapper.getMaxPath();
    }

    @Override
    public void addMenu(Menu menu) {
        menuMapper.addOneMenu(menu);
    }

    @Override
    public String findNodePath(Integer id) {
        return menuMapper.findNodePath(id);
    }

    @Override
    public void editParentMenu(Menu menu) {
        //现在获取到的是父节点，所以需要遍历父节点中的数据,然后修改子节点的内容
        List<Menu> children = menu.getChildren();
        String parentPath = menu.getPath();
        Integer a = 1;
        if (children != null && children.size() > 0) {
            for (Menu child : children) {
                child.setPriority(a);
                child.setPath(parentPath + "-" + child.getPriority());
                menuMapper.editMenu(child);
                editParentMenu(child);
                a++;
            }
        }
    }

    @Override
    public void editMenu(Menu menu) {
        menuMapper.editMenu(menu);
    }

    @Override
    public boolean canDeleteMenu(Integer id) {
        Integer count = menuMapper.canDeleteMenu(id);
        if (count != 0) {
            //说明存在关联关系
            return false;
        } else {
            //说明不存在关联关系，可以删除
            return true;
        }
    }

    @Override
    public void deleteMenu(Menu menu) {
        List<Menu> children = menu.getChildren();
        if (children != null) {
            for (Menu child : children) {
                deleteMenu(child);
            }
        }
        menuMapper.deleteMenu(menu.getId());
    }

    @Override
    public Integer findOnePath() {
        return menuMapper.findOnePath();
    }

    @Override
    public void editOneLevelMenu(Menu[] menus) {
        Integer a = 1;
        if (menus != null && menus.length > 0) {
            for (Menu child : menus) {
                child.setPriority(a);
                child.setPath("/" + a);
                editParentMenu(child);
                menuMapper.editMenu(child);
                a++;
            }
        }
    }
}

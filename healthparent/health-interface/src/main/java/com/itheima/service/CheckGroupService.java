package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(Integer[] checkitemIds, CheckGroup checkGroup);

    PageResult queryPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemsById(Integer id);

    void update(Integer[] checkitemIds, CheckGroup checkGroup);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

    void continueDelete(Integer id);
}

package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    public void add(CheckItem checkItem);

    PageResult queryPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();

    void continueDelete(Integer id);
}

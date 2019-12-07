package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);

    Page<CheckItem> findByConition(String queryString);

    void deleteById(Integer id);

    public long findCountById(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();

    CheckItem newFindById(int id);

    void continueDelete(Integer id);
}

package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void addCheckGroup(CheckGroup checkGroup);


    void addReference(Map<String, Integer> map);

    Page<CheckGroup> findByCondition(String conditions);


    CheckGroup findById(Integer id);

    List<Integer> findCheckItemsById(Integer id);

    void updateCheckGroup(CheckGroup checkGroup);

    void deleteReferenceById(Integer checkGroupid);

    Integer findCountInCheckgroupCheckitem(Integer id);

    Integer findCountInSetmealCheckgroup(Integer id);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

    void deleteCheckItemsReference(Integer id);

    void deletesetmealsReference(Integer id);

    CheckGroup newFindById(int id);

}

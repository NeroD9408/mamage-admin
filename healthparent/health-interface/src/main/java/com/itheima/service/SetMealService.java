package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    void add(Setmeal setmeal,Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findcheckgroupIds(Integer id);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    void deleteById(Integer id);

    List<Setmeal> getAllSetmeal();

    Setmeal findSetmealById(Integer id);

    Map<String,Object> findSetmealNameAndValue();

    List<Setmeal> findAll();

    List<Integer> getCheckIds(Integer orderId);
}

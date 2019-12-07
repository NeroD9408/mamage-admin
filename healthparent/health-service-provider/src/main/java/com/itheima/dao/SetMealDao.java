package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    void addSetMeal(Setmeal setmeal);

    void addReference(Map<String, Integer> map);

    Page<Setmeal> findByCondition(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findcheckgroupIds(Integer id);

    void update(Setmeal setmeal);

    void deleteReference(Integer setMealId);

    void deleteById(Integer id);

    List<Setmeal> getAllSetmeal();

    Setmeal newFindById(Integer id);

    List<Setmeal> findAll();


    Integer findIdByCode(String code);

    List<Integer> getCheckIds(Integer orderId);

}

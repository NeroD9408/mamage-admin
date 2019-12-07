package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Food;
import com.itheima.pojo.Sport;

public interface FoodDao {
    public void add(Food food);
    public long findCountById(Integer id);
    public Page<Food> findByConition(String queryString);
    public Food findById(Integer id);
    public void deleteById(Integer id);
    public void update(Food food);
}

package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Food;

import java.util.List;


public interface FoodService {
    void add(List<Food> dataList);
    PageResult queryPage(QueryPageBean queryPageBean);
    Food findById(Integer id);
    void addFood(Food food);
    void deleteById(Integer id);
    void update(Food food);
}

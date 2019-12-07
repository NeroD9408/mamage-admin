package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Sport;

import java.util.List;

//11111
public interface SportService {
    void add(List<Sport> dataList);
    PageResult queryPage(QueryPageBean queryPageBean);
    Sport findById(Integer id);
    void addSport(Sport sport);
    void deleteById(Integer id);
    void update(Sport sport);
    boolean check(String num);
}

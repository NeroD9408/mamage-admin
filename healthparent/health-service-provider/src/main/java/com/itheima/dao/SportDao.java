package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Sport;

//111111
public interface SportDao {
    public void add(Sport sport);
    public long findCountByNumber(String number);
    public void editDateByNumber(Sport sport);
    public Page<Sport> findByConition(String queryString);
    public Sport findById(Integer id);
    public void deleteById(Integer id);
    public void update(Sport sport);
}

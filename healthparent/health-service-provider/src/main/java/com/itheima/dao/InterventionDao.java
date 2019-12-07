package com.itheima.dao;

import com.itheima.pojo.Intervention;

public interface InterventionDao {
    String findFood(int a);

    String findSport(int c);

    String findMedicine(int e);

    void add(Intervention intervention);

    Intervention findInterventionById(Integer interventionId);
}

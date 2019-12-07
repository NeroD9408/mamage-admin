package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Questions;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    PageResult findPage(QueryPageBean queryPageBean);

    Questions findById(Integer id);

    void add(Questions questions);

    void update(Questions questions);

    void deleteById(Integer id);

    List<Questions> getRandomQuestion();

    List<Questions> findAll();

    Map getResultByTitle(String title);
}

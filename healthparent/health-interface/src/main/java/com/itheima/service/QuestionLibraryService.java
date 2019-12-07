package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.QuestionLibrary;

import java.util.List;

public interface QuestionLibraryService {
    void add(Integer[] questionIds, QuestionLibrary questionLibrary);

    PageResult queryPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findQuestionById(Integer id);

    void update(Integer[] questionIds, QuestionLibrary questionLibrary);

    void deleteById(Integer id);
}

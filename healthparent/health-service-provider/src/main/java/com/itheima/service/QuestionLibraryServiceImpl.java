package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.QuestionLibraryMapper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.QuestionLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = QuestionLibraryService.class)
@Transactional
public class QuestionLibraryServiceImpl implements QuestionLibraryService {

    @Autowired
    private QuestionLibraryMapper mapper;

    @Override
    public void add(Integer[] questionIds, QuestionLibrary questionLibrary) {
        //添加题库的基本信息
        mapper.add(questionLibrary);
        Integer lid = questionLibrary.getId();
        System.out.println(lid);
        //添加题库的题目信息
        if (questionIds != null && questionIds.length > 0) {
            for (Integer id : questionIds) {
                mapper.addQuestion(id, lid);
            }
        }
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String conditions = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<QuestionLibrary> pages = mapper.queryPage(conditions);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {

        return mapper.findById(id);
    }

    @Override
    public List<Integer> findQuestionById(Integer id) {
        return mapper.findQuestionById(id);
    }

    @Override
    public void update(Integer[] questionIds, QuestionLibrary questionLibrary) {
        //更新基本数据
        mapper.updateQuestionLibrary(questionLibrary);
        int lid = questionLibrary.getId();
        //首先清空掉关系表中的所有数据
        mapper.clearByLid(lid);
        //更新题目信息数据
        if (questionIds!=null && questionIds.length > 0) {
            for (Integer id : questionIds) {
                mapper.addQuestion(id, lid);
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        mapper.deleteById(id);
    }
}

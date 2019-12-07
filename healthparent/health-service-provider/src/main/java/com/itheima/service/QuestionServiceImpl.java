package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.QuestionMapper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = QuestionService.class)
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //查询总条数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Questions> questions = questionMapper.findPage(queryString);
        return new PageResult(questions.getTotal(), questions.getResult());

    }

    @Override
    public Questions findById(Integer id) {
        return questionMapper.findById(id);
    }

    @Override
    public void add(Questions questions) {
        questionMapper.add(questions);
    }

    @Override
    public void update(Questions questions) {
        questionMapper.update(questions);
    }

    @Override
    public void deleteById(Integer id) {
        questionMapper.deleteById(id);
    }

    @Override
    public List<Questions> getRandomQuestion() {
        //获取随机
        List<Questions> randomQuestion = questionMapper.getRandomQuestion();
        List<Questions> question = questionMapper.getQuestion();
        //去重
        question.removeAll(randomQuestion);
        //添加问题
        question.addAll(randomQuestion);
        //补齐剩余问题
        while(question.size() < 20) {
            List<Questions> limitQuestion = questionMapper.getLimitQuestion(20 - question.size());
            question.removeAll(limitQuestion);
            question.addAll(limitQuestion);
        }
        return question;
    }

    @Override
    public List<Questions> findAll() {
        return questionMapper.findAll();
    }

    @Override
    public Map getResultByTitle(String title) {
        return questionMapper.getResultByTitle(title);
    }
}

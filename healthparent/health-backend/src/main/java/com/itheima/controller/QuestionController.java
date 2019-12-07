package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Questions;
import com.itheima.service.QuestionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Reference
    private QuestionService questionService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            return questionService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Questions questions = questionService.findById(id);
            return new Result(true, "", questions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取问题信息失败");
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Questions questions) {
        try {
            questionService.add(questions);
            return new Result(true, "新增问题信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加问题信息失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Questions questions) {
        try {
            questionService.update(questions);
            return new Result(true, "修改问题信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改问题信息失败");
        }
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            questionService.deleteById(id);
            return new Result(true, "删除问题信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , "删除问题信息失败");
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Questions> questionsList = questionService.findAll();
            return new Result(true, "", questionsList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询题目信息失败");
        }
    }
}

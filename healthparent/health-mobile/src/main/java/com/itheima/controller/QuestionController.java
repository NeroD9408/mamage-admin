package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.Questions;
import com.itheima.service.QuestionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Reference
    private QuestionService questionService;

    @RequestMapping("/getRandomQuestion")
    public Result getRandomQuestion() {
        try {
            List<Questions> questionList =  questionService.getRandomQuestion();
            return new Result(true, "", questionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取问题列表失败");
        }
    }

    @RequestMapping("/getResult")
    public Result getResult(@RequestBody Map param) {
        String memberType = null;
        try {
            //根据前台传回的数据来判断该用户的体质类型
            Integer easyScore = (Integer) param.get("easyScore");
            Integer hardScore = (Integer) param.get("hardScore");
            String maxScoreType = (String) param.get("maxScoreType");
            if ((easyScore >= 60 && hardScore < 30) || (easyScore >= 40 && hardScore < 40)) {
                memberType = "平和质";
            } else {
                memberType = maxScoreType;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Result(false, "当前测试人数过多，请稍后再试");
        }
        return new Result(true, "", memberType);
    }

    @RequestMapping("/getResultByTitle")
    public Result getResultByTitle(String result) {
        try {
           String title =  new String(result.getBytes("ISO8859-1"),"UTF-8");
            Map map = questionService.getResultByTitle(title);
            return new Result(true, "", map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "服务器正忙");
        }

    }

}

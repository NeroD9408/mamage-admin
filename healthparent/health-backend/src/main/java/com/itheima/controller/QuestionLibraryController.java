package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.QuestionLibrary;
import com.itheima.service.QuestionLibraryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questionLibrary")
public class QuestionLibraryController {

    @Reference
    private QuestionLibraryService questionLibraryService;

    //新增检查组
    @RequestMapping("/add")
    public Result add(Integer[] questionIds, @RequestBody QuestionLibrary questionLibrary){
        try {
            questionLibraryService.add(questionIds,questionLibrary);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult queryPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=questionLibraryService.queryPage(queryPageBean);
        return pageResult;
    }

    //根据id查询检查组
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = questionLibraryService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //根据检查组id 查询所有对应的检查项
    @RequestMapping("/findQuestionsById")
    public List<Integer> findQuestionsById(Integer id){
        List<Integer> list=questionLibraryService.findQuestionById(id);
        return list;
    }

    //编辑检查组
    @RequestMapping("/update")
    public Result update(Integer[] questionIds,@RequestBody QuestionLibrary questionLibrary){
        try {
            questionLibraryService.update(questionIds,questionLibrary);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    //删除检查组
//    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            questionLibraryService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    //新增检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/add")
    public Result add(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.add(checkitemIds,checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    //分页查询
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findPage")
    public PageResult queryPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=checkGroupService.queryPage(queryPageBean);
        return pageResult;
    }

    //根据id查询检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //根据检查组id 查询所有对应的检查项
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findCheckItemsById")
    public List<Integer> findCheckItemsById(Integer id){
        List<Integer> list=checkGroupService.findCheckItemsById(id);
        return list;
    }

    //编辑检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    @RequestMapping("/update")
    public Result update(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.update(checkitemIds,checkGroup);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    //删除检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            checkGroupService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //这个方法是新增检查套餐的时候需要展示所有的检查组信息,才有的
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList=checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //客户再看到提示之后继续点击删除之后执行
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/continueDelete")
    public Result continueDelete(Integer id){
        try {
            checkGroupService.continueDelete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}

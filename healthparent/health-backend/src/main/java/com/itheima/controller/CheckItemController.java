package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.apache.zookeeper.Op;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    //新增检查项
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //分页查询
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult=checkItemService.queryPage(queryPageBean);
        return pageResult;
    }

    //删除检查项
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //根据id查询检查项
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem=checkItemService.findById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

    }

    //编辑检查项
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.update(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //查询所有的检查项
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckItem> checkItemList=checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //在明知道检查项关联着其他检查组还是继续点击删除之后执行该方法
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/continueDelete")
    public Result continueDelete(Integer id){
        try {
            checkItemService.continueDelete(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
}

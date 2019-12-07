package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Reference
    private SetMealService setMealService;

    //客户点击首页的点击预约的时候,需要把所有的套餐都展示给客户,这里是查询所有套餐
    @RequestMapping("/getAllSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> list=setMealService.getAllSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //这个是客户点击了某一个套餐的时候,需要把该套餐的所有信息以及下面的检查组以及检查项都展示给客户的方法
    @RequestMapping("/findSetmealById")
    public Result findSetmealById(Integer id){
        try {
            Setmeal setmeal=setMealService.findSetmealById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}

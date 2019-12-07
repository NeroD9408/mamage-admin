package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageBean;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;
import com.itheima.service.OrderSettingListService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSettingList")
public class OrderSettingListController {

    @Reference
    private OrderSettingListService service;
    @RequestMapping("/queryPage")
    public PageResult queryPage(@RequestBody PageBean pageBean) throws Exception{
        return service.getOrderSettingList(pageBean);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Map map, Integer[] checkSetMealIds){
        Result result = null;
        try {
            result = service.add(map,checkSetMealIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ORDERSETTING_FAIL);
        }
        return result;
    }

    @RequestMapping("/delete")
    public Result delete(String orderDate, String phoneNumber, String code){
        try {
            service.delete(orderDate,phoneNumber,code);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETTINGLIST_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETTINGLIST_SUCCESS);
    }

    @RequestMapping("/changeStatus")
    public Result changeStatus(@RequestBody Map map) throws Exception{
           return service.changeStatus(map);
    }

    @RequestMapping("/backMessage")
    public Result backMessage(Integer id){
        Map<String,Object> map = service.backMessage(id);
        return new Result(true,null,map);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Map map, Integer checkSetMealId){
        try {
            service.update(map,checkSetMealId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEALLIST_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEALLIST_SUCCESS);
    }

    @RequestMapping("/getAddress")
    public Result getAddress(Integer setMealId){
        List<Address> addressList = null;
        try {
            addressList = service.getAddress(setMealId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ADDRESS_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_ADDRESS_SUCCESS,addressList);
    }

    @RequestMapping("/getAddressByOrderId")
    public Result getAddressByOrderId(Integer orderId){
        List<Address> addressList = null;
        try {
            addressList = service.getAddressByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ADDRESS_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_ADDRESS_SUCCESS,addressList);
    }
}

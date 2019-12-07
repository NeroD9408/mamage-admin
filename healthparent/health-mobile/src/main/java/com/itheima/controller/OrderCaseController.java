package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderCaseController {
    @Reference
    private OrderService orderService;
    //根据预约id查询用户预约信息详情
    @RequestMapping("/findByOrderId")
    public Result findByOrderId(String id){
        System.out.println(id);
        try {
            Map<String,Object> map = orderService.findByOrderId(id);
            Date date1 = (Date) map.get("orderDate");
            String date = new SimpleDateFormat("yyyy-MM-dd").format(date1);
            map.put("orderDate",date);
            return new Result(true, "",map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"");
    }
}

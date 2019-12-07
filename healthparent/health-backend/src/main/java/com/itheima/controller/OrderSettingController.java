package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    //下载excel模板
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> dataList=new ArrayList<>();
            for (String[] string : list) {
                String orderDate=string[0];
                String number=string[1];
                OrderSetting orderSetting=new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                dataList.add(orderSetting);
            }
            orderSettingService.add(dataList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    //根据月份查询当月的所有的可预约数量的设置,数据最后被渲染展示在日历上面
    @RequestMapping("/findDataByMonth")
    public Result findDataByMonth(String date){
        try {
            List<Map> list=orderSettingService.findDataByMonth(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    //根据某一天的日期设置可预约数量
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/setNumberByDate")
    public Result setNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.setNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}

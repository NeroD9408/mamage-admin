package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private ReportService reportService;
    @RequestMapping("/getReport")
    public Result getReport(Integer id){
        try {
            List<Map<String, Object>> list = reportService.getReportByOrderId(id);
            return new Result(true,null,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查看报告失败!");
        }
    }
    @RequestMapping("/getOrderUser")
    public Result getOrderUser(Integer id){
        try {
            Map<String,Object> map = reportService.getOrderUser(id);
            return new Result(true,null,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查看报告人信息失败!");
        }

    }
}

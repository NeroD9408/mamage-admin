package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.service.InterventionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/intervention")
public class InterventionController {
    @Reference
    private InterventionService interventionService;

    @RequestMapping("/findAMapById")
    public Result findAMapById(Integer id){
        try {
            Map<String,Object>map=interventionService.findAMapById(id);
            return new Result(true,"",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"");
        }
    }
}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Food;
import com.itheima.service.FoodService;
import com.itheima.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/food")
public class FoodController {
    @Reference
    private FoodService foodService;

    //下载excel模板
    @PreAuthorize("hasAuthority('FOOD_UPLOAD')")
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<Food> dataList=new ArrayList<>();
            for (String[] string : list) {
                String id=string[0];
                String type=string[1];
                String name=string[2];
                String kj=string[8];
                String protein=string[9];
                String fat=string[10];
                String unit=string[22];
                Food food=new Food(Integer.parseInt(id),Integer.parseInt(type),name,Integer.parseInt(kj),Float.parseFloat(protein),Float.parseFloat(fat),unit);
                dataList.add(food);
            }
            foodService.add(dataList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
    //分页查询
    @PreAuthorize("hasAuthority('FOOD_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult=foodService.queryPage(queryPageBean);
        return pageResult;
    }
    //根据id查询运动项
    @PreAuthorize("hasAuthority('FOOD_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Food food=foodService.findById(id);
            return new Result(true,MessageConstant.QUERY_FOOD_SUCCESS,food);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_FOOD_FAIL);
        }
    }
    //添加运动项
    @PreAuthorize("hasAuthority('FOOD_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Food food){

            try {
                foodService.addFood(food);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.ADD_FOOD_FAIL);
            }
            return new Result(true,MessageConstant.ADD_FOOD_SUCCESS);
        }

    //删除运动项
    @PreAuthorize("hasAuthority('FOOD_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            foodService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_FOOD_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_FOOD_SUCCESS);
    }
    //更新运动项
    @PreAuthorize("hasAuthority('FOOD_UPDATE')")
    @RequestMapping("/update")
    public Result update(@RequestBody Food food) {
        try {
            foodService.update(food);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_FOOD_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_FOOD_SUCCESS);
    }
}

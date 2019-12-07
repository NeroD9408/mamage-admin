package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Sport;
import com.itheima.service.SportService;
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
@RequestMapping("/sport")
public class SportController {
    @Reference
    private SportService sportService;

    //112121
    //下载excel模板
    @PreAuthorize("hasAuthority('SPORT_UPLOAD')")
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
//        System.out.println(excelFile);
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<Sport> dataList=new ArrayList<>();
            for (String[] string : list) {
                String id=string[0];
                String number=string[1];
                String name=string[2];
                String type=string[4];
                String time=string[5];
                String frequency=string[6];
                String position=string[14];
                String method=string[15];
                Sport sport=new Sport(Integer.parseInt(id),number,name,Integer.parseInt(type),time,frequency,position,method);
                dataList.add(sport);
            }
            sportService.add(dataList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
    //分页查询
    @PreAuthorize("hasAuthority('SPORT_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult=sportService.queryPage(queryPageBean);
        return pageResult;
    }
    //根据id查询运动项
    @PreAuthorize("hasAuthority('SPORT_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Sport sport=sportService.findById(id);
            return new Result(true,MessageConstant.QUERY_SPORT_SUCCESS,sport);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SPORT_FAIL);
        }
    }
    //添加运动项
    @PreAuthorize("hasAuthority('SPORT_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Sport sport){
        String number=sport.getNumber();
        Boolean flag=sportService.check(number);
        if(flag){
            try {
                sportService.addSport(sport);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.ADD_SPORT_FAIL);
            }
            return new Result(true,MessageConstant.ADD_SPORT_SUCCESS);
        }else {
            return new Result(false,"助记码重复,请修改后添加");
        }
    }
    //删除运动项
    @PreAuthorize("hasAuthority('SPORT_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            sportService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SPORT_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_SPORT_SUCCESS);
    }
    //更新运动项
    @PreAuthorize("hasAuthority('SPORT_UPDATE')")
    @RequestMapping("/update")
    public Result update(@RequestBody Sport sport) {
        try {
            sportService.update(sport);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SPORT_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_SPORT_SUCCESS);
    }
}

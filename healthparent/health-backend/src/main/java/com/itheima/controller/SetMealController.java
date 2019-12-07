package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetMealService setMealService;

    //在新增套餐的时候,需要上传一张图片,这里就是上传图片用的
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        //System.out.println(imgFile);
        String originalFilename = imgFile.getOriginalFilename();
        //originalFilename,就是这样的形式,原始文件名03a36073-a140-4942-9b9b-712cecb144901.jpg
        int index = originalFilename.lastIndexOf(".");
        String fileName_last = originalFilename.substring(index - 1);
        String fileName= UUID.randomUUID().toString()+fileName_last;
        //System.out.println(fileName);
        /*String name = imgFile.getName();
        String path = SetMealController.class.getClassLoader().getResource(name).getPath();*/
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //图片上传成功之后把图片名称存储到redis里面,方便后续清理垃圾图片
            if (fileName!=null){
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            }
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    //新增套餐
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=setMealService.findPage(queryPageBean);
        return pageResult;
    }

    //编辑套餐的时候信息回显,需要根据id查出当前的套餐基础信息
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal=setMealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //编辑套餐的时候还需要根据id查出findcheckgroupIds的数据,用list集合去装吧,然后返回前台
    @RequestMapping("/findcheckgroupIds")
    public List<Integer> findcheckgroupIds(Integer id){
        List<Integer> list=setMealService.findcheckgroupIds(id);
        return list;
    }

    //接收修改过后的前台页面参数,去进行最终的修改
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setMealService.update(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //删除套餐
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            setMealService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    //在创建新的机构地址的时候,需要先显示所有的套餐信息供选择,所以需要先查询出所有的套餐返回
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Setmeal> list=setMealService.findAll();
            return new Result(true,"",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/getCheckIds")
    public Result getCheckIds(Integer orderId){
        List<Integer> list = setMealService.getCheckIds(orderId);
        return new Result(true,null,list);
    }
}

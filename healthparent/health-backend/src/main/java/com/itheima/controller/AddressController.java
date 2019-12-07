package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;
import com.itheima.service.AddressService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;

    @Autowired
    private JedisPool jedisPool;

    //查询所有的体检机构位置并分页展示
    @PreAuthorize("hasAuthority('ADDRESS_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            PageResult pageResult=addressService.findPage(queryPageBean);
            return pageResult;
    }

    //客户在新增地址或者编辑地址的时候,如果要上传或者更换图片,首先走的就是这一步,先把图片存到七牛云
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.indexOf(".");
        String substring = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID().toString() + substring;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            if (fileName!=null){
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            }
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    //客户在填写好新增地址页面之后点击了确定按钮后,执行这个方法,也就是添加客户提交的信息到数据库
    @PreAuthorize("hasAuthority('ADDRESS_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Address address, Integer[] setmealIds){
        try {
            addressService.add(address,setmealIds);
            //如果保存到数据库没问题的话,那把这个数据里面的图片img取出来保存到redis里面,为了方便后续定期清除垃圾
            String img = address.getImg();
            if (img!=null){
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
            }
            return new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
        }
    }



    //当客户点击编辑按钮之后,弹出编辑框,首先要进行被点击地址的信息回显,通过id去数据库查询
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Address address=addressService.findById(id);
            return new Result(true, MessageConstant.GET_ADDRESS_LIST_SUCCESS,address);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ADDRESS_LIST_FAIL);
        }
    }

    //根据id查询出所有的套餐对应的id,让套餐也能回显
    @RequestMapping("/findSetmealIds")
    public List<Integer> findSetmealIds(Integer id){
        List<Integer> list=addressService.findSetmealIds(id);
        return list;
    }


    //当客户填写好编辑框的信息之后,点击可确定,执行该方法,接收客户提交的新信息,去更新数据库
    @PreAuthorize("hasAuthority('ADDRESS_EDIT')")
    @RequestMapping("/update")
    public Result update(@RequestBody Address address, Integer[] setmealIds){
        try {
            addressService.update(address,setmealIds);
            //如果跟新成功,还得把新获取到图片名称保存到redis里面,方便后续清理垃圾使用
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,address.getImg());
            return new Result(true, MessageConstant.EDIT_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ADDRESS_FAIL);
        }
    }

    //在第一次点击删除按钮的时候,并且点击了确认,执行此方法
    @PreAuthorize("hasAuthority('ADDRESS_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ADDRESS_FAIL);
        }
    }

    //在第一次删除的时候,后台没删掉,提示有订单关联,但是用户在看到提示后,任然点击确认,就会执行此方法
    //此方法代表会把该id的机构运营状态该为暂停运营,类似商品下架,客户端将无法再看到该机构信息
    @PreAuthorize("hasAuthority('ADDRESS_EDIT')")
    @RequestMapping("/editAddressStatusById")
    public Result editAddressStatusById(Integer id){
        try {
            //先去数据库查一下这个机构的运营状态
            String status=addressService.findStatusById(id);
            if (status.equals("暂停运营")){
                return new Result(false,"该机构已是暂停运营状态,无需再次更改");
            }
            addressService.editAddressStatusById(id);
            return new Result(true, MessageConstant.EDIT_ADDRESS_STATUS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ADDRESS_STATUS_FAIL);
        }
    }



    //这是为了在添加地址的时候发送一个异步请求,写的方法,只是为了让异步请求成功,然后页面局部刷新,无法写具体内容
    @RequestMapping("/getLocation")
    public Result getLocation(){
        return new Result(true,"");
    }

  /*  根据地址的id查询出地图需要回显的信息,是一个集合里面装一个map.这个地图回显页面格式变乱,
    前台页面太难搞,考虑医院也不会经常搬迁,所以就不在地图上回显了,管理员真的想更换医院位置的话直接
    在地图上点新的地址就可以了,后台会动态获取新的地址坐标,可以保存*/
  /*  @RequestMapping("/findAddressDataById")
    public Result findAddressDataById(Integer id){
        List<Map> list=new ArrayList<>();
        Map<String,Object> map=addressService.findAddressDataById(id);
        list.add(map);
        return new Result(true, MessageConstant.GET_ADDRESS_LIST_SUCCESS,list);
    }*/


}

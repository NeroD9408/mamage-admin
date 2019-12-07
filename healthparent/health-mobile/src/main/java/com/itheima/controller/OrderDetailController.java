package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.OrderDetailService;
import com.itheima.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/*
*  个人基本信息
* */
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderDetailService orderDetailService;

    @RequestMapping("/getOrderDetail")
    public Result getOrderDetail(HttpServletRequest request) throws Exception {

        Cookie[] cookies = request.getCookies();

        String json = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("login_member_telephone")){
                String telephone1 = cookie.getValue();
                String telephone= RSAUtils.decrypt(telephone1);
                System.out.println("解密后"+telephone);
                json = jedisPool.getResource().get(telephone);
                break;
            }
        }
        if(json !=null){

            Member member = JSONObject.parseObject(json, Member.class);
            return new Result(true, "", member);
        }
        return new Result(false,"");
    }

    //修改个人信息
    @RequestMapping("/update")
    public Result update(@RequestBody Member member,HttpServletRequest request,HttpServletResponse response){

        try {
            orderDetailService.update(member);
            String phoneNumber = member.getPhoneNumber();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("login_member_telephone")){
                    String phoneNumber1 = RSAUtils.encrypt(phoneNumber);
                    Cookie cookie1 = new Cookie("login_member_telephone", phoneNumber1);
                    cookie1.setPath("/");
                    cookie1.setMaxAge(60*60*24*30);
                    response.addCookie(cookie1);


                    //将会员信息存到redis,时间30分钟
                    String json = JSON.toJSON(member).toString();
                    jedisPool.getResource().setex(phoneNumber,60*30,json);

                }
            }

            return  new Result(true, MessageConstant.UPDATE_INFODETAIL_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPDATE_INFODETAIL_FAIL);
        }
    }


    //修改密码
    @RequestMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map map){
        String  phoneNumber = (String)map.get("phoneNumber");
        String  password1 = (String)map.get("password1");
        String  password2 = (String)map.get("password2");



        //根据手机号码查询密码
        String password = orderDetailService.findPasswordByPhoneNum(phoneNumber);
        if(password !=null && password.length()>0 && password.equals(password1)){

            Member member = new Member();
            member.setPassword(password2);
            member.setPhoneNumber(phoneNumber);
            orderDetailService.updatePassword(member);
            return new Result(true,"");
        }else {
            return  new Result(false,"");
        }
    }
}

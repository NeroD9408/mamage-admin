package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberOrderService;
import com.itheima.service.MemberService;
import com.itheima.utils.RSAUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/memberOrder")
public class MemberOrderController {

    @Reference
    private MemberOrderService memberOrderService;

    //根据日期查询预约记录
    @RequestMapping("/findOrdersByDate")
    public Result findOrdersByDate(@RequestBody Date[] value2, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String start = new SimpleDateFormat("yyyy-MM-dd").format(value2[0]);
        String end = new SimpleDateFormat("yyyy-MM-dd").format(value2[1]);
        //从cookie中获取当前登录用户手机号
        Cookie[] cookies = request.getCookies();
        String telephone = "";
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("login_member_telephone")){
                telephone = cookie.getValue();
                System.out.println(telephone);
                telephone= RSAUtils.decrypt(telephone);
                System.out.println("解密后"+telephone);
                }
            }
        }
        //根据手机号查询用户预约信息
        try {

            List<Map<String,Object>> list = memberOrderService.findByPhoneNumberAndDate(telephone,start,end);
            return new Result(true, MessageConstant.QUERY_MEMBER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MEMBER_FAIL);
        }
    }
}

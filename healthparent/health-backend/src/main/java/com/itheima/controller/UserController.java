package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    //这个是为了获取当前登录用户名,展示在主页面的右上角用的
    @RequestMapping("/findUsername")
    public Result findUsername(){
        //当security完成认证之后,会把当前用户信息存到框架提供的上下文对象中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user!=null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}

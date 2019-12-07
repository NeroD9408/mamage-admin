package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
//import com.itheima.utils.RSAUtils;
import com.itheima.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    //用手机验证码登录的功能2,这里是当客户收到验证码填写提交之后的处理,获取客户写的验证码,验证是否正确
    @RequestMapping("/login")
    public Result login(HttpServletResponse response,@RequestBody Map map) throws Exception {
        //获取客户提交的验证码
        String validateCode = (String) map.get("validateCode");
        System.out.println(validateCode);
        //获取之前保存在redis里面的验证码
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //判断验证码是否正确
        if (validateCode!=null && codeInRedis!=null && validateCode.equals(codeInRedis)){
            //验证码是正确的,然后看下是否是会员
            Member member=memberService.findByTelephone(telephone);
            if (member==null){
                member = new Member();
                //说明还不是会员,那就自动把已有的信息保存在会员表里
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }

            //会员信息也保存了,向客户端写cookie,内容是手机号
            String telephone1 = RSAUtils.encrypt(telephone);
            Cookie cookie=new Cookie("login_member_telephone",telephone1);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);

            //将会员信息存到redis,时间30分钟
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else{
            //说明验证码不对
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}

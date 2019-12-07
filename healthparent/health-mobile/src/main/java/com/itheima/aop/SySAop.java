package com.itheima.aop;

import com.alibaba.fastjson.JSON;
import com.itheima.utils.RSAUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Component
@Aspect
public class SySAop {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private HttpServletRequest request;

    //声明切入点表达式
    @Pointcut("execution(* com.itheima.controller.*.*(..))")
    public void pid(){}


    //前置
    @Before("pid()")
    public void before() throws Exception {

        Cookie[] cookies = request.getCookies();

        String json = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("login_member_telephone")){
                String telephone1 = cookie.getValue();
                String telephone= RSAUtils.decrypt(telephone1);
                System.out.println("解密后"+telephone);
                json = jedisPool.getResource().get(telephone);

                //将会员信息存到redis,时间30分钟
                jedisPool.getResource().setex(telephone,60*30,json);
            }
        }

    }
}

package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class validateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //客户进行预约之前先要通过手机获取验证码,这里就是获取验证码的
    @RequestMapping("/sendValidateCode")
    public Result sendValidateCode(String telephone){
        //第一步随机生成一个4位的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //第二步给客户发送这个验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //第三步,如果验证码顺利发送了,把验证码保留在redis里面,保留个5分钟吧
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }



    //这个是通过获取手机验证码登录1,这里是用来获取这个验证码的,然后保存到redis里面5分钟
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //第一步随机生成一个6位的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(validateCode);
        //第二步给客户发送这个验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //第三步,如果验证码顺利发送了,把验证码保留在redis里面,保留个5分钟吧
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}

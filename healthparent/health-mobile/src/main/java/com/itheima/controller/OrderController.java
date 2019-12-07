package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.service.MemberService;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.RSAUtils;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    //客户填写好了预约需要的信息之后,点击提交,走到这里,进行预约用的
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //获取之前存在redis里面的验证码,来和页面传递过来的做比较
        //如果验证码一致,则调用service保存预约数据
        String validateCode = (String) map.get("validateCode");

        String telephone = (String) map.get("telephone");
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (code!=null && validateCode!=null && code.equals(validateCode)){
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result=null;
            try {
                result=orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }//这下面的一步是指数据库也操作成功了,预约成功了,给客户发预约成功短信,但是能走到这一步说明已经成功了
            //老师搞个result.isFlag()不知道是什么意思?
            if (result.isFlag()){
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,(String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else{
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }

    //预约成功之后,需要显示一些基本信息给客户,这个是根据订单的id去数据库进行多表查询
    @RequestMapping("/findById")
    public Result findById(String id){
        try {
            Map<String, Object> map=orderService.findById(id);
            if (map!=null){
                Date orderDate = (Date) map.get("orderDate");
                map.put("orderDate", DateUtils.parseDate2String(orderDate));
            }
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
    //根据用户手机号码查询用户预约记录
    @RequestMapping("/findOrders")
    public Result findOrders(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            List<Map<String,Object>> list = memberService.findByPhoneNumber(telephone);
            return new Result(true,MessageConstant.QUERY_MEMBER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MEMBER_FAIL);
        }
    }
}

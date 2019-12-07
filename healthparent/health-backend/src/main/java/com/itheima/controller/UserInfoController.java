package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.pojo.UserInfo;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.UserInfoService;
import com.itheima.utils.HandleImgUtils;
import com.itheima.utils.QiniuHeadImgUtils;
import com.itheima.utils.UuidUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    private String loginUsername = "";

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private UserInfoService userInfoService;

    //查询当前登录用户的信息
    @RequestMapping("/findLoginUser")
    public Result findLoginUser() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            loginUsername = user.getUsername();
            //不需要加载角色
            UserInfo loginUser = userInfoService.findUserInfo(loginUsername);
            System.out.println(loginUser);
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    //修改头像
    @RequestMapping("/updateHeadImg")
    public Result updateHeadImg(String imgFile, HttpServletRequest request) {
        //获取存储的真实路径
        byte[] bytes = HandleImgUtils.handleImg(imgFile);
        String imgName = UuidUtil.getUuid() + ".jpg";
        //调用工具类将图片保存到云端
        //直接使用字符串获取bytes
        QiniuHeadImgUtils.upload2Qiniu(bytes, imgName);
        //修改当前登录账号的头像信息
        userInfoService.updateUserHead("http://q1bny4qv1.bkt.clouddn.com/" + imgName, loginUsername);
        return new Result(true, "成功");
    }

    //修改密码页面修改用户信息的方法
    @RequestMapping("/updateUser")
    public Result updateUser(UserInfo userInfo) {
        try {
            userInfoService.update(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //页面加载方法
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return userInfoService.findPage(queryPageBean);
    }

    @RequestMapping("/editUser")
    public Result editUser(@RequestBody UserInfo userInfo, Integer[] ids) {
        try {
            userInfoService.update(userInfo, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/addUser")
    public Result addUser(@RequestBody UserInfo userInfo, Integer[] ids) {
        try {
            userInfoService.add(userInfo, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //数据回显
    @RequestMapping("/findUserByUsername")
    public Result findUserByUsername(String username) {
        try {
            //需要加载角色
            UserInfo userInfo = userInfoService.findUserInfo(username);
            return new Result(true, "", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取用户信息失败");
        }
    }

    @RequestMapping("/deleteUser")
    public Result deleteUser(Integer id) {
        try {
            userInfoService.deleteUser(id);
            return new Result(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "该用户存在角色信息,禁止删除");
        }
    }
}

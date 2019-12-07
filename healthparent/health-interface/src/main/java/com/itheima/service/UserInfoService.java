package com.itheima.service;

import com.itheima.pojo.UserInfo;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

public interface UserInfoService {

    //根据用户名查询用户信息
    UserInfo findUserInfoByUsername(String username);

    //通过用户名密码确认用户信息
    UserInfo findUserInfo(String username) throws Exception;

    //修改登录用户的头像地址
    void updateUserHead(String headimg, String loginUsername);

    //修改登录用户的信息
    void update(UserInfo userInfo);

    //修改用户信息
    void update(UserInfo userInfo, Integer[] ids) throws Exception;

    PageResult findPage(QueryPageBean queryPageBean);

    void add(UserInfo userInfo, Integer[] ids);

    void deleteUser(Integer id);
}

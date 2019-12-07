package com.itheima.dao;

import com.itheima.pojo.Member;

public interface OrderDetailDao {
    //修改个人信息
    public void update(Member member);

    //根据手机号查找密码
    public String findPasswordByPhoneNum(String phoneNumber);

    //修改密码
    public void updatePassword(Member member);
}

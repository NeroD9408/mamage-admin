package com.itheima.service;

import com.itheima.pojo.Member;

public interface OrderDetailService {
    public void update(Member member);

    public String findPasswordByPhoneNum(String phoneNumber);

    public void updatePassword(Member member);
}

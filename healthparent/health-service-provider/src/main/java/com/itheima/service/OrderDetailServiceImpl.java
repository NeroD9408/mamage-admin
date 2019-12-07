package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderDetailDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass =  OrderDetailService.class)
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDao orderDetailDao;
    public void update(Member member) {
        orderDetailDao.update(member);
    }


    public String findPasswordByPhoneNum(String phoneNumber) {
        return orderDetailDao.findPasswordByPhoneNum(phoneNumber);
    }


    public void updatePassword(Member member) {
        orderDetailDao.updatePassword(member);
    }
}

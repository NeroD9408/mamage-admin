package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Result order(Map map) throws Exception;

    Map<String, Object> findById(String id);

    Map<String,Object> findByOrderId(String id);

    List<Order> findOrderByMemberId(Integer id);

}

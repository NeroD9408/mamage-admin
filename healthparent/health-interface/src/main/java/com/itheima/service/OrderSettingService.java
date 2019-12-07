package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> dataList);

    List<Map> findDataByMonth(String orderDate);

    void setNumberByDate(OrderSetting orderSetting);
}

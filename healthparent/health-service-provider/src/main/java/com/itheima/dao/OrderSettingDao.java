package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    long findCountByDate(Date orderDate);

    void editNumber(OrderSetting orderSetting);

    void addNumber(OrderSetting orderSetting);

    List<OrderSetting> findDataByMonth(Map map);
    //预约时先查询当天是否已设置过预约
    OrderSetting findByDate(Date date);

    void editReservationsByDate(OrderSetting orderSetting);
}

package com.itheima.service;

import com.itheima.entity.PageBean;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;

import java.util.List;
import java.util.Map;

public interface OrderSettingListService {

    PageResult getOrderSettingList(PageBean pageBean) throws Exception;

    Result add(Map map, Integer[] checkSetMealIds) throws Exception;

    void delete(String orderDate, String phoneNumber, String code);

    Result changeStatus(Map map) throws Exception;

    Map<String, Object> backMessage(Integer id);

    void update(Map map, Integer checkSetMealId) throws Exception;

    List<Address> getAddress(Integer setMealId);

    List<Address> getAddressByOrderId(Integer orderId);
}

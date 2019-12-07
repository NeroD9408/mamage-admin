package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(Address address, Integer[] setmealIds);

    Address findById(Integer id);

    void update(Address address, Integer[] setmealIds);

    void deleteById(Integer id);

    void editAddressStatusById(Integer id);

    List<Integer> findSetmealIds(Integer id);

    String findStatusById(Integer id);

    List<Address> findAllAddressBySetmealId(Integer id);

    Map<String,Object> findAddressDataById(Integer id);
}

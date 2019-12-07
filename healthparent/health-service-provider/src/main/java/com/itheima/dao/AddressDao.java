package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Address;

import java.util.List;
import java.util.Map;

public interface AddressDao {
    Page<Address> findByCondition(String conditions);

    void add(Address address);

    Address findById(Integer id);

    void update(Address address);

    void deleteById(Integer id);

    void editAddressStatusById(Integer id);

    void addReference(Map<String, Integer> map);

    List<Integer> findSetmealIds(Integer id);

    void deleteReference(Integer addressId);

    String findStatusById(Integer id);

    List<Address> findAllAddressBySetmealId(Integer id);


    List<Address> getAddress(Integer setMealId);

    List<Address> getAddressByOrderId(Integer orderId);

    Integer findIdByName(String addressName);
}

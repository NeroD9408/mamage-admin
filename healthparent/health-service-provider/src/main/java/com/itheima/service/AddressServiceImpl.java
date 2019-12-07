package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.AddressDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String conditions = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Address> page = addressDao.findByCondition(conditions);
        long total = page.getTotal();
        List<Address> rows = page.getResult();
        return new PageResult(total, rows);
    }

    @Override
    public void add(Address address, Integer[] setmealIds) {
        addressDao.add(address);
        Integer addressId = address.getId();
        if (setmealIds!=null && setmealIds.length>0){
            for (Integer setmealId : setmealIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("addressId",addressId);
                map.put("setmealId",setmealId);
                addressDao.addReference(map);
            }
        }
    }

    @Override
    public Address findById(Integer id) {
        return addressDao.findById(id);
    }

    @Override
    public void update(Address address, Integer[] setmealIds) {

        addressDao.update(address);

        //然后根据id删除所有的关联
        Integer addressId = address.getId();
        addressDao.deleteReference(addressId);

        //重新绑定外界关联
        if (setmealIds!=null && setmealIds.length>0){
            for (Integer setmealId : setmealIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("addressId",addressId);
                map.put("setmealId",setmealId);
                addressDao.addReference(map);
            }
        }

    }

    @Override
    public void deleteById(Integer id) {
        //先去中间表把跟该机构有关的套餐清空,套餐只是医院的一部分,无需保留,也无需提醒,只要没有订单跟该机构关联,就允许删除
        addressDao.deleteReference(id);
        //直接根据地址的id去删除,如果该地址没有任何订单关联自然删除成功,一旦有订单关联系统会抛sql异常,会删除失败
        addressDao.deleteById(id);
    }

    @Override
    public void editAddressStatusById(Integer id) {
        addressDao.editAddressStatusById(id);
    }

    @Override
    public List<Integer> findSetmealIds(Integer id) {
        return addressDao.findSetmealIds(id);
    }

    @Override
    public String findStatusById(Integer id) {
        return addressDao.findStatusById(id);
    }

    @Override
    public List<Address> findAllAddressBySetmealId(Integer id) {

        return addressDao.findAllAddressBySetmealId(id);
    }

    @Override
    public Map<String, Object> findAddressDataById(Integer id) {
        return null;
    }
}

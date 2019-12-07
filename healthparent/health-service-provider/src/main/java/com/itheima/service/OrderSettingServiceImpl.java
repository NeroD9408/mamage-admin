package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> dataList) {
        for (OrderSetting orderSetting : dataList) {
            //先要判断这个日期的可预约人数是否已经被设置过了
            long count=orderSettingDao.findCountByDate(orderSetting.getOrderDate());
            if (count>0){
                //说明之前有人已经设置过了,那本次上传的数据我们不再中心往数据库插入,而是修改这个日期的可预约人数
                orderSettingDao.editNumber(orderSetting);
            }else{
                //说明之前没有人设置过这个日期的可预约人数,那我们就第一次设置一下
                orderSettingDao.addNumber(orderSetting);
            }
        }
    }

    @Override
    public List<Map> findDataByMonth(String date) {
        //查询语句select * from t_ordersetting where orderDate BETWEEN #{begin} and #{end}
        String begin=date+"-1";
        String end=date+"-31";
        Map<String,String> map=new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        List<OrderSetting> list=orderSettingDao.findDataByMonth(map);
        List<Map> resultList=new ArrayList<>();
        //{ date: 1, number: 120, reservations: 1 }
        for (OrderSetting orderSetting : list) {
            Map<String,Object> m=new HashMap<>();
            m.put("date",orderSetting.getOrderDate().getDate());
            m.put("number",orderSetting.getNumber());
            m.put("reservations",orderSetting.getReservations());
            resultList.add(m);
        }
        return resultList;
    }

    @Override
    public void setNumberByDate(OrderSetting orderSetting) {
        //也是分2步,第一步先判断这个日期是否已经设置过了

        long count = orderSettingDao.findCountByDate(orderSetting.getOrderDate());
        if (count>0){
            //说明已经设置过了,那就更新
            orderSettingDao.editNumber(orderSetting);
        }else{
            //说明是第一次设置
            orderSettingDao.addNumber(orderSetting);
        }
    }
}

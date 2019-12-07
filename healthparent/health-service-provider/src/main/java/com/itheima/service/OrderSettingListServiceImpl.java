package com.itheima.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.AddressDao;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageBean;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingListService.class)
@Transactional
public class OrderSettingListServiceImpl implements OrderSettingListService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private AddressDao addressDao;
    //分页查询(条件查询分页)
    @Override
    public PageResult getOrderSettingList(PageBean pageBean) throws Exception {
        Integer currentPage = pageBean.getCurrentPage();
        Integer pageSize = pageBean.getPageSize();
        String[] dataRange = pageBean.getDateRange();
        String queryString = pageBean.getQueryString();
        String status = pageBean.getStatus();
        String type = pageBean.getType();
        PageHelper.startPage(currentPage,pageSize);
        //有条件的情况
        String dateOne = null;
        String dateTwo = null;
        if (dataRange !=null && dataRange.length == 1){
            dateOne = dataRange[0];
        }
        if (dataRange !=null && dataRange.length ==2){
            dateOne = dataRange[0];
            dateTwo = dataRange[1];
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateOne",dateOne);
        map.put("dateTwo",dateTwo);
        map.put("queryString",queryString);
        map.put("status",status);
        map.put("type",type);
        Page<Map<String, Object>> orderSettingList = orderDao.getOrderByCondition(map);
        for (Map<String, Object> stringObjectMap : orderSettingList) {
            Date date = (Date) stringObjectMap.get("orderDate");
            String orderStatusStr = (String) stringObjectMap.get("orderStatus");
            Boolean orderStatus = false;
            if (orderStatusStr.equals("已到诊")){
                orderStatus = true;
            }
            String orderDate = DateUtils.parseDate2String(date);
            stringObjectMap.put("orderStatus",orderStatus);
            stringObjectMap.put("orderDate",orderDate);
        }
        long total = orderSettingList.getTotal();
        return new PageResult(total,orderSettingList);
    }

    //添加预约信息
    @Override
    public Result add(Map map, Integer[] checkSetMealIds) throws Exception {
        String phoneNumber = (String) map.get("phoneNumber");
        String sex = (String) map.get("sex");
        String name = (String) map.get("userName");
        String orderTypeBefore = (String) map.get("orderType");
        String orderType = "微信预约";
        if (orderTypeBefore.equals("2")){
            orderType = "电话预约";
        }
        String orderDate = (String) map.get("orderDate");
        Integer address = (Integer) map.get("address");
        String idCard = (String) map.get("idCard");
        String age = (String) map.get("age");
        Member member = memberDao.findByTelephone(phoneNumber);
        if (member == null){
            Member newMember = new Member();
            newMember.setPhoneNumber(phoneNumber);
            String substring = phoneNumber.substring(7);
            String s = DateUtils.parseDate2String(new Date());
            String replace = s.replace("-", "");
            String fileNumber = replace + substring;
            newMember.setFileNumber(fileNumber);
            newMember.setRegTime(DateUtils.parseString2Date(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
            newMember.setSex(sex);
            newMember.setName(name);
            memberDao.add(newMember);
            member = memberDao.findByTelephone(phoneNumber);
        }
        Integer memberId = member.getId();
        Order order = new Order();
        order.setPatientsName(name);
        order.setIdCard(idCard);
        order.setSex(sex);
        order.setAge(Integer.parseInt(age));
        order.setAddressId(address);
        order.setMemberId(memberId);
        order.setOrderType(orderType);
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        Map<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("date", DateUtils.parseString2Date(orderDate));
        conditionMap.put("memberId",memberId);
        for (Integer checkSetMealId : checkSetMealIds) {
            order.setSetmealId(checkSetMealId);
            conditionMap.put("checkSetMealId",checkSetMealId);
            Order orderExist = orderDao.findByMemberAndSetMealId(conditionMap);
            if (orderExist != null){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
            orderDao.addOrder(order);
        }
        return new Result(true, MessageConstant.ADD_ORDERSETTING_SUCCESS);
    }

    //删除预约信息
    @Override
    public void delete(String orderDate, String phoneNumber, String code) {
        Member member = memberDao.findByTelephone(phoneNumber);
        Integer setMealId = setMealDao.findIdByCode(code);
        Map<String,Object> map = new HashMap<>();
        map.put("memberId",member.getId());
        map.put("setMealId",setMealId);
        map.put("orderDate",orderDate);
        orderDao.delete(map);
    }

    //改变变是否到诊的状态
    @Override
    public Result changeStatus(Map map) throws Exception {
        Integer orderId = (Integer) map.get("orderId");
        Map<String,Object> mapCheckCondition = new HashMap<>();
        mapCheckCondition.put("orderId",orderId);
        mapCheckCondition.put("orderDate",DateUtils.parseDate2String(new Date()));
        Order checkOrder = orderDao.checkDate(mapCheckCondition);
        if (checkOrder == null){
            return new Result(false,"此订单的日期还没到哦,不能进行此操作");
        }
        Boolean orderStatusBoolean = (Boolean) map.get("orderStatus");
        String orderStatus = "未到诊";
        if (orderStatusBoolean){
            orderStatus = "已到诊";
            memberDao.addReportStatus(orderId);
        }
        Map<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("orderId",orderId);
        conditionMap.put("orderStatus",orderStatus);
        orderDao.changeStatus(conditionMap);
        return new Result(true,"更改成功!");
    }


    //编辑回显数据
    @Override
    public Map<String, Object> backMessage(Integer id) {
        return orderDao.backMessage(id);
    }

    //更新预约信息
    @Override
    public void update(Map map, Integer checkSetMealId) throws Exception {
        String orderDate = (String) map.get("orderDate");
        map.put("orderDate", DateUtils.parseString2Date(orderDate));
        map.put("checkSetMealId",checkSetMealId);
        orderDao.update(map);
        memberDao.updateMember(map);
    }

    //添加时根据套餐id获取机构信息
    @Override
    public List<Address> getAddress(Integer setMealId) {
        return addressDao.getAddress(setMealId);
    }

    //编辑时根据orderId  查询机构信息
    @Override
    public List<Address> getAddressByOrderId(Integer orderId) {
        return addressDao.getAddressByOrderId(orderId);
    }
}

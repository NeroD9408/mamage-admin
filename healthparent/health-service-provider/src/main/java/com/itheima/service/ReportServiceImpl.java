package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.*;
import com.itheima.pojo.Member;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ReportMapper reprotMapper;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String, Object> dataMap = new HashMap<>();
        //先获取报告时间reportDate
        //这个今日的时间也可以用工具类去获取,一开始我没看视频不知道,就自己写了,
        // 工具类用这个方法获取就可以了String today = DateUtils.parseDate2String(new Date());
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dataMap.put("reportDate",today);

        //获取todayNewMember今日会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        dataMap.put("todayNewMember",todayNewMember);

        //获取会员总数
        Integer totalMember = memberDao.findMemberTotalCount();
        dataMap.put("totalMember",totalMember);

        //获取本周的会员数 thisWeekNewMember,用工具类获取本周一的时间,然后再用工具类把这个时间转化成string
        String  monday=null;
        try {
            monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
            dataMap.put("thisWeekNewMember",thisWeekNewMember);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取本月的会员数thisMonthNewMember
        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        String begin=date+"-1";
        String end=date+"-31";
        Map<String,Object>map=new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        Integer thisMonthNewMember=memberDao.findThisMonthNewMember(map);
        dataMap.put("thisMonthNewMember",thisMonthNewMember);

        //获取今日订单数量 todayOrderNumber
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        dataMap.put("todayOrderNumber",todayOrderNumber);

        //获取今日到诊数量todayVisitsNumber
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        dataMap.put("todayVisitsNumber",todayVisitsNumber);

        //获取本周的订单数量 thisWeekOrderNumber
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(monday);
        dataMap.put("thisWeekOrderNumber",thisWeekOrderNumber);

        //获取本周的就诊人数 thisWeekVisitsNumber
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        dataMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);

        //获取本月的订单数量 thisMonthOrderNumber
        Integer thisMonthOrderNumber=orderDao.findThisMonthOrderNumber(map);
       dataMap.put("thisMonthOrderNumber",thisMonthOrderNumber);

       //获取本月就诊数量
        Integer thisMonthVisitsNumber=orderDao.findThisMonthVisitsNumber(map);
        dataMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

        //获取热门套餐数据
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        dataMap.put("hotSetmeal",hotSetmeal);

        //最后返回dataMap
        return dataMap;
    }

    @Override
    public Map<String, Object> exportMemberReport(Integer[] ids) {
        Map<String,Object> dataMap = new HashMap<>();
        //List<Member> memberList = memberDao.findAll();
        for (int i = 0; i < ids.length; i++) {
            Integer _id = ids[i];
            //会员信息
            Member member = memberDao.findById(_id);
            Integer id = member.getId();
            dataMap.put("id",id);
            String fileNumber = member.getFileNumber();
            dataMap.put("fileNumber",fileNumber);
            String name = member.getName();
            dataMap.put("name",name);
            String sex = member.getSex();
            dataMap.put("sex",sex);
            String idCard = member.getIdCard();
            dataMap.put("idCard",idCard);
            String phoneNumber = member.getPhoneNumber();
            dataMap.put("phoneNumber",phoneNumber);
            Date regTime = member.getRegTime();
            dataMap.put("regTime",regTime);
            String password = member.getPassword();
            dataMap.put("password",password);
            String email = member.getEmail();
            dataMap.put("email",email);
            Date birthday = member.getBirthday();
            dataMap.put("birthday",birthday);
        }
        return dataMap;
    }

    public List<Map<String, Object>> getReportByOrderId(Integer id) {
        return orderDao.getReportByOrderId(id);
    }

    public Map<String, Object> getOrderUser(Integer id) {
        return orderDao.getOrderUser(id);
    }

    @Override
    public Map getIncomeReport(Date[] dateRange) throws Exception {
        String beginDate = null;
        String endDate = null;
        if (dateRange != null && dateRange.length > 0) {
            //获取查询条件中的时间范围
            beginDate = DateUtils.parseDate2String(dateRange[0]);
            endDate = DateUtils.parseDate2String(dateRange[1]);
        }
        List<Map> incomes = orderDao.getIncomeGroupBySid(beginDate, endDate);
        Map res = new HashMap();
        Double count = 0.0;
        List<String> setmealNameList = new ArrayList<>();
        List<Double> setmealIncomeList= new ArrayList<>();
        //定义一个变量用来存放所有套餐的总金额
        for (Map income : incomes) {
            //获取每个套餐的收入金额
            String setmealName = (String) income.get("name");
            Double setmealIncome = (Double) income.get("income");
            count += setmealIncome;
            setmealNameList.add(setmealName);
            setmealIncomeList.add(setmealIncome);
        }
        //添加所有套餐的总金额
        setmealNameList.add("套餐总收入");
        setmealIncomeList.add(count);
        res.put("setmealname", setmealNameList);
        res.put("income", setmealIncomeList);
        return res;
    }

    @Override
    public Map getReservationsReport(Date[] dateRange) throws Exception {
        String beginDate = null;
        String endDate = null;
        if (dateRange != null && dateRange.length > 0) {
            //获取查询条件中的时间范围
            beginDate = DateUtils.parseDate2String(dateRange[0]);
            endDate = DateUtils.parseDate2String(dateRange[1]);
        }
        //如果没有选择查询日期, 构造一个默认的查询月份
        String defaultDate = new SimpleDateFormat("yyyy-MM").format(new Date());
        //定义需要返回的map集合
        Map res = new HashMap();
        //定义封装时间的list集合
        List<String> timeDataNames = new ArrayList<>();
        //定义封装对应时间的预约人数集合
        List<Long> numbers = new ArrayList<>();
        //定义封装对应时间的到诊人数集合
        List<Long> reservations = new ArrayList<>();
        //数据库查询所需要的数据
        List<Map> list = beginDate == null ? reprotMapper.getDefaultReservationsReport(defaultDate) : reprotMapper.getReservationsReport(beginDate, endDate);
        for (Map map : list) {
            String timeDataName = (String) map.get("timeDataNames");
            Long number = (Long) map.get("number");
            Long reservation = (Long) map.get("reservation");
            timeDataNames.add(timeDataName);
            numbers.add(number);
            reservations.add(reservation);
        }
        res.put("timeDataNames", timeDataNames);
        res.put("number", numbers);
        res.put("reservation", reservations);
        return res;
    }

    @Override
    public Map getMemberAgeRangeAndOrderType() throws Exception {
        Map res = new HashMap();
        //创建需要封装的所有展示项名称集合
        List<String> allNames = new ArrayList<>();
        //创建需要封装的所有订单类型的集合
        List<Map> orderTypes = orderDao.findOrderType();
        //创建需要封装的年龄分布集合
        List<Map> ageRanges = reprotMapper.getMemberAgeRangeReport();
        //设置默认选中的饼状图
        orderTypes.get(0).put("selected", true);
        //给项目名称的展示集合进行赋值
        for (Map orderType : orderTypes) {
            String name = (String) orderType.get("name");
            allNames.add(name);
        }
        for (Map ageRange : ageRanges) {
            String name = (String) ageRange.get("name");
            allNames.add(name);
        }
        res.put("allNames", allNames);
        res.put("orderType", orderTypes);
        res.put("ageRange", ageRanges);
        return res;
    }

    @Override
    public Map getWorkSpaceData() throws Exception {
        Map map = new HashMap();
        //获取会员总人数
        Integer totalMemeberCount = memberDao.findMemberTotalCount();
        //获取今日会员人数
        Integer todayMemeberCount = memberDao.findMemberCountByDate(DateUtils.parseDate2String(new Date()));
        //获取预约总人数
        Integer totalOrderCount = orderDao.totalOrderCount();
        //获取今日预约人数
        Integer todayOrderCount = orderDao.findOrderCountByDate(DateUtils.parseDate2String(new Date()));
        //获取今日收入
        Double todayIncome = orderDao.getTodayIncome(DateUtils.parseDate2String(new Date()));
        //获取今日新增问卷
        map.put("totalMemeberCount", totalMemeberCount);
        map.put("todayMemeberCount", todayMemeberCount);
        map.put("totalOrderCount", totalOrderCount);
        map.put("todayOrderCount", todayOrderCount);
        map.put("todayIncome", todayIncome);
        return map;
    }


}

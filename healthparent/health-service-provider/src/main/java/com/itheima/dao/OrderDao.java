package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();

    long findCountBySetmealId(Integer id);

    Integer findThisMonthOrderNumber(Map<String, Object> map);

    Integer findThisMonthVisitsNumber(Map<String, Object> map);

    //查询套餐收入
    @Select("<script>SELECT s.name name, SUM(price) income FROM t_order o, t_setmeal s where 1=1 and o.setmeal_id = s.id " +
            "<if test='beginDate!=null'> " +
            " and o.orderDate BETWEEN #{beginDate} AND #{endDate} " +
            "</if>" +
            "GROUP BY s.id" +
            "</script>")
    List<Map> getIncomeGroupBySid(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    //查询订单类型
    @Select("SELECT orderType name, COUNT(id) value FROM t_order GROUP BY orderType")
    List<Map> findOrderType();

    @Select("SELECT COUNT(*) FROM t_order")
    Integer totalOrderCount();

    @Select(" SELECT ifnull(SUM(s.price),0) FROM t_order o, t_setmeal s WHERE s.id = o.setmeal_id AND orderStatus = '已到诊' AND orderDate = #{today}")
    Double getTodayIncome(@Param("today") String todayIncome);

    List<Member> findMemberIdInOrder(Integer id);


    Order findByMemberAndSetMealId(Map map);
    void addOrder(Order order);
    void delete(Map<String, Object> map);
    void changeStatus(Map<String, Object> conditionMap);
    Page<Map<String, Object>> getOrderByCondition(Map<String, Object> map);

    Map<String, Object> backMessage(Integer id);

    void update(Map map);

    List<Order> findOrderByMemberId(Integer id);

    Map<String, Object> findAll(Integer id);

    //根据会员id获取预约id
    List<Integer> findOrderIdsByMemberId(Integer memberId);

    List<Integer> findOrderIdsByMemberIdAndDate(Map<String,Object>map);

    Map<String,Object> findById(String id);

    String findSeatmealNameIdByOrderId(Integer id);

    Order findAllByOrderId(Integer id);

    Integer findInterventionCountByOrderId(Integer id);

    void updateInterventionId(Map<String, Object> map1);

    List<Map<String, Object>> getReportByOrderId(Integer id);

    Map<String, Object> getOrderUser(Integer id);

    Order checkDate(Map<String, Object> mapCheckCondition);
}

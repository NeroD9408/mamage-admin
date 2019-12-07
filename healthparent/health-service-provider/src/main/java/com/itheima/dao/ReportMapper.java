package com.itheima.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ReportMapper {

    @Select("select s.name, count(o.id) setmeal_count, (count(o.id)/(select count(*) from t_order)) proportion from t_setmeal s, t_order o where s.id = o.setmeal_id group by o.setmeal_id")
    List<Map> findHotSetmeal();

    @Select("<script>SELECT n.days timeDataNames, COALESCE(n.count,0) number, COALESCE(r.count,0) reservation " +
            " FROM (SELECT DATE_FORMAT(orderDate,'%Y-%m-%d') days,COUNT(id) COUNT FROM t_order GROUP BY days) n " +
            " LEFT JOIN (SELECT DATE_FORMAT(orderDate,'%Y-%m-%d') days,COUNT(id) COUNT FROM t_order WHERE orderStatus = '已到诊' GROUP BY days) r " +
            " ON n.days = r.days <if test='beginDate!=null'> HAVING n.days BETWEEN #{beginDate} AND #{endDate}</if></script>")
    List<Map> getReservationsReport(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    @Select("SELECT n.days timeDataNames, COALESCE(n.count,0) number, COALESCE(r.count,0) reservation " +
            "    FROM (SELECT DATE_FORMAT(orderDate,'%Y-%m-%d') days,COUNT(id) COUNT FROM t_order GROUP BY days) n " +
            "    LEFT JOIN " +
            "    (SELECT DATE_FORMAT(orderDate,'%Y-%m-%d') days,COUNT(id) COUNT FROM t_order WHERE orderStatus = '已到诊' GROUP BY days) r " +
            "    ON n.days = r.days HAVING n.days LIKE '%${defaultDate}%' ")
    List<Map> getDefaultReservationsReport(@Param("defaultDate") String defaultDate);

    @Select("SELECT (CASE " +
            "WHEN a.age IS NULL THEN '未知' " +
            "WHEN a.age BETWEEN 0 AND 6 THEN '婴幼儿' " +
            "WHEN a.age BETWEEN 7 AND 12 THEN '少儿' " +
            "WHEN a.age BETWEEN 13 AND 17 THEN '青少年' " +
            "WHEN a.age BETWEEN 18 AND 45 THEN '青年' " +
            "WHEN a.age BETWEEN 46 AND 70 THEN '中年' " +
            "WHEN a.age >= 70 THEN '老年' " +
            "END) AS name, " +
            "COUNT(*) value FROM " +
            "(SELECT(YEAR (CURDATE())- YEAR ( birthday )- 1+ (DATE_FORMAT( birthday, '%m%d')<=(DATE_FORMAT( CURDATE(), '%m%d')))) age FROM t_member) a GROUP BY NAME ")
    List<Map> getMemberAgeRangeReport();

}

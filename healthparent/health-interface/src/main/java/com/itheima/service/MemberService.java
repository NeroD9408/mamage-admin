package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Report;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member) throws Exception;

    List<Integer> findCountByDate(List<String> monthList);

    PageResult findPage(QueryPageBean queryPageBean) throws Exception;

    void edit(Member member) throws Exception;

    Member findById(Integer id);

    Result deleteById(Integer id);

    List<Member> findAll();

    PageResult queryPageReport(QueryPageBean queryPageBean) throws Exception;

    Map<String, Object> getMessage(Integer id);

    List<CheckItem> getCheckItemByOrderId(Integer id);

    void addReport(Report report);

    void addReportItem(Map<String, String> map);

    int getCount(Integer orderId);

    List<Map<String,Object>>findByPhoneNumber(String telephone);

    void changeStatus(Integer orderId);

    Report findReportByOrderId(Integer orderId);

    Result deleteReport(Integer id);
}

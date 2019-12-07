package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Report;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Map<String,Object>> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String phoneNumber);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();

    Integer findByMonth(String month);

    Integer findThisMonthNewMember(Map<String, Object> map);

    void updateMember(Map map);

    Page<Map<String, Object>> queryPageReport(String queryString);

    Map<String, Object> getMessage(Integer id);

    List<CheckItem> getCheckItemByOrderId(Integer id);

    void addReport(Report report);

    void addReportItem(Map<String, String> map);

    int getCount(Integer orderId);

    void changeStatus(Integer orderId);

    Report findReportByOrderId(Integer orderId);

    void addReportStatus(Integer orderId);

    String checkStatus(Integer id);

    int getReportIdByOrderId(Integer id);

    void deleteReportItem(int reportId);

    void deleteReport(Integer id);

    void changeReportExist(Integer id);
}

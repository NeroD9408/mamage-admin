package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Report;
import com.itheima.utils.DateUtils;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) throws Exception {
        String password = member.getPassword();
        if (password != null) {
            //password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        String phoneNumber = member.getPhoneNumber();
        String substring = phoneNumber.substring(7);
        String s = DateUtils.parseDate2String(new Date());
        String replace = s.replace("-", "");
        String fileNumber = replace + substring;
        member.setFileNumber(fileNumber);
        member.setRegTime(new Date());
        memberDao.add(member);
    }

    @Override
    public List<Integer> findCountByDate(List<String> monthList) {
        List<Integer> countList = new ArrayList<>();
        for (String _month : monthList) {
            String month = _month + ".31";
            Integer count = memberDao.findByMonth(month);
            countList.add(count);
        }
        return countList;
    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) throws Exception {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);

        Page<Map<String, Object>> page = memberDao.selectByCondition(queryString);
        for (Map<String, Object> map : page) {
            Date regTime = (Date) map.get("regTime");
            map.put("regTime", DateUtils.parseDate2String(regTime));
        }
        long total = page.getTotal();
        return new PageResult(total, page);
    }

    @Override
    public void edit(Member member) throws Exception {
        String phoneNumber = member.getPhoneNumber();
        String substring = phoneNumber.substring(7);
        String s = DateUtils.parseDate2String(new Date());
        String replace = s.replace("-", "");
        String fileNumber = replace + substring;
        member.setFileNumber(fileNumber);
        memberDao.edit(member);
    }

    @Override
    public Member findById(Integer id) {
        Member member = memberDao.findById(id);
        return member;
    }

    @Override
    public Result deleteById(Integer id) {
        try {
            memberDao.deleteById(id);
            return new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MEMBER_FAIL);
        }
    }

    @Override
    public List<Member> findAll() {

        return memberDao.findAll();
    }

    @Override
    public PageResult queryPageReport(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Map<String, Object>> pageReport = memberDao.queryPageReport(queryString);
        for (Map<String, Object> stringObjectMap : pageReport) {
            Date orderDate = (Date) stringObjectMap.get("orderDate");
            stringObjectMap.put("orderDate", new SimpleDateFormat("yyyy-MM-dd").format(orderDate));
        }
        long total = pageReport.getTotal();
        return new PageResult(total, pageReport);
    }

    @Override
    public Map<String, Object> getMessage(Integer id) {
        Map<String, Object> message = memberDao.getMessage(id);
        System.out.println(message.size());
        return message;
    }

    @Override
    public List<CheckItem> getCheckItemByOrderId(Integer id) {
        return memberDao.getCheckItemByOrderId(id);
    }

    //体检上传
    private int reportId;

    @Override
    public void addReport(Report report) {
        memberDao.addReport(report);
        reportId = report.getId();
    }

    @Override
    public void addReportItem(Map<String, String> map) {
        map.put("reportId", String.valueOf(reportId));
        memberDao.addReportItem(map);
    }

    @Override
    public int getCount(Integer orderId) {
        return memberDao.getCount(orderId);
    }

    public List<Map<String, Object>> findByPhoneNumber(String telephone) {
        //根据手机号获取会员id
        System.out.println(telephone);
        Member member = memberDao.findByTelephone(telephone);
        Integer memberId = member.getId();
        System.out.println(12345);
        //根据会员id获取预约id
        List<Integer> ids = orderDao.findOrderIdsByMemberId(memberId);
        List<Map<String, Object>> list = new ArrayList<>();

        for (Integer id : ids) {
            //获取预约信息
            Map<String, Object> map = orderDao.findAll(id);
            list.add(map);
        }
        return list;
    }

    //体检上传改变报告状态
    @Override
    public void changeStatus(Integer orderId) {
        memberDao.changeStatus(orderId);
    }

    //查询报告是否已上传
    @Override
    public Report findReportByOrderId(Integer orderId) {
        return memberDao.findReportByOrderId(orderId);
    }

    @Override
    public Result deleteReport(Integer id) {
        String status = memberDao.checkStatus(id);
        if (status.equals("未提交")) {
            return new Result(false, "删除失败,体检报告未提交,请提交后再执行此操作");
        }
        int reportId = memberDao.getReportIdByOrderId(id);
        memberDao.deleteReportItem(reportId);
        memberDao.deleteReport(id);
        memberDao.changeReportExist(id);
        return new Result(true, "删除成功!");
    }
}

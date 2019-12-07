package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.*;
import com.itheima.service.*;
import com.itheima.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;
    @Reference
    private OrderService orderService;
    @Reference
    private CheckGroupService checkGroupService;
    @Reference
    private CheckItemService checkItemService;
    @Reference
    private AddressService addressService;
    //查询会员数量统计数据(折线图)
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        //定义一个Map集合,后续用来返回页面想要的数据格式
        Map<String, Object> map = new HashMap<>();
        List<String> monthList = new ArrayList<>();

        //因为是要查询前12个月的数据,所以先获取当前的时间
        Calendar calendar = Calendar.getInstance();
        //用add方法,把日历时间往前推12个月,现在的calendar对象表示的就是12个月前的今天的日期了
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            //循环12次,每次把日期往前加1个月
            calendar.add(Calendar.MONTH, 1);
            //获取每次获得的新的日期
            Date date = calendar.getTime();
            //装到list集合里面
            monthList.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months", monthList);

        List<Integer> countList = memberService.findCountByDate(monthList);
        map.put("memberCount", countList);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    //查询套餐的数据统计列表(饼状图)
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //这个功能写的和视频老师说的不一样,这个是我自己写的,就不用视频老师的了
        try {
            Map<String, Object> data = setMealService.findSetmealNameAndValue();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    //查询运营数据统计
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> reportData = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    //统计数据的导出
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            String path = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数  
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数
            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数
            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //通过输出流进行文件下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms‐excel");
            response.setHeader("content‐Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);
        }
    }

    //导出会员信息
    @RequestMapping("/exportMemberReport")
    public Result exportMemberReport(Integer[] ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // List<Member> list = memberService.findAll();
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "member_report.xlsx";
            //在内存中创建工作簿对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);

            Map<String, Object> map = reportService.exportMemberReport(ids);
            //获取会员信息
            for (int i = 0; i < map.size(); i++) {  //104
                for (int j = 0; j < ids.length; j++) {
                    Integer _id = ids[j];
                    Member member = memberService.findById(_id);
                    Integer id = member.getId();
                    String fileNumber = member.getFileNumber();
                    String name = member.getName();
                    String sex = member.getSex();
                    if (sex.equals("1")){
                        sex = "男";
                    } else {
                        sex = "女";
                    }
                    String idCard = member.getIdCard();
                    String phoneNumber = member.getPhoneNumber();
                    Date regTime = member.getRegTime();
                    String email = member.getEmail();
                    Date birthday = member.getBirthday();
                    //获取行
                    XSSFRow row = sheet.getRow(3 + j);
                    //会员信息
                    XSSFCell cell = row.getCell(0);
                    cell.setCellValue(id); //id
                    row.getCell(1).setCellValue(fileNumber); //档案号
                    row.getCell(2).setCellValue(name); //姓名
                    row.getCell(3).setCellValue(sex); //性别
                    row.getCell(4).setCellValue(idCard); //身份证号
                    row.getCell(5).setCellValue(phoneNumber); //手机号
                    row.getCell(6).setCellValue(DateUtils.parseDate2String(regTime)); //注册时间
                    row.getCell(7).setCellValue(email); //邮箱
                    row.getCell(8).setCellValue(DateUtils.parseDate2String(birthday)); //生日

                    //预约信息
                    String addressName = "";
                    String setmealName = "";
                    String checkGroupName = "";
                    String checkItemName = "";
                    List<Order> orderList = orderService.findOrderByMemberId(id);
                    for (int k = 0; k < orderList.size(); k++) {
                        //第一个会员的预约信息
                        Order order = orderList.get(k);
                        Date orderDate = order.getOrderDate();
                        String orderType = order.getOrderType();
                        String orderStatus = order.getOrderStatus();
                        Integer setmeal_id = order.getSetmeal_id(); //获得预约套餐的id
                        Integer address_id = order.getAddress_id(); //获得地址id
                        row.getCell(9).setCellValue(DateUtils.parseDate2String(orderDate));
                        row.getCell(10).setCellValue(orderType);
                        row.getCell(11).setCellValue(orderStatus);
                        //机构地址
                        Address address = addressService.findById(address_id);
                        addressName = addressName + address.getName() + ".";
                        //套餐信息
                        Setmeal setmeal = setMealService.findById(setmeal_id);
                        setmealName = setmealName + setmeal.getName() + ".";

                        Integer setmealId = setmeal.getId();
                        //检查组信息
                        CheckGroup checkGroup = checkGroupService.findById(setmealId);
                        checkGroupName = checkGroupName + checkGroup.getName() + ".";

                        Integer checkGroupId = checkGroup.getId();
                        List<Integer> checkItemIds = checkGroupService.findCheckItemsById(checkGroupId);
                        for (Integer checkItemId : checkItemIds) {
                            //检查项信息
                            CheckItem checkItem = checkItemService.findById(checkItemId);
                            checkItemName = checkItemName + checkItem.getName() + ",";
                        }
                    }
                    row.getCell(12).setCellValue(addressName);
                    row.getCell(13).setCellValue(setmealName);
                    row.getCell(14).setCellValue(checkGroupName);
                    row.getCell(15).setCellValue(checkItemName);
                }
            }
            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);

            out.flush();
            out.close();
            excel.close();
            return null;
            //return new Result(true, MessageConstant.GET_Member_REPORT_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_Member_REPORT_FAIL);
        }
    }

    //收入报告
    @RequestMapping("/getIncomeReport")
    public Result getIncomeReport(@RequestBody Date[] dateRange) {
        try {
            System.out.println(dateRange.length);
            Map res = reportService.getIncomeReport(dateRange);
            return new Result(true, "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "");
        }
    }

    //预约到诊数据
    @RequestMapping("/getReservationsReport")
    public Result getReservationsReport(@RequestBody Date[] dateRange) {
        try {
            Map res = reportService.getReservationsReport(dateRange);
            return new Result(true, "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "数据获取失败");
        }
    }

    //会员年龄分段
    @RequestMapping("/getMemberAgeRangeAndOrderType")
    public Result getMemberAgeRangeAndOrderType() {
        try {
            Map res = reportService.getMemberAgeRangeAndOrderType();
            return new Result(true, "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取数据失败");
        }
    }

    //工作空间需要的数据展示
    @RequestMapping("/getWorkSpaceData")
    public Result getWorkSpaceData() {
        try {
            Map res = reportService.getWorkSpaceData();
            return new Result(true, "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取数据失败");
        }
    }
}

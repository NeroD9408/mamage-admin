package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Report;
import com.itheima.service.MemberService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) throws Exception {
        PageResult data = memberService.findPage(queryPageBean);
        return data;
    }
    /**
     * 新建会员
     * @param member
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Member member){
        try {
            memberService.add(member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.ADD_MEMBER_SUCCESS);
    }

    /**
     * 编辑会员信息
     * @param member
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Member member){
        try {
            memberService.edit(member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_MEMBER_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Member member = memberService.findById(id);
            return new Result(true,MessageConstant.QUERY_MEMBER_SUCCESS,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MEMBER_FAIL);
        }

    }

   @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
       return memberService.deleteById(id);
   }

    @RequestMapping("/queryPageReport")
    public PageResult queryPageReport(@RequestBody QueryPageBean queryPageBean) throws Exception {
        return memberService.queryPageReport(queryPageBean);
    }

    @RequestMapping("/exportReport")
    public Result exportReport(Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            //姓名,年龄,检查套餐,检查日期, order  检查项目,单位,参考范围 checkItem    //手机号码    member
            Map<String, Object> map = memberService.getMessage(id);
            List<CheckItem> checkItems = memberService.getCheckItemByOrderId(id);
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_report.xlsx";
            XSSFWorkbook excel = new XSSFWorkbook(filePath);
            XSSFSheet sheet = excel.getSheetAt(0);
            XSSFRow row1 = sheet.getRow(1);
            row1.getCell(3).setCellValue((String) map.get("patientsName"));
            String sex = ((String) map.get("sex")).equals("1") ? "男" : "女";
            row1.getCell(5).setCellValue(sex);
            row1.getCell(7).setCellValue((Integer) map.get("age"));
            row1.getCell(9).setCellValue((String) map.get("idCard"));
            row1.getCell(12).setCellValue((String) map.get("phoneNumber"));
            XSSFRow row2 = sheet.getRow(2);
            row2.getCell(3).setCellValue((String) map.get("name"));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format((Date) map.get("orderDate")));
            row2.getCell(9).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format((Date) map.get("orderDate")));

            for (int i = 0; i < checkItems.size(); i++) {
                XSSFRow row = sheet.getRow(i + 4);
                row.getCell(2).setCellValue(checkItems.get(i).getName());
                row.getCell(8).setCellValue(checkItems.get(i).getUnit());
                row.getCell(9).setCellValue(checkItems.get(i).getScope());
            }
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);
            out.flush();
            out.close();
            excel.close();
            return new Result(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, null);
        }
    }


    //体检上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile,Integer orderId) {
        try {
            Report reportExist = memberService.findReportByOrderId(orderId);
            if (reportExist != null){
                return new Result(false,null);
            }
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            Report report = new Report();
            int count = memberService.getCount(orderId);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            XSSFRow row1 = sheet.getRow(1);
            //姓名
            String name = getValue(row1.getCell(3));
            System.out.println(name);
            //性别
            String sex = getValue(row1.getCell(5));
            //年龄
            String age = getValue(row1.getCell(7));
            //身份证
            String idCard = getValue(row1.getCell(9));
            //电话号码
            String phoneNumber = getValue(row1.getCell(12));
            XSSFRow row2 = sheet.getRow(2);
            //套餐id
            String setMealName = getValue(row2.getCell(3));
            //体检日期
            String checkDate = getValue(row2.getCell(9));
            //负责医生
            String doctorName = getValue(row2.getCell(12));
            //检查组名字      医生嘱咐
           report.setOrderId(orderId);
           report.setAge(age);
           report.setCheckDate(checkDate);
           report.setDoctorName(doctorName);
           report.setName(name);
           report.setIdCard(idCard);
           report.setSex(sex);
            report.setPhoneNumber(phoneNumber);
            report.setSetMealName(setMealName);
            memberService.addReport(report);
            for (int i = 4; i < count+4; i++) {
                Map<String,String> map = new HashMap<>();
                XSSFRow row = sheet.getRow(i);
                String checkItemName = getValue(row.getCell(2));
                String result = getValue(row.getCell(6));
                String attention = getValue(row.getCell(10));
                map.put("result",result);
                map.put("attention",attention);
                map.put("checkItemName",checkItemName);
                memberService.addReportItem(map);
            }

            memberService.changeStatus(orderId);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,null);
        }
        return new Result(true,null);
    }
    //读取单元格的值
    private String getValue(XSSFCell xSSFCell) {
        if (null == xSSFCell) {
            return "";
        }
        if (xSSFCell.getCellType() == xSSFCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值  
            return String.valueOf(xSSFCell.getBooleanCellValue());
        } else if (xSSFCell.getCellType() == xSSFCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值  
            return String.valueOf(xSSFCell.getNumericCellValue());
        } else {// 返回字符串类型的值  
            return String.valueOf(xSSFCell.getStringCellValue());
        }
    }

    @RequestMapping("/deleteReport")
    public Result deleteReport(Integer id){
        return memberService.deleteReport(id);
    }
}
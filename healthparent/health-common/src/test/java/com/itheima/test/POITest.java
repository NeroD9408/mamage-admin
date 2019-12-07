package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.util.Date;

public class POITest {



 /*   @Test
    public void test1(){
        String date="2019/11/22";
        System.out.println(new Date(date));
    }*/
/*    @Test
    public void test1() throws Exception {
        //获取excel对象
        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("f:\\poi.xlsx")));
        //excel里面也有分页,根据index获取分页的对象,0就是第一页,1是第二页
        XSSFSheet sheet = excel.getSheetAt(0);
        //遍历这一页,获取每一行
        for (Row row : sheet) {
            for (Cell cell : row) {
                //遍历每一行,获取这一行里面的每一个单元格
                System.out.println(cell.getStringCellValue());
            }
        }
    }


    //第二种方式
    @Test
    public void test2() throws Exception {
        //获取excel对象
        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("f:\\poi.xlsx")));
        //excel里面也有分页,根据index获取分页的对象,0就是第一页,1是第二页
        XSSFSheet sheet = excel.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastRowNum; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }


    //往excel表格里写数据
    @Test
    public void test3() throws Exception{
        XSSFWorkbook excel=new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("用户表");
        XSSFRow title = sheet.createRow(0);
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("年龄");
        title.createCell(2).setCellValue("住址");
        title.createCell(3).setCellValue("会员等级");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("小凡");
        row1.createCell(1).setCellValue("20");
        row1.createCell(2).setCellValue("大竹峰");
        row1.createCell(3).setCellValue("天神");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("碧瑶");
        row2.createCell(1).setCellValue("19");
        row2.createCell(2).setCellValue("鬼王宗");
        row2.createCell(3).setCellValue("天神");

        FileOutputStream fos=new FileOutputStream(new File("f:\\user.xlsx"));
        excel.write(fos);
    }*/
}

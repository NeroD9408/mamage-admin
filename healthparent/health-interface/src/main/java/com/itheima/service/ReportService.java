package com.itheima.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {

    Map<String,Object> getBusinessReportData();

    Map getIncomeReport(Date[] dateRange) throws Exception;

    Map getReservationsReport(Date[] dateRange) throws Exception;

    Map getMemberAgeRangeAndOrderType() throws Exception;

    Map getWorkSpaceData() throws Exception;

    Map<String, Object> exportMemberReport(Integer[] ids);

    List<Map<String, Object>> getReportByOrderId(Integer id);

    Map<String, Object> getOrderUser(Integer id);
}

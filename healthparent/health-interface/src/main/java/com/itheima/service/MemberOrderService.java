package com.itheima.service;

import java.util.List;
import java.util.Map;

public interface MemberOrderService {
    List<Map<String, Object>> findByPhoneNumberAndDate(String telephone, String start, String end);
}

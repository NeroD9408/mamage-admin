package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberOrderService.class)
@Transactional
public class MemberOrderServiceImpl implements MemberOrderService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public List<Map<String, Object>> findByPhoneNumberAndDate(String telephone, String start, String end) {
        Member member = memberDao.findByTelephone(telephone);
        Integer memberId = member.getId();
        Map<String,Object> map=new HashMap<>();
        map.put("memberId",memberId);
        map.put("start",start);
        map.put("end",end);
        List<Integer> ids = orderDao.findOrderIdsByMemberIdAndDate(map);
        List<Map<String,Object>> list=new ArrayList<>();
        for (Integer id : ids) {
            Map<String,Object> map1=orderDao.findAll(id);
            list.add(map1);
        }
        return list;
    }
}

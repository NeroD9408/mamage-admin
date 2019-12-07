package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.InterventionDao;
import com.itheima.dao.OrderDao;
import com.itheima.pojo.Intervention;
import com.itheima.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service(interfaceClass = InterventionService.class)
@Transactional
public class InterventionServiceImpl implements InterventionService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private InterventionDao interventionDao;

    @Override
    public Map<String, Object> findAMapById(Integer id) {
        Map<String,Object> map=new HashMap<>();
        //先获取套餐的名称
        String setmealName=orderDao.findSeatmealNameIdByOrderId(id);
        map.put("setmealName",setmealName);
        //根据order的id查询这个id对应的所有信息
        Order order=orderDao.findAllByOrderId(id);
        //获取体检人姓名
        String patientsName = order.getPatientsName();
        map.put("patientsName",patientsName);
        //获取体检日期
        Date date = order.getOrderDate();
        String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        map.put("orderDate",formatDate);
        //获取orderCount体检报告的数量,这个本来也要去数据库查的,先给个1吧
        map.put("orderCount","1");
        //获取opinionCount健康师意见数量这个本来也要去数据库查的,先给个1吧
        map.put("opinionCount","1");
        //获取剩下的属性,剩下的属性都在t_intervention表里面,order表里面有t_intervention表的外键关联id
        //先获取这个id,如果是空,说明还没有给出意见,那就随机从对应的饮食,运动,药物表里面随机生成数据,然后
        //保存到t_intervention表里面,并且更新一下这个order对应的t_intervention的外键,如果不为空,说明之前存
        //过了,那就根据这个获取的id去t_intervention表里面查询所有,然后一个一个拿出来赋值给map,当然前面不光
        //要保存到数据库,也得保存到map
        Integer interventionId=orderDao.findInterventionCountByOrderId(id);

        if (interventionId==null){
            //说明这个订单还没有生成对应的干预表
            Intervention intervention=new Intervention();
            Random r=new Random();//从food表里面随机取数据的Ranodm,时间原因,范围就根据数据库写死了
            int a = r.nextInt(676)+1;
            int b = r.nextInt(676)+1;
            while (a==b){//防止2个值一样
                b=r.nextInt(676);
            }
            String foodOne=interventionDao.findFood(a);
            String foodTwo=interventionDao.findFood(b);
            intervention.setFoodOne(foodOne);
            intervention.setFoodTwo(foodTwo);

            int c = r.nextInt(200)+400;
            int d = r.nextInt(200)+400;
            while (c==d){//防止2个值一样
                d=r.nextInt(200)+400;
            }

            String sportOne=interventionDao.findSport(c);
            String sportTwo=interventionDao.findSport(d);
            intervention.setSportOne(sportOne);
            intervention.setSportTwo(sportTwo);
            int e = r.nextInt(14)+1;
            int f = r.nextInt(14)+1;
            while (e==f){//防止2个值一样
                f=r.nextInt(14)+1;
            }

           String medicineOne= interventionDao.findMedicine(e);
           String medicineTwo= interventionDao.findMedicine(f);
            intervention.setMedicineOne(medicineOne);
            intervention.setMedicineTwo(medicineTwo);

            interventionDao.add(intervention);
            Integer id1 = intervention.getId();
            Map<String,Object>map1=new HashMap<>();
            map1.put("orderId",id);
            map1.put("interventionId",id1);
            orderDao.updateInterventionId(map1);
        }

        interventionId=orderDao.findInterventionCountByOrderId(id);
        Intervention intervention=interventionDao.findInterventionById(interventionId);
        String foodOne = intervention.getFoodOne();
        map.put("foodOne",foodOne);
        String foodTwo = intervention.getFoodTwo();
        map.put("foodTwo",foodTwo);
        String medicineOne = intervention.getMedicineOne();
        map.put("medicineOne",medicineOne);
        String medicineTwo = intervention.getMedicineTwo();
        map.put("medicineTwo",medicineTwo);
        String sportOne = intervention.getSportOne();
        map.put("sportOne",sportOne);
        String sportTwo = intervention.getSportTwo();
        map.put("sportTwo",sportTwo);
        return map;
    }
}

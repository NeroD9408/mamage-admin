package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.AddressDao;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
   @Autowired
    private OrderDao orderDao;
   @Autowired
   private OrderSettingDao orderSettingDao;
   @Autowired
   private MemberDao memberDao;
   @Autowired
   private AddressDao addressDao;

   //新增订单
    @Override
    public Result order(Map map) throws Exception{
        //先根据日期去查询当天是否设置过预约,只要设置过预约才能进行预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting=orderSettingDao.findByDate(date);
        if (orderSetting==null){
            //说明这个日期是没有设置可预约人数的,所以不能预约
            return  new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断可预约人数和已预约人数的关系
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (number<=0 || number<=reservations){//视频老师的判断是reservations>=number
            //可预约人数是小于等于0的,或者可预约人数是小于等于已预约人数的,都不能预约
            return  new Result(false, MessageConstant.ORDER_FULL);
        }

        //走到这一步,说明当前的日期是有空位可以预约的,但是还得判断该用户是否重复预约,如果会员id,预约日期和预约套餐id都一样那就属于重复预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member!=null){
        Integer memberId = member.getId();
        String setmealId = (String) map.get("setmealId");
        //预约日期date 上面已经获取过了,这样判断的haul,如果是同一个会员,就不能帮别人预约了,所以不能根据会员的id
            //判断,而是通过前端填写的会员名判断,同一个姓名,同一天,预约同一个套餐,算重复预约,但是如果是重名了,并且
            //在同一天预约了同一套餐,这样判断就会导致第二个人无法预约,所以我们要
            //根据5个条件去判断,就是同一个会员id在同一天为同一个人(这里其实应该让前端传体检人的身份证过来,但是时间不够,就不写了)
            // 在同一个医院预约了同一个套餐,算重预约

            //判断客户是否选择了地址
            String addressName = (String) map.get("addressName");
            //这个是通过医院名称获取的医院id,也就是客户通过地图选择的
            Integer addressId=addressDao.findIdByName(addressName);
            if (addressId==null){
                return new Result(false,"您未选择医院地址,请选择后再提交哦~");
            }

            //获取实际填写的体检人姓名
            String patientsName = (String) map.get("name");

            Order order=new Order(memberId,date,Integer.parseInt(setmealId));
            order.setPatientsName(patientsName);
            order.setAddressId(addressId);

            List<Order> list = orderDao.findByCondition(order);
            if (list!=null && list.size()>0){
            return new Result(false, MessageConstant.HAS_ORDERED);
             }
            }else{
            //说明不是会员,那先登记信息注册到会员表里面

            member=new Member();
            member.setIdCard((String) map.get("idCard"));
            member.setName((String)map.get("name"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setSex((String)map.get("sex"));
            //注册的时候把档案号也注册进去
    /*        String phoneNumber = member.getPhoneNumber();
            String substring = phoneNumber.substring(7);
            String s = DateUtils.parseDate2String(new Date());
            String replace = s.replace("-", "");
            String fileNumber = replace + substring;
            member.setFileNumber(fileNumber);*/

            memberDao.add(member);
           }


        //都筛选过了,没问题了,那就开始设置预约,设置之前先通过医疗机构名称获取一下机构的id
        Order order=new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType((String) map.get("orderType"));
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        //获取所选医院的id,虽然前面已经写过了,但是不重新获取,下面order.setAddressId(addressId)里面的参数获取不到,很奇怪,先重写获取一下吧
        String addressName = (String) map.get("addressName");
        Integer addressId=addressDao.findIdByName(addressName);
        order.setAddressId(addressId);
        String patientsName = (String) map.get("name");
        order.setPatientsName(patientsName);
        Integer age= Integer.parseInt((String)map.get("age")) ;
        String sex= (String) map.get("sex");
        String idCard= (String) map.get("idCard");
        order.setAge(age);
        order.setSex(sex);
        order.setIdCard(idCard);
        orderDao.add(order);

        //添加好了之后,还要把ordersetting表里面的已预约人数加1
        orderSetting.setReservations(reservations+1);//这个reservations上面已经获取过了,加1就行了,然后去更新表
        orderSettingDao.editReservationsByDate(orderSetting);

        return new Result(true, MessageConstant.ORDER_SUCCESS,order.getId());//页面需要这个order的id

    }

    //根据预约id查询预约信息，包括体检人信息、套餐信息
    @Override
    public Map<String, Object> findById(String id) {
        Map<String, Object> map = orderDao.findById4Detail(Integer.parseInt(id));
        return map;
    }

    //根据预约id查询预约记录详情
    @Override
    public Map<String,Object> findByOrderId(String id) {
        Map<String,Object> map =  orderDao.findById(id);
        return map;
    }

    @Override
    public List<Order> findOrderByMemberId(Integer id) {
        return orderDao.findOrderByMemberId(id);
    }
}

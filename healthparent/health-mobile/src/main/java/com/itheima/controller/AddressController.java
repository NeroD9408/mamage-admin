package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;
import com.itheima.service.AddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;

    //客户通过点击套餐详情里面的预约按钮,进入填写预约信息的页面,在进入的时候,像后台请求可以可提供该套餐的机构有哪些
    @RequestMapping("/findAllAddressBySetmealId")
    public Result findAllAddressBySetmealId(Integer id){
        //根据客户所选套餐的id查询出哪些正常在运营的医疗机构有这个套餐,并返回这些机构,展示给客户,非正常运营的机构是不展示的-->
        List<Address> list=addressService.findAllAddressBySetmealId(id);

        //但是前台要的不是整个address,只需要里面的id和地址名称就可以了,所以自己封装一个集合
        List<Map<String,Object>> resutList=new ArrayList<>();
        //遍历查询出的集合,开始封装
        for (Address address : list) {
        Map<String,Object>map=new HashMap<>();
            Integer addressId = address.getId();
            String addressName = address.getName();
            map.put("addressId",addressId);
            map.put("addressName",addressName);
            resutList.add(map);
        }

        return new Result(true, MessageConstant.GET_ADDRESS_LIST_SUCCESS,resutList);
    }


    //再发一个异步请求,根据套餐的id查询出所有可以预约的机构,这个是地图用的
    //这2个有点重复了,先都留着吧,后续应该只保留一个,下拉框的可能就不用了
    @RequestMapping("/findAllAddressDataBySetmealId")
    public Result findAllAddressDataBySetmealId(Integer id){
        //根据客户所选套餐的id查询出哪些正常在运营的医疗机构有这个套餐,并返回这些机构,展示给客户,非正常运营的机构是不展示的-->
        List<Address> list=addressService.findAllAddressBySetmealId(id);


        //但是前台要的不是整个address,只需要里面的id和地址名称就可以了,所以自己封装一个集合
/*       [{title:"美年大健康(滨湖徽州大道体检部)",point:"117.293609|31.74217",isOpen:0,icon:{w:21,h:21,l:0,t:0,x:6,lb:5}}
    ,{title:"美年大健康(蜀山香樟大道体检部)",point:"117.177907|31.828572",isOpen:0,icon:{w:21,h:21,l:0,t:0,x:6,lb:5}}
    ,{title:"美年大健康(庐阳清源路体检部)",point:"117.261845|31.894325",isOpen:0,icon:{w:21,h:21,l:0,t:0,x:6,lb:5}}
    ,{title:"美年大健康(蜀山繁华大道体检部)",point:"117.153761|31.785366",isOpen:0,icon:{w:21,h:21,l:0,t:0,x:6,lb:5}}
    ,{title:"美年大健康(瑶海长江东路体检部)",point:"117.36188|31.875684",isOpen:1,icon:{w:21,h:21,l:0,t:0,x:6,lb:5}}
];*/
        List<Map<String,Object>> resutList=new ArrayList<>();
        //遍历查询出的集合,开始封装
        for (Address address : list) {
            Map<String,Object>map=new LinkedHashMap<>();

            String addressName = address.getName();
            map.put("title",addressName);

            String longitude = address.getLongitude();
            String latitude = address.getLatitude();
            map.put("point",longitude+"|"+latitude);

            map.put("isOpen",0);
            Map<String,Integer>m=new LinkedHashMap<>();
            m.put("w",21);
            m.put("h",21);
            m.put("l",0);
            m.put("t",0);
            m.put("x",6);
            m.put("lb",5);

            map.put("icon",m);

            resutList.add(map);

        }

        return new Result(true, MessageConstant.GET_ADDRESS_LIST_SUCCESS,resutList);
    }
}

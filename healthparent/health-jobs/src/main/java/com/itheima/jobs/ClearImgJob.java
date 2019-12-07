package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //通过sdiff方法,把redis里面的数据进行一下筛选
        Set<String> picNames = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (picNames!=null){
            for (String picName : picNames) {
                //把这个文件名的图片从七牛云删除
                QiniuUtils.deleteFileFromQiniu(picName);
                //这个图片也没必要放在redis里面了,也删除掉
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);

                //System.out.println("执行了"+picName+"---"+new Date());已测试,可以正常删除
            }
        }
    }
}

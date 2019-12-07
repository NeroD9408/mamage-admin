package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService{
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private OrderDao orderDao;

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //先把数据插入数据库
        setMealDao.addSetMeal(setmeal);

        //然后根据最新获得的ID把所有的checkitemIds跟这个id关联在中间表里面
        Integer setMealId = setmeal.getId();
        if (checkgroupIds!=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("setMealId",setMealId);
                map.put("checkGroupId",checkgroupId);
                setMealDao.addReference(map);
            }
        }
        //如果数据被存储到数据库成功了,那么把该条数据的图片信息存储到redis里面,方便后续清理垃圾图片使用
        String fileName = setmeal.getImg();
        if (fileName!=null){
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
        }

    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);

        Page<Setmeal> page=setMealDao.findByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();

        return new PageResult(total,rows);
    }

    //根据id查询套餐
    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    //根据套餐id查询所有对应检查组的id
    @Override
    public List<Integer> findcheckgroupIds(Integer id) {
        return setMealDao.findcheckgroupIds(id);
    }

    //编辑套餐
    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //先把基础项重新设置
        setMealDao.update(setmeal);

        //然后根据id删除所有的关联
        Integer setMealId = setmeal.getId();
        setMealDao.deleteReference(setMealId);

        //重新绑定外界关联
        if (checkgroupIds!=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("setMealId",setMealId);
                map.put("checkGroupId",checkgroupId);
                setMealDao.addReference(map);
            }
        }

        //如果数据被存储到数据库成功了,那么把该条数据的图片信息存储到redis里面,方便后续清理垃圾图片使用
        String fileName = setmeal.getImg();
        if (fileName!=null){
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
        }

    }

    //根据id删除套餐
    @Override
    public void deleteById(Integer id) {
        setMealDao.deleteById(id);
    }

    //查询所有的套餐
    @Override
    public List<Setmeal> getAllSetmeal() {
        return setMealDao.getAllSetmeal();
    }

    //根据id套餐id查询套餐
    @Override
    public Setmeal findSetmealById(Integer id) {

        Setmeal setmeal=setMealDao.newFindById(id);
        return setmeal;

        /*下面是我自己没看视频之前自己写的,后来看了视频,视频老师是在dao利用sql语句处理的,我是在
        服务层处理的,都能实现相同功能,我就统一用老师的方式吧,我自己的代码我注释放在下面了*/

    /*    //第一步通过id获取到这个套餐的信息
        Setmeal setmeal = setMealDao.findById(id);
        //通过id去中间关系表查出所有的跟该id关联的检查组的id,如果不为空就遍历
        List<Integer> list = setMealDao.findcheckgroupIds(id);
        List<CheckGroup> checkGroupsList=new ArrayList<>();
        if (list!=null && list.size()>0){
            for (Integer integer : list) {
                CheckGroup checkGroup = checkGroupDao.findById(integer);
                //通过检查组的id去查询是否有关联的检查项,如果有,继续遍历
                List<Integer> items = checkGroupDao.findCheckItemsById(integer);
                List<CheckItem> checkItemsList=new ArrayList<>();
                if (items!=null && items.size()>0){
                    for (Integer item : items) {
                        CheckItem checkItem= checkItemDao.findById(item);
                        checkItemsList.add(checkItem);
                    }
                    checkGroup.setCheckItems(checkItemsList);
                }
                checkGroupsList.add(checkGroup);
            }
        }
        setmeal.setCheckGroups(checkGroupsList);
        return setmeal;*/
    }


    @Override
    public Map<String, Object> findSetmealNameAndValue() {
        Map<String,Object> data=new HashMap<>();
        //先去setmeal表里面查出所有的套餐信息
        List<Setmeal> setmealList=setMealDao.findAll();
        //遍历获取每一个setmeal,然后获取里面的name赋值给setmealNames
        List<String> setmealNames=new ArrayList<>();
        if (setmealList!=null){
            for (Setmeal setmeal : setmealList) {
                setmealNames.add(setmeal.getName());
            }
        }
        //遍历结束之后,setmealNames集合已经获取了数据库所有的setmeal的name,然后填充进data
        data.put("setmealNames",setmealNames);


        List<Map<String,Object>>setmealCount=new ArrayList();
        //再次遍历刚才获得的setmealList集合,准备给setmealCount填充数据

            for (Setmeal setmeal : setmealList) {
                Map<String,Object>map=new HashMap<>();
                //先获取到这个setmeal的名称,给map先赋值上
                map.put("name",setmeal.getName());
                //第二个value的值需要拿setmeal的id去order订单表中查询
                long count=orderDao.findCountBySetmealId(setmeal.getId());
                map.put("value",count);
                setmealCount.add(map);
            }
        data.put("setmealCount",setmealCount);

        return data;
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }


    @Override
    public List<Integer> getCheckIds(Integer orderId) {
        return setMealDao.getCheckIds(orderId);
    }
}

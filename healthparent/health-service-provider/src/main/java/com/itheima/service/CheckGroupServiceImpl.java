package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //第一步,把该检查组的基本信息插入到检查组的数据表中
        checkGroupDao.addCheckGroup(checkGroup);

        //第二步把遍历传过来的检查项的id,调用方法把每一个检查项的id和该检查组的id加入中间表里面关联
        Integer checkGroupid = checkGroup.getId();
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkGroupid);
                map.put("checkitemId", checkitemId);
                checkGroupDao.addReference(map);
            }
        }
    }

    //分页查询
    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String conditions = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<CheckGroup> page = checkGroupDao.findByCondition(conditions);
        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();
        return new PageResult(total, rows);
    }

    //根据id查询检查组
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    //根据检查组的id查询所有关联的检查项
    @Override
    public List<Integer> findCheckItemsById(Integer id) {
        List<Integer> list = checkGroupDao.findCheckItemsById(id);
        return list;
    }


    //编辑检查组
    @Override
    public void update(Integer[] checkitemIds, CheckGroup checkGroup) {
        //先更新CheckGroup的基本信息
        checkGroupDao.updateCheckGroup(checkGroup);
        //获取当前CheckGroup的id,根据id先把之前的所有关联关系全部清空
        Integer checkGroupid = checkGroup.getId();
        checkGroupDao.deleteReferenceById(checkGroupid);
        //再根据CheckGroup的id以及新获得的checkitemIds重新建立关联
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkGroupid);
                map.put("checkitemId", checkitemId);
                checkGroupDao.addReference(map);
            }
        }
    }


    //删除检查组
    @Override
    public void deleteById(Integer id) {
  /*    //先根据id查询t_checkgroup_checkitem表中是否有该组的关联
        Integer countInCheckgroupCheckitem=checkGroupDao.findCountInCheckgroupCheckitem(id);

        //再根据id查询t_setmeal_checkgroup表中是否有该组的关联
        Integer countInSetmealCheckgroup=checkGroupDao.findCountInSetmealCheckgroup(id);

        //判断,只有在这个数据都是0的时候才能删除
        if (countInCheckgroupCheckitem==0 && countInSetmealCheckgroup==0){
            checkGroupDao.deleteById(id);
        }else{
            throw new RuntimeException();
            这是视频老师说的方式,但是杰哥上课说了,不用判断,因为如果有外界关联,本来就删除不了,还是抛sql异常
            所以直接根据id去删除即可,如下
        }*/

        checkGroupDao.deleteById(id);

    }

    //查询所有的检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //客户在知道该检查组有外界关联的时候仍然选择继续删除该检查组时执行
    @Override
    public void continueDelete(Integer id) {
        //管理员选择继续删除,第一步,清除该组id关联的所有检查项相关的信息
        checkGroupDao.deleteCheckItemsReference(id);
        //第二步删除该组id关联的所有套餐的信息
        checkGroupDao.deletesetmealsReference(id);
        //第三步,删除该id
        checkGroupDao.deleteById(id);
    }
}
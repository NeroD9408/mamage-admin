package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.FoodDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = FoodService.class)
@Transactional
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodDao foodDao;

    public void add(List<Food> dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (Food food : dataList) {
                //判断id是否有值
                long countById = foodDao.findCountById(food.getId());
                if (countById > 0) {
                    //已经有值,执行修改
                    foodDao.update(food);
                } else {
                    //没有值，执行插入操作
                    foodDao.add(food);
                }
            }
        }
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //查询总条数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);

        Page<Food> page=foodDao.findByConition(queryString);
        long total = page.getTotal();
        List<Food> rows = page.getResult();
        return new PageResult(total,rows);
    }

    @Override
    public Food findById(Integer id) {
        return foodDao.findById(id);
    }

    @Override
    public void addFood(Food food) {
         foodDao.add(food);
    }


    @Override
    public void deleteById(Integer id) {
        foodDao.deleteById(id);
    }

    @Override
    public void update(Food food) {
        foodDao.update(food);
    }
}

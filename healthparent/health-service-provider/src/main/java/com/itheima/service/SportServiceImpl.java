package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SportDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Sport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = SportService.class)
@Transactional
public class SportServiceImpl implements SportService {
    @Autowired
    private SportDao sportDao;

    public void add(List<Sport> dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (Sport sport : dataList) {
                //判断改助记码是否有值
                long countByNumber = sportDao.findCountByNumber(sport.getNumber());
                if (countByNumber > 0) {
                    //已经有值,执行修改
                    sportDao.editDateByNumber(sport);
                } else {
                    //没有值，执行插入操作
                    sportDao.add(sport);
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

        Page<Sport> page=sportDao.findByConition(queryString);
        long total = page.getTotal();
        List<Sport> rows = page.getResult();
        return new PageResult(total,rows);
    }

    @Override
    public Sport findById(Integer id) {
        return sportDao.findById(id);
    }

    @Override
    public void addSport(Sport sport) {
        sportDao.add(sport);
    }

    @Override
    public void deleteById(Integer id) {
        sportDao.deleteById(id);
    }

    @Override
    public void update(Sport sport) {
        sportDao.update(sport);
    }

    //222222
    @Override
    public boolean check(String number) {
        long count = sportDao.findCountByNumber(number);
        if (count>0){
            return  false;
        }else {
            return true;
        }
    }
}

package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass =CheckItemService.class )
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //查询总条数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);

        Page<CheckItem> page=checkItemDao.findByConition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //删除检查项
    @Override
    public void deleteById(Integer id) {
      /*  //先根据id判断一下是否关联了检查组
        long count = checkItemDao.findCountById(id);
        if (count>0){//说明跟检查组有关联,就不能删除,手动制造一个运行时异常
            throw new RuntimeException();
        }
        checkItemDao.deleteById(id);
        这是视频老师说的方式,但是杰哥上课说了,不用判断,因为如果有外界关联,本来就删除不了,还是抛sql异常
         所以直接根据id去删除即可,如下
        */

        checkItemDao.deleteById(id);
    }


    //根据id查询检查项
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem=checkItemDao.findById(id);
        if (checkItem!=null){
            return checkItem;
        }else{
            new RuntimeException();
            return null;
        }
    }

    //编辑检查项
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    //这个方法是检查组在编辑的时候,需要显示所有的检查项的信息
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    //在明知道检查项关联着其他检查组还是继续点击删除之后执行该方法
    @Override
    public void continueDelete(Integer id) {
        //去中间表根据检查项的id把对应的所有检查组的关系全部删除
        checkItemDao.continueDelete(id);
        //删除关联之后就可以正常删除该检查项了,用上面写好的删除方法,这次就可以删除成功了
        checkItemDao.deleteById(id);

    }
}

package com.itheima.service;

import com.itheima.pojo.UserInfo;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.dao.UserInfoMapper;
import com.itheima.utils.BCryptUtil;
import com.itheima.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserInfoService.class)
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    //根据用户名查询用户信息
    public UserInfo findUserInfoByUsername(String username) {
        //查询出来的用户信息需要包含角色信息和权限信息
        UserInfo userInfo = null;
        try {
            userInfo = userInfoMapper.findUserInfoByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    //通过用户名密码确认用户信息
    @Override
    public UserInfo findUserInfo(String username) throws Exception {
        UserInfo userInfo = userInfoMapper.findUserInfo(username);
//        userInfo.setBirthdayStr(DateUtils.parseDate2String(userInfo.getBirthday()));
        return userInfo;
    }

    //修改当前登录用户的头像地址
    @Override
    public void updateUserHead(String headimg, String loginUsername) {
        userInfoMapper.updateUserHead(headimg, loginUsername);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfo.setPassword(BCryptUtil.encode(userInfo.getPassword()));
        userInfoMapper.update(userInfo);
    }

    @Override
    public void update(UserInfo userInfo, Integer[] ids) throws Exception {
        //修改用户基本信息
        userInfo.setPassword(BCryptUtil.encode(userInfo.getPassword()));
//        userInfo.setBirthday(DateUtils.parseString2Date(userInfo.getBirthdayStr()));
        userInfoMapper.update(userInfo);
        Integer uid = userInfo.getId();
        //首先清空用户对应的角色信息
        userInfoMapper.clearByUid(uid);
        //修改用户对应的角色信息
        if (ids != null && ids.length > 0) {
            for (Integer id : ids) {
                userInfoMapper.addUserRole(uid, id);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<UserInfo> page = userInfoMapper.findCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(UserInfo userInfo, Integer[] ids) {
        userInfo.setPassword(BCryptUtil.encode(userInfo.getPassword()));
        userInfoMapper.add(userInfo);
        Integer uid = userInfo.getId();
        if (ids != null && ids.length > 0 && uid != null) {
            for (Integer id : ids) {
                userInfoMapper.addUserRole(uid, id);
            }
        }
    }

    @Override
    public void deleteUser(Integer id) {
        userInfoMapper.deleteUser(id);
    }
}

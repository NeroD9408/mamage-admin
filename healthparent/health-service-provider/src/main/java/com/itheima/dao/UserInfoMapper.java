package com.itheima.dao;

import com.itheima.pojo.UserInfo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.Set;

public interface UserInfoMapper {

    @Select("SELECT * FROM t_user u where username = #{username}")
    UserInfo findUserInfoByUsername(String username);

    //根据用户名查询用户信息，并且关联查询角色信息和权限信息
    @Select("select u.id,u.birthday,IFNULL(u.name,'') name,u.gender,u.username,u.password,IFNULL(u.remark,'') remark,IFNULL(u.station,'') station, IFNULL(telephone,'') telephone, u.headimg from t_user u where username = #{username}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(
                    property = "roles",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.RoleMapper.findRoleByUid")
            )
    })
    UserInfo findUserInfo(String username);

    //修改登录用户的头像地址
    @Update("update t_user set headimg = #{headimg} where username = #{username}")
    void updateUserHead(@Param("headimg") String headimg, @Param("username") String loginUsername);

    //"update t_user set name = #{name}, gender = #{gender}, password = #{password}, telephone = #{telephone} where id = #{id}"
    @Update("<script>update t_user set " +
            "<if test='username!=null'> username = #{username}, </if>" +
            "<if test='password!=null'> password = #{password}, </if>" +
            "<if test='name!=null'> name = #{name}, </if>" +
            "<if test='gender!=null'> gender = #{gender}, </if>" +
            "<if test='telephone!=null'> telephone = #{telephone}, </if>" +
            "<if test='birthday!=null'> birthday = #{birthday}, </if>" +
            "<if test='remark!=null'> remark = #{remark}, </if>" +
            "id = #{id} where id = #{id}" +
            "</script>")
    void update(UserInfo userInfo);

    @Select("<script>" +
            "select u.id,u.birthday,IFNULL(u.name,'') name,u.gender,u.username,u.password,IFNULL(u.remark,'') remark,IFNULL(u.station,'') station, IFNULL(telephone,'') telephone, u.headimg from t_user u where 1=1 " +
            "<if test='condition != null and condition.length &gt; 0'>" +
            "and username like '%${condition}%' or name like '%${condition}%' or telephone like '%${condition}%'" +
            "</if>" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "roles",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.RoleMapper.findRoleByUid")
            )
    })
    Page<UserInfo> findCondition(@Param("condition") String queryString);

    @Delete("delete from t_user_role where user_id = #{uid}")
    void clearByUid(Integer uid);

    @Insert("insert into t_user_role values(#{uid}, #{rid})")
    void addUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    @Insert("insert into t_user values(null, #{birthday}, #{gender}, #{username}, #{password}, #{remark}, #{station}, #{telephone}, #{name}, #{headimg})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void add(UserInfo userInfo);

    @Delete("delete from t_user where id = #{id}")
    void deleteUser(Integer id);
}

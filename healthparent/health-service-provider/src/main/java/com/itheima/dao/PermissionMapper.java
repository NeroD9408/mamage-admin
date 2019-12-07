package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;


public interface PermissionMapper {

    //根据角色id查询角色对应的权限信息
    @Select("select p.id, p.name, p.keyword, IFNULL(p.description, '') description from t_permission p, t_role_permission rp where p.id = rp.permission_id and rp.role_id = #{rid}")
    Set<Permission> findPermissionByRid(Integer rid);

    @Select("<script>select p.id, p.name, p.keyword, IFNULL(p.description, '') description from t_permission p where 1=1 <if test='condition != null and condition.length &gt; 0'>and name like '%${condition}%' or keyword like '%${condition}%'</if></script>")
    Page<Permission> findPage(String condition);

    @Select("select p.id, p.name, p.keyword, IFNULL(p.description, '') description from t_permission p")
    List<Permission> findAllPermission();

    @Insert("insert into t_permission values(null, #{name}, #{keyword}, #{description})")
    void add(Permission permission);

    @Select("select p.id, p.name, p.keyword, IFNULL(p.description, '') description from t_permission p where id = #{pid}")
    Permission findById(Integer pid);

    @Delete("delete from t_role_permission where permission_id = #{pid}")
    void clearByPid(Integer pid);

    @Delete("delete from t_permission where id = #{pid}")
    void delete(Integer pid);

    @Update("update t_permission set name = #{name}, keyword = #{keyword}, description = #{description} where id = #{id}")
    void update(Permission permission);

}

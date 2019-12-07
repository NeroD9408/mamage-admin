package com.itheima.dao;

import com.itheima.pojo.Role;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface RoleMapper {

    //根据用户id查询用户对应的角色信息
    @Select("select r.id, r.name, r.keyword, COALESCE(r.description,'无') description from t_role r, t_user_role ur where r.id = ur.role_id and ur.user_id = #{uid}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "permissions",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.PermissionMapper.findPermissionByRid")
            )
    })
    Role findRoleByUid(Integer uid);

    @Select("select * from t_role")
    Page<Role> findPage(String queryString);

//    @Select("select * from t_role")
    @Select("<script>" +
            "select r.id, r.name, r.keyword, COALESCE(r.description,'无') description from t_role r where 1=1 " +
                "<if test='condition != null and condition.length &gt; 0'>" +
                    "and name like '%${condition}%' or keyword like '%${condition}%'" +
                "</if>" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "permissions",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.PermissionMapper.findPermissionByRid")
            ),
            @Result(
                    property = "menus",
                    javaType = LinkedHashSet.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findMenuByRid")
            )
    })
    Page<Role> findAllRoleDetail(@Param("condition") String queryString);

    @Select("select r.id, r.name, r.keyword, COALESCE(r.description,'无') description from t_role r")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "menus",
                    javaType = LinkedHashSet.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findMenuByRid")
            )
    })
    List<Role> findRoleMenu();

    @Insert("insert into t_role values(null, #{name}, #{keyword}, #{description})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addRole(Role role);

    @Insert("insert into t_role_permission values(#{rid}, #{pid})")
    void addRolePermission(@Param("pid") Integer pid, @Param("rid") Integer rid);

    @Insert("insert into t_role_menu values(#{rid}, #{mid})")
    void addRoleMenu(@Param("mid") Integer mid, @Param("rid") Integer rid);

    @Select("select * from t_role where id = #{rid}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "permissions",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.PermissionMapper.findPermissionByRid")
            ),
            @Result(
                    property = "menus",
                    javaType = LinkedHashSet.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findMenuByRid")
            )
    })
    Role findRoleById(Integer rid);

    @Update("update t_role set name = #{name}, keyword = #{keyword}, description = #{description} where id = #{id}")
    void updateRole(Role role);

    @Delete("delete from t_role_permission where role_id = #{rid}")
    void clearPermissionByRid(Integer rid);

    @Delete("delete from t_role_menu where role_id = #{rid}")
    void clearRoleMenuByRid(Integer rid);

    @Delete("delete from t_role where id = #{rid}")
    void deleteRole(Integer rid);
}

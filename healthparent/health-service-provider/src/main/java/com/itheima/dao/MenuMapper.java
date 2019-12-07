package com.itheima.dao;

import com.itheima.pojo.Menu;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface MenuMapper {

    //查询角色对应的顶级菜单
    @Select("SELECT distinct m.* FROM t_menu m, t_role_menu rm WHERE m.id = rm.menu_id AND role_id IN(SELECT ur.role_id FROM t_user u, t_user_role ur WHERE u.id = ur.user_id AND u.id = #{uid}) AND m.level = 1 order by priority")
    LinkedHashSet<Menu> findTopMenu(Integer uid);

    //根据父id查询当前父id底下的子菜单
    @Select("SELECT * FROM t_menu m WHERE parentMenuId = #{parentId} order by priority")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "children",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findChildMenu")
            )
    })
    LinkedHashSet<Menu> findChildMenu(Integer parentId);

    @Select("select * from t_menu order by priority")
    List<Menu> findAllMenu();

    @Select("select m.* from t_menu m, t_role_menu rm where m.id = rm.menu_id and rm.role_id = #{rid} order by priority")
    LinkedHashSet<Menu> findMenuByRid(Integer rid);

    @Select("<script>select id from t_menu where 1=1 and path in" +
            "<foreach collection='mids' open='(' item='mid' separator=',' close=')'> #{mid}</foreach>" +
            "</script>")
    Set<Integer> findIdByPath(@Param("mids") Set<String> menuIds);

    //查询所有菜单列表，包含所有子菜单
    @Select("<script>select * from t_menu where 1=1 " +
                "<if test='condition!=null and condition.length &gt; 0'> " +
                    "and name like '%${condition}%' or linkUrl like '%${condition}%' or level like '%${condition}%'" +
                "</if> and level = 1 order by priority" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "children",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findChildMenu")
            )
    })
    Page<Menu> findPage(@Param("condition") String queryString);

    @Select("select * from t_menu where level = 1 order by priority")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "children",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findChildMenu")
            )
    })
    List<Menu> findAll();

    @Insert("insert into t_menu values(null, #{name}, #{linkUrl}, #{path}, #{priority}, #{icon}, #{description}, #{parentMenuId}, #{level})")
    void addOneMenu(Menu menu);

    @Select("SELECT priority FROM t_menu WHERE LEVEL = 1 ORDER BY priority DESC LIMIT 1")
    Integer getMaxPath();

    @Select("SELECT path FROM t_menu WHERE parentMenuId = #{id} ORDER BY path DESC LIMIT 1")
    String findNodePath(Integer id);

    @Update("<script>update t_menu set " +
            "<if test='name!=null'> name = #{name}, </if>" +
            "<if test='linkUrl!=null'> linkUrl = #{linkUrl}, </if>" +
            "<if test='path!=null'> path = #{path}, </if>" +
            "<if test='priority!=null'> priority = #{priority}, </if>" +
            "<if test='icon!=null'> icon = #{icon}, </if>" +
            "<if test='description!=null'> description = #{description}, </if>" +
            "<if test='true'> parentMenuId = #{parentMenuId}, </if>" +
            "<if test='level!=null'> level = #{level}, </if>" +
            "id = #{id} where id = #{id}" +
            "</script>")
    void editMenu(Menu menu);

    @Select("select count(*) from t_role_menu where menu_id = #{id}")
    Integer canDeleteMenu(Integer id);

    @Delete("delete from t_menu where id = #{id}")
    void deleteMenu(Integer id);

    @Select("select path from t_menu where level = 1 order by desc limit 1")
    Integer findOnePath();

    //根据父id查询当前父id底下的子菜单
    @Select("SELECT * FROM t_menu m WHERE parentMenuId = #{pid} AND id IN(SELECT menu_id FROM t_role_menu WHERE role_id IN(SELECT role_id FROM t_user_role WHERE user_id = #{uid})) AND LEVEL != 1 ORDER BY priority")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(
                    property = "children",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "com.itheima.dao.MenuMapper.findUserChildMenu")
            )
    })
    LinkedHashSet<Menu> findUserChildMenu(@Param("pid") Integer parentId, @Param("uid") Integer uid);
}

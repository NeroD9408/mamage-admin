package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.QuestionLibrary;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface QuestionLibraryMapper {
    @Insert("insert into t_questionlibrary values(null, #{name}, #{type}, #{peopleArea}, #{helpCode}, #{status}, #{togender}, #{desc})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void add(QuestionLibrary questionLibrary);

    @Insert("insert into t_question_library values(#{qid}, #{lid})")
    void addQuestion(@Param("qid") Integer id, @Param("lid") Integer lid);

    @Select("<script>" +
            "select * from t_questionlibrary where 1=1 " +
            "<if test='condition != null and condition.length &gt; 0'>" +
            "and name like '%${condition}%' or name helpCode '%${condition}%'" +
            "</if>" +
            "</script>")
    Page<QuestionLibrary> queryPage(@Param("condition") String conditions);

    @Select("select * from t_questionlibrary where id = #{id}")
    CheckGroup findById(Integer id);

    @Select("SELECT id FROM t_questions WHERE id IN ((SELECT questions_id FROM t_question_library WHERE library_id = #{id}))")
    List<Integer> findQuestionById(Integer id);

    @Update("<script>update t_questionlibrary set " +
            "<if test='name!=null'> name = #{name}, </if>" +
            "<if test='type!=null'> type = #{type}, </if>" +
            "<if test='peopleArea!=null'> peopleArea = #{peopleArea}, </if>" +
            "<if test='helpCode!=null'> helpCode = #{helpCode}, </if>" +
            "<if test='status!=null'> status = #{status}, </if>" +
            "<if test='togender!=null'> togender = #{togender}, </if>" +
            "<if test='desc!=null'> desc = #{desc}, </if>" +
            "id = #{id} where id = #{id}" +
            "</script>")
    void updateQuestionLibrary(QuestionLibrary questionLibrary);

    @Delete("delete from t_question_library where library_id = #{lid}")
    void clearByLid(int lid);

    @Delete("delete from t_questionlibrary where id = #{id}")
    void deleteById(Integer id);
}

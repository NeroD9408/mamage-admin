package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Questions;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface QuestionMapper {

    @Select("<script>" +
                "select * from t_questions where 1=1 " +
                "<if test='condition != null and condition.length &gt; 0'>" +
                    "and question like '%${condition}%' or type like '%${condition}%'" +
                "</if>" +
            "</script>")
    Page<Questions> findPage(@Param("condition") String queryString);

    @Select("select * from t_questions where id = #{id}")
    Questions findById(Integer id);

    @Insert("insert into t_questions values(null, #{question}, #{type}, #{status}, #{togender}, #{rev})")
    void add(Questions questions);

    @Update("<script>" +
                "update t_questions set " +
                "<if test='question!=null'> question=#{question}, </if>" +
                "<if test='type!=null'> type=#{type}, </if>" +
                "<if test='status!=null'> status=#{status}, </if>" +
                "<if test='togender!=null'> togender=#{togender}, </if>" +
                "<if test='rev!=null'> rev=#{rev}, </if>" +
                "id = #{id} where id = #{id}" +
            "</script>")
    void update(Questions questions);

    @Delete("delete from t_questions where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from t_questions where type = '平和质'")
    List<Questions> getQuestion();

    @Select("SELECT * FROM (SELECT * FROM t_questions WHERE TYPE != '平和质' and status = 1 and togender = 0 ORDER BY RAND()) t GROUP BY t.type")
    List<Questions> getRandomQuestion();

    @Select("select * from t_questions where status = 1 and togender = 0 order by rand() limit #{number}")
    List<Questions> getLimitQuestion(Integer number);

    @Select("select * from t_questions")
    List<Questions> findAll();

    @Select("select * from t_result where title = #{title}")
    Map getResultByTitle(@Param("title") String title);
}

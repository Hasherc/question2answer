package com.qming.question2answer.dao;

import com.qming.question2answer.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-23
 * Time: 14:45
 */
@Repository
@Mapper
public interface QuestionDao {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into" + TABLE_NAME + "(" + INSERT_FIELDS + ") values "
            + "(#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})"})
    int addQuestion(Question question);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id = #{id}"})
    public Question selectQuestionById(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update", TABLE_NAME, "set comment_count = ${commentCount} where id = #{id}"})
    public int updateStatus(@Param("id") int id, @Param("commentCount") int commentCount);
}

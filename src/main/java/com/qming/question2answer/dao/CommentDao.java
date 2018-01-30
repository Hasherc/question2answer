package com.qming.question2answer.dao;

import com.qming.question2answer.model.Comment;
import com.qming.question2answer.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-26
 * Time: 12:25
 */
@Repository
@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id,entity_id,entity_type,content, created_date,status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert ", TABLE_NAME, "(", INSERT_FIELDS, ") values ",
            "(#{userId},#{entityId},#{entityType},#{content},#{createdDate},#{status})"})
    public int insertComment(Comment comment);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where ticket = #{ticket}"})
    public LoginTicket selectCommentById(int id);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc"})
    public List<Comment> selectCommentsByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id = #{commentId}"})
    public Comment selectCommentsById(int commentId);

    @Select({"select count(id) from", TABLE_NAME, "where entity_id = #{entityId} and entity_type = #{entityType}"})
    public int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from", TABLE_NAME, "where user_id = #{userId}"})
    public int getUserCommentCount(@Param("userId") int userId);

    @Update({"update", TABLE_NAME, "set status = ${status} where id = #{id}"})
    public int updateStatus(@Param("id") int id, @Param("status") int status);


}

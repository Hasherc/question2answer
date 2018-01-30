package com.qming.question2answer.dao;

import com.qming.question2answer.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-26
 * Time: 19:27
 */
@Mapper
@Repository
public interface MessageDao {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id,to_id,content,created_date, has_read,conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert ", TABLE_NAME, "(", INSERT_FIELDS, ") values ",
            "(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    public int insertMessage(Message message);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where conversation_id=#{conversationId} order by created_date desc"})
    public List<Message> selectMessageListByConversationId(String conversationId);


    @Select({"select", INSERT_FIELDS, ", count(id) as id from " +
            "( select", SELECT_FIELDS, "from", TABLE_NAME, "where from_id=#{userId} or to_id=#{userId} order by created_date desc) t group by conversation_id order by created_date desc"})
    public List<Message> selectConversationListByUserId(int userId);

    @Select({"select count(id) from", TABLE_NAME, "where conversation_id=#{conversationId} and has_read=0 "})
    public int selectConversationUnreadCount(String conversationId);

    @Update({"update", TABLE_NAME, "set has_read = ${status} where conversation_id = #{conversationId}"})
    public int updateUnRead(@Param("conversationId") String conversationId, @Param("status") int status);

}

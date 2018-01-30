package com.qming.question2answer.dao;

import com.qming.question2answer.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-24
 * Time: 19:47
 */
@Repository
@Mapper
public interface TicketDao {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert ", TABLE_NAME, "(", INSERT_FIELDS, ") values ",
            "(#{userId},#{expired},#{status},#{ticket})"})
    public int insertTicket(LoginTicket loginTicket);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where ticket = #{ticket}"})
    public LoginTicket selectTicket(String ticket);

    @Update({"update", TABLE_NAME, "set status = ${status} where ticket = #{ticket}"})
    public int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}

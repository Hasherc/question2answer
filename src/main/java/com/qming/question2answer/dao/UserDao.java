package com.qming.question2answer.dao;

import com.qming.question2answer.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-23
 * Time: 18:09
 */
@Repository
@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, "where id = #{id}"})
    User selectUserById(int id);

    @Insert({"insert ", TABLE_NAME, "(", INSERT_FIELDS, ") values ",
            "(#{name}, #{password}, #{salt}, #{headUrl})"})
    int insertUser(User user);

    @Update({"update", TABLE_NAME, "set password=#{password} where id = #{id}"})
    int updatePassword(User user);

    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, "where name = #{name}"})
    User selectUserByName(String name);
}

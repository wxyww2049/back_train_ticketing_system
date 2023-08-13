package com.example.trainticket.mapper;

import com.example.trainticket.data.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("insert into user (id, user_name, role, email, password, token, id_code) values (#{id}, #{userName}, #{role}, #{email}, #{password}, #{token}, #{idCode})")
    void addUser(User user);

    @Update("update user set user_name = #{userName}, role = #{role}, email = #{email}, password = #{password}, token = #{token}, id_code = #{idCode} where id = #{id}")
    void updUser(User user);

    @Select("select * from user where email = #{email}")
    User findUserByEmail(String email);

}

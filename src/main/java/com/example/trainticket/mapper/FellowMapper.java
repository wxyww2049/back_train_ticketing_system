package com.example.trainticket.mapper;

import com.example.trainticket.data.po.Fellow;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.data.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FellowMapper {
    @Insert("insert into huqfellow (id, user_name, email, id_code, status) values (#{id}, #{userName}, #{email}, #{idCode}, #{status})")
    void addFellow(Fellow fellow);

    @Update("update huqfellow set user_name = #{userName}, email = #{email}, id_code = #{idCode}, `status` = #{status} where id = #{id}")
    void upFellow(Fellow fellow);

    @Select("select * from huqfellow where `status` = 1 and email = #{email}")
    List<Fellow> getFellowByEmail(String email);

    @Select("select * from huqfellow where `status` = 1 and user_name = #{userName}")
    Fellow getFellowByName(String userName);

    @Update("update huqfellow set `status` = 0 where id = #{id}")
    void deleteFellow(String id);

    @Select("select * from huqfellow where `status` = 1 and id_code = #{idCode} and email = #{email}")
    Fellow getFellowIdCodeAndEmail(String idCode,String email);
}

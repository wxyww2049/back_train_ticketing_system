package com.example.trainticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.trainticket.data.po.Ticket;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TicketMapper extends BaseMapper<Train> {

    @Select("select max(id) from ticket")
    Integer findMaxId();

    @Insert("INSERT INTO ticket (id, status, start_station_code, end_station_code, " +
            "start_station_name, end_station_name, train_no, train_code, is_start, " +
            "is_end, price, seat, start_time, end_time, arrive_day_diff, seat_type," +
            "user_id,id_code,name,order_id,date) " +
            "VALUES (#{id}, #{status}, #{startStationCode}, #{endStationCode}, #{startStationName}, " +
            "#{endStationName}, #{trainNo}, #{trainCode}, #{isStart}, #{isEnd}, #{price}, #{seat}, " +
            "#{startTime}, #{endTime}, #{arriveDayDiff}, #{seatType},#{userId},#{idCode},#{name},#{orderId},#{date})")
    void insertTicket(Ticket ticket);

    @Select("select * from ticket where user_id = #{userId}")
    List<Ticket> getTicketsByUserId(Integer userId);

    @Select("select * from ticket where id = #{id} limit 1")
    Ticket getTicketById(Integer id);

    @Update("update ticket set `status` = #{status} where id = #{id}")
    void updTicketStatus(Integer id, Integer status);

    @Select("select * from ticket where id_code = #{idCode}")
    List<Ticket> getTicketsByIdCode(String idCode);

    @Select("select * from ticket where id_code = #{idCode} and `status` = #{status}")
    List<Ticket>findTicketsByIdCodeAndStatus(String idCode,Integer status);


    @Update("update ticket set `status` = #{status} where order_id = #{orderId}")
    void updStatusByOrderId(Integer orderId,Integer status);

    @Select("select * from ticket where order_id = #{orderId}")
    List<Ticket> findTicketsByOrderId(Integer orderId);
}
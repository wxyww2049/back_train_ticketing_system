package com.example.trainticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.trainticket.data.po.Ticket;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketMapper extends BaseMapper<Train> {

    @Select("select max(id) from ticket")
    Integer findMaxId();

    @Insert("INSERT INTO ticket (id, status, start_station_code, end_station_code, start_station_name, end_station_name, train_no, train_code, is_start, is_end, price, seat, start_time, end_time, arrive_day_diff, seat_type,user_id) " +
            "VALUES (#{id}, #{status}, #{startStationCode}, #{endStationCode}, #{startStationName}, #{endStationName}, #{trainNo}, #{trainCode}, #{isStart}, #{isEnd}, #{price}, #{seat}, #{startTime}, #{endTime}, #{arriveDayDiff}, #{seatType},#{userId})")
    void insertTicket(Ticket ticket);

    @Select("select * from ticket where user_id = #{userId}")
    List<Ticket> getTicketsByUserId(Integer userId);
}

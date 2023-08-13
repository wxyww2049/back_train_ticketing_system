package com.example.trainticket.mapper;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.example.trainticket.data.po.Carriage;
import org.apache.ibatis.annotations.*;

import java.util.BitSet;
import java.util.List;

@Mapper
public interface CarriageMapper {


    @Insert("insert into carriage (train_no,station_no,seat) values (#{trainNo},#{stationNo},#{seat})")
    void insertCarriage(Carriage carriage);

    @Select("select * from carriage where train_no = #{trainNo} and station_no = #{stationNo} limit 1")
    Carriage getCarriage(String trainNo, Integer stationNo);

    @Select("select * from carriage")
    List<Carriage> getAllCarriage();

    @Select("select seat from carriage where train_no = #{trainNo} and station_no = #{stationNo} limit 1")
    BitSet getSeat(String trainNo, Integer stationNo);

    @Update("update carriage set seat = #{seat} where train_no = #{trainNo} and station_no = #{stationNo}")
    void updateSeat(Carriage carriage);
}

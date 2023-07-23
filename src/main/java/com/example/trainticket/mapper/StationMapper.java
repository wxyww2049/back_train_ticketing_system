package com.example.trainticket.mapper;

import com.example.trainticket.data.po.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StationMapper {

    @Select("select * from station")
    List<Station> getAllStations();
}

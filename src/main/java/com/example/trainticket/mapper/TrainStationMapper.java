package com.example.trainticket.mapper;

import com.example.trainticket.data.po.TrainStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TrainStationMapper {
    @Select("select * from train_station")
    List<TrainStation> getAllTrainStations();



}

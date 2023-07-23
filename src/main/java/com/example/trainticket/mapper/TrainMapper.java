package com.example.trainticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.trainticket.data.po.Station;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrainMapper extends BaseMapper<Train> {

    @Select("select * from train_station s where train_no = #{trainNo} order by s.station_no")
    List<TrainStation> getStationsByTrainNo( String trainNo);

    @Select("select * from train  where train_no = #{trainNo} limit 1")
    Train getTrainByTrainNo(String trainNo);

    @Select("select * from train")
    List<Train> getAllTrains();
}

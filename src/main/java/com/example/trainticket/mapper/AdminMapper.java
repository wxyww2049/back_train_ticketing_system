package com.example.trainticket.mapper;

import com.example.trainticket.data.vo.HotCityVo;
import com.example.trainticket.data.vo.ProfitVo;
import com.example.trainticket.data.vo.TrainDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 管理端相关接口，根据需要部分接口已经在数据库中设成视图
 */

@Mapper
public interface AdminMapper {

    @Select("SELECT * from profit_data")
    List<ProfitVo> getProfitData();

    @Select("select * from train_data")
    List<TrainDataVo> getTrainData();

    @Select("select * from hot_city")
    List<HotCityVo> getHotCity();
}

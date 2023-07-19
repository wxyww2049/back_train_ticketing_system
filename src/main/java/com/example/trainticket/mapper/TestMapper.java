package com.example.trainticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.trainticket.data.po.Test;
import com.example.trainticket.data.po.Train;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper  extends BaseMapper<Train> {
    @Select("select * from test")
    List<Test> getAll();

    @Select("select * from rank_ab limit 1")
    Test getTrainByTrainNo(String trainNo);

    @Select("select count(*) from train")
    Integer getCount();
}

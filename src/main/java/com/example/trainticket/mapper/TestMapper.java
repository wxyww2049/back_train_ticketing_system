package com.example.trainticket.mapper;

import com.example.trainticket.data.po.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("select * from test")
    List<Test> getAll();
}

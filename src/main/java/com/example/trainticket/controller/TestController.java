package com.example.trainticket.controller;

import com.alibaba.fastjson.JSON;
import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.service.MainService;
import com.example.trainticket.utils.RedisUtil;
import com.example.trainticket.utils.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.BitSet;

@RestController
public class TestController {

    @Autowired
    MainService mainService;

    @Value("${custom.CARRIAGE_LENGTH}")
    private Integer CARRIAGE_LENGTH;

    @GetMapping("/test")
    Result test() throws UnsupportedEncodingException {

        TrainStation trainStation = new TrainStation();
        trainStation.setStationNo(1);
        trainStation.setStationName("哈尔滨");
        trainStation.setTrainNo("0100000Z1630");
        trainStation.setArriveTime(Time.valueOf("21:15:00"));
        trainStation.setStartTime(Time.valueOf("21:15:00"));
        trainStation.setStationCode(10000);
        trainStation.setTrainCode("Z16");
        trainStation.setWz(30.0);
        trainStation.setM(null);
        RedisUtil.pushTsForStation(trainStation);
        String tmp = SerializationUtil.serialize(1000);
        Integer tmp1 = SerializationUtil.deserialize(tmp,Integer.class);
        return Result.success("test",tmp1);
    }
}

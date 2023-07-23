package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.service.MainService;
import com.example.trainticket.utils.RedisUtil;
import com.example.trainticket.utils.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.Base64;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    MainService mainService;


    @Autowired
    RedisUtil redisUtil;

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

        redisUtil.pushTsForStation(trainStation);

        String tmp = SerializationUtil.serialize(1000);

        Integer tmp1 = SerializationUtil.deserialize(tmp,Integer.class);

        return Result.success("test",tmp1);
    }
}

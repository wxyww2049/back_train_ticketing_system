package com.example.trainticket.service;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Carriage;
import com.example.trainticket.mapper.CarriageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.Configuration;

@Service
public class CarriageService {
    @Autowired
    CarriageMapper carriageMapper;

    public Result getCarriageByTrainNoAndStationNo(String trainNo, Integer StationNo) {
        Carriage res = carriageMapper.getCarriage(trainNo, StationNo);
//        System.out.println(res.getOrginSeat().toString());
        return Result.success("success",res);
    }



}


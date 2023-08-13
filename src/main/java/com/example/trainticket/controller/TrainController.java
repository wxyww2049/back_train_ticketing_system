package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Carriage;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.service.CarriageService;
import com.example.trainticket.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TrainController {
    @Autowired
    TrainService trainService;
    @Autowired
    CarriageService carriageService;

    @GetMapping("/trainDetail")
    Result trainDetail(@RequestParam(value = "trainNo", required = true) String trainNo) {
        return trainService.getTrainDetail(trainNo);
    }
    @GetMapping("/queryTrain")
    Result queryTrain(@RequestParam(value = "startStation", required = true) Integer startStation,
                      @RequestParam(value = "endStation", required = true) Integer endStation,
                      @RequestParam(value = "date", required = true) String date) {
        return trainService.queryTrain(startStation, endStation, date);
    }
    @GetMapping("/queryCarriage")
    Result queryCarriage(@RequestParam(value = "trainNo", required = true) String trainNo,
                         @RequestParam(value = "stationNo", required = true) Integer stationNo) {
        return  carriageService.getCarriageByTrainNoAndStationNo(trainNo,stationNo);
    }


}

package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainController {
    @Autowired
    TrainService trainService;

    @GetMapping("/trainDetail")
    Result trainDetail(@RequestParam(value = "trainNo", required = true) String trainNo) {
        return trainService.getTrainDetail(trainNo);
    }
    @GetMapping("/queryTrain")
    Result queryTrain(@RequestParam(value = "startStation", required = true) String startStation,
                      @RequestParam(value = "endStation", required = true) String endStation,
                      @RequestParam(value = "date", required = true) String date) {
        return trainService.queryTrain(startStation, endStation, date);
    }
}

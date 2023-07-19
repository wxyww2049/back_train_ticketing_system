package com.example.trainticket.service;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.mapper.TrainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {
    @Autowired
    TrainMapper trainMapper;
    public Result getTrainDetail(String trainNo) {
        Train train = trainMapper.getTrainByTrainNo(trainNo);
        if(train == null) {
            return Result.error("未找到对应车次");
        }
        TrainDetail res = new TrainDetail(train);
        res.setStations(trainMapper.getStationsByTrainNo(trainNo));
        return Result.success("success", res);
    }
    public Result queryTrain(String startStation, String endStation, String date) {
//        List<Train> trains = trainMapper.queryTrain(startStation, endStation, date);
//        if(trains == null) {
//            return Result.error("未找到对应车次");
//        }
        return Result.success("success", null);
    }
}

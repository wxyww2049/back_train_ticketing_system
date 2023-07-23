package com.example.trainticket.service;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.data.vo.BaseRoute;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.mapper.TrainMapper;
import com.example.trainticket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {
    @Autowired
    TrainMapper trainMapper;

    @Autowired
    RedisUtil redisUtil;

    public Result getTrainDetail(String trainNo) {
        Train train = trainMapper.getTrainByTrainNo(trainNo);
        if(train == null) {
            return Result.error("未找到对应车次");
        }
        TrainDetail res = new TrainDetail(train);
        res.setStations(trainMapper.getStationsByTrainNo(trainNo));
        return Result.success("success", res);
    }
    public Result queryTrain(Integer startStation, Integer endStation, String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            List<TrainStation>  startStations = redisUtil.getTsForStation(startStation);
            List<TrainStation>  endStations = redisUtil.getTsForStation(endStation);
            //得到列车已经是按照train_no排序后的，只要用双指针求即可。

            int p = 0;
            List<BaseRoute> res = new ArrayList<BaseRoute>();
            for(TrainStation ss : startStations) {
                while(p < endStations.size() && endStations.get(p).getTrainNo().compareTo(ss.getTrainNo()) > 0) {
                    p++;
                }
                if(p >= endStations.size() ) {
                    break;
                }
                if(endStations.get(p).getTrainNo().equals(ss.getTrainNo())
                        && ss.getStationNo() < endStations.get(p).getStationNo()
//                        && ss.getStartTime().compareTo(Time.valueOf(LocalDateTime.now().plusMinutes(30).format(formatter))) > 0
                ) {
                    BaseRoute baseRoute = new  BaseRoute(redisUtil.getTrain(ss.getTrainNo()),ss,endStations.get(p));
//                    baseRoute.setPrice(redisUtil.getPrice(ss.getTrainNo(), ss.getStationNo(), endStations.get(p).getStationNo()));
                    res.add(baseRoute);

                }
            }
            if(res.size() > 0) {
                return Result.success("success", res);
            }
            else {
                return Result.error("未找到对应车次,可以考虑换乘方案");
            }
        }
        catch (Exception e) {
            return Result.error("查询失败");
        }
    }
}

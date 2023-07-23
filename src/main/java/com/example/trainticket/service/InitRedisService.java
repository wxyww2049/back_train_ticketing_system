package com.example.trainticket.service;

import com.example.trainticket.data.po.Station;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.mapper.StationMapper;
import com.example.trainticket.mapper.TrainMapper;
import com.example.trainticket.mapper.TrainStationMapper;
import com.example.trainticket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.zip.CheckedOutputStream;

@Component
public class InitRedisService implements ApplicationRunner {

    @Autowired
    private TrainStationMapper trainStationMapper;
    @Autowired
    private TrainMapper trainMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private RedisUtil redisUtil;

    public void processbar(int cnt,int tot) {
        int tcnt = 100 * cnt / tot;
        StringBuilder tmp = new StringBuilder();
        tmp.append("[");
        for(int i = 0;i < 100;++i) {
            if(i <= tcnt) {
                tmp.append('=');
            }
            else {
                tmp.append(' ');
            }
            if(i == tcnt) tmp.append(">");
        }
        tmp.append("]");
        System.out.print("\r" + tmp );
    }
    private void InitTs() throws Exception {
        List<TrainStation> trainStations = trainStationMapper.getAllTrainStations();
        trainStations.sort(Comparator.comparing(TrainStation::getStationNo));
        System.out.println("====init TsForTrains====");
        int cnt = 0;
        for (TrainStation trainStation : trainStations) {

            processbar(cnt++, trainStations.size());
            redisUtil.pushTsForTrain(trainStation);
        }
        System.out.println("");

        System.out.println("====init TsForStations====");
        //按照列车编号直接插入到车站中，乘车方案可以使用双指针O(n)查询
        cnt = 0;
        trainStations.sort(Comparator.comparing(TrainStation::getTrainNo));
        for (TrainStation trainStation : trainStations) {
            processbar(cnt++, trainStations.size());
            redisUtil.pushTsForStation(trainStation);
        }
        System.out.println("");
    }
    private void InitTrain() throws Exception {
        System.out.println("====init Trains====");
        List<Train> trains = trainMapper.getAllTrains();
        int cnt = 0;
        for(Train train : trains) {
            processbar(cnt++, trains.size());
            redisUtil.setTrain(train);
        }
        System.out.println("");
    }

    private void InitStation() throws Exception {
        System.out.println("====init Stations====");
        List<Station> stations = stationMapper.getAllStations();
        int cnt = 0;
        for(Station station : stations) {
            processbar(cnt++, stations.size());
            redisUtil.setStation(station);
        }
        System.out.println("");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("init redis? (y/n)");
        String s = scanner.nextLine();
        scanner.close();
        if(s.equals("y") || s.equals("Y")) {
            System.out.println("====init redis====");
            try {
                //按照车站编号插入到列车中，便于查询列车信息
                redisUtil.flushAll();
                InitTs();
                InitStation();
                InitTrain();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("====init redis success====");
        }
        System.out.println("server start success!");
    }

}

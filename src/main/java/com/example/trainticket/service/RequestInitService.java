package com.example.trainticket.service;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Carriage;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.mapper.CarriageMapper;
import com.example.trainticket.mapper.TrainMapper;
import com.example.trainticket.mapper.TrainStationMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.BitSet;
import java.util.List;

@Service
public class RequestInitService {
    @Autowired
    private TrainStationMapper trainStationMapper;
    @Autowired
    private CarriageMapper carriageMapper;

    @Value("${custom.CARRIAGE_LENGTH}")
    private int CARRIAGE_LENGTH;

    public Result initSeat() {
        try {
            BitSet bitSetForZTK = new BitSet(CARRIAGE_LENGTH * 11);
            for (int i = 0; i < 2; ++i) {//初始化软座
                bitSetForZTK.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 32 + 1, true);
            }

//            Gson gson = new Gson();
//            System.out.println(bitSetForZTK);

            for (int i = 2; i < 7; ++i) {
                bitSetForZTK.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 110 + 1, true);
            }

            for (int i = 7; i < 10; ++i) {
                bitSetForZTK.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 66 + 1, true);
            }
            Carriage carriageForZTK = new Carriage();
            carriageForZTK.setOriginSeat(bitSetForZTK);


            BitSet bitSetForCD = new BitSet(CARRIAGE_LENGTH * 11);
            for (int i = 0; i < 3; ++i) {//初始化软座
                bitSetForCD.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 52 + 1, true);
            }


            for (int i = 3; i < 10; ++i) {
                bitSetForCD.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 100 + 1, true);
            }




            Carriage carriageForCD = new Carriage();
            carriageForCD.setOriginSeat(bitSetForCD);

            BitSet bitSetForG = new BitSet(CARRIAGE_LENGTH * 11);
            for (int i = 0; i < 2; ++i) {
                bitSetForG.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 24 + 1, true);
            }


            for (int i = 2; i < 7; ++i) {
                bitSetForG.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 100 + 1, true);
            }

            for (int i = 7; i < 10; ++i) {
                bitSetForG.set(i * CARRIAGE_LENGTH + 1, i * CARRIAGE_LENGTH + 52 + 1, true);
            }

//            bitSetForG.set(7 * CARRIAGE_LENGTH + 1, 9 * CARRIAGE_LENGTH + 52 + 1, true);

            Carriage carriageForG = new Carriage();
            carriageForG.setOriginSeat(bitSetForG);


            List<TrainStation> trainStations = trainStationMapper.getAllTrainStations();
            int tot = trainStations.size(),now = 0;
            for (TrainStation trainStation : trainStations) {
                System.out.print("\r==========" + (++now) + "/" + tot + "============");
                if (trainStation.getTrainCode().charAt(0) == 'Z' || trainStation.getTrainCode().charAt(0) == 'T' || trainStation.getTrainCode().charAt(0) == 'K') {
                    carriageForZTK.setTrainNo(trainStation.getTrainNo());
                    carriageForZTK.setStationNo(trainStation.getStationNo());
                    carriageMapper.insertCarriage(carriageForZTK);
                } else if (trainStation.getTrainCode().charAt(0) == 'C' || trainStation.getTrainCode().charAt(0) == 'D') {
                    carriageForCD.setTrainNo(trainStation.getTrainNo());
                    carriageForCD.setStationNo(trainStation.getStationNo());
                    carriageMapper.insertCarriage(carriageForCD);
                } else if (trainStation.getTrainCode().charAt(0) == 'G') {
                    carriageForG.setTrainNo(trainStation.getTrainNo());
                    carriageForG.setStationNo(trainStation.getStationNo());
                    carriageMapper.insertCarriage(carriageForG);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return Result.error("初始化失败");
        }
        return Result.success("初始化成功");
    }
}

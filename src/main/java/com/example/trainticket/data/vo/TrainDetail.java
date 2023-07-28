package com.example.trainticket.data.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.trainticket.data.po.Station;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TrainDetail {
    public TrainDetail(Train train) {
        this.trainNo = train.getTrainNo();
        this.trainClassName = train.getTrainClassName();
        this.startStationName = train.getStartStationName();
        this.endStationName = train.getEndStationName();
        this.trainCode = train.getTrainCode();
    }
    public void InsertStation(TrainStation station) {
        this.stations.add(station);
    }

    private String trainNo;
    private String trainClassName;
    private String startStationName;
    private String endStationName;
    private String trainCode;
    private Integer seatNum;//车厢数

    private List<TrainStation> stations;
}

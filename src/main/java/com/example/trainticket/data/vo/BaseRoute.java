package com.example.trainticket.data.vo;

import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRoute {
    private String trainNo;
    private String trainClassName;
    private String trainCode;
    private String fromStation;
    private Integer fromStationCode;
    private String toStation;
    private Integer toStationCode;
    private Time startTime;
    private Time arriveTime;
    private Double wz;//无座
    private Double  m;//一等座
    private Double  o;//二等座
    private Double  a9;//商务座
    private Double  a1;//硬座
    private Double  a4;//软卧
    private Double  a3;//硬卧
    private boolean isStart;
    private boolean isEnd;


    public BaseRoute(Train train) {
        this.trainNo = train.getTrainNo();
        this.trainClassName = train.getTrainClassName();
        this.trainCode = train.getTrainCode();
    }

    public BaseRoute(Train train, TrainStation start,TrainStation end) {
        this(train);
        this.fromStation = start.getStationName();
        this.toStation = end.getStationName();
        this.fromStationCode = start.getStationCode();
        this.toStationCode = end.getStationCode();
        this.startTime = start.getStartTime();
        this.arriveTime = end.getArriveTime();
        this.isStart = start.getStationName().equals(train.getStartStationName());

        this.isEnd = end.getStationName().equals(train.getEndStationName());
        if(end.getWz() != null) {
            this.wz = end.getWz() - (start.getWz() == null ? 0 : start.getWz());
        }
        if(end.getM() != null) {
            this.m = end.getM() - (start.getM() == null ? 0 : start.getM());
        }
        if(end.getO() != null) {
            this.o = end.getO() - (start.getO() == null ? 0 : start.getO());
        }
        if(end.getA9() != null) {
            this.a9 = end.getA9() - (start.getA9() == null ? 0 : start.getA9());
        }
        if(end.getA1() != null) {
            this.a1 = end.getA1() - (start.getA1() == null ? 0 : start.getA1());
        }
        if(end.getA4() != null) {
            this.a4 = end.getA4() - (start.getA4() == null ? 0 : start.getA4());
        }
        if(end.getA3() != null) {
            this.a3 = end.getA3() - (start.getA3() == null ? 0 : start.getA3());
        }
    }
}

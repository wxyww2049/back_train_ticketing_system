package com.example.trainticket.data.vo;

import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.utils.RedisUtil;
import com.example.trainticket.utils.SerializationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Time;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

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
    private Integer  cntWz;
    private Integer  cntM;
    private Integer  cntO;
    private Integer  cntA9;
    private Integer  cntA1;
    private Integer  cntA4;
    private Integer  cntA3;
    private boolean isStart;//是否是始发站
    private boolean isEnd;//是否是终点站



    public BaseRoute(Train train) {
        this.trainNo = train.getTrainNo();
        this.trainClassName = train.getTrainClassName();
        this.trainCode = train.getTrainCode();
        this.cntA1 = this.cntA3 = this.cntA4 = this.cntA9 = this.cntM = this.cntO = this.cntWz = 0;
    }



    /**
     * 根据车次编号和起止站点编号构造BaseRoute
     * 车票数量计算方式：将途径所有位置and起来，结果中还有位置的此类票数加1。
     * 在每个站点都计算一下无座类（硬座和二等座）的数量，取最小值mn，如果有的无座类位置and起来是1，则让mn--。最终得到的mn就是无座票数量。
     * 无座票数量代表了乘客不能坐下，但是最好情况下可以通过不停换座找到可以坐的位置。在无人占用时，允许无座乘客坐下。
     * */
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

        BitSet ansSeat = null;
        this.cntWz = 120 * 10;

        for(int i = start.getStationNo() + 1;i <= end.getStationNo();i++) {
//            System.out.println("i = " + i);
            BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(train.getTrainNo(), i)).getOrginSeat();
            long flg = train.getSeatTypes();
            int pos = 9 * 120 + 1;
            int tcntWz = 0;
            for(int j = 0;j < 10;++j) {
                long type = flg % 6;
                flg /= 6;
                if(type == 0 || type == 4) {
                    for(int k = pos;k < pos + 120;++k) {
                        if(seat.get(k)) {
                            tcntWz++;
                        }
                    }
                }
                pos -= 120;
            }
            this.cntWz  = Math.min(this.cntWz,tcntWz);
            if(ansSeat == null) {
                ansSeat = seat;
            } else {
                ansSeat.and(seat);
            }
        }

        long flg = train.getSeatTypes();
        int pos = 9 * 120 + 1;
        for(int j = 0;j < 10;++j) {
            long type = flg % 6;
            flg /= 6;
            for(int k = pos;k < pos + 120;++k) {

                assert ansSeat != null;
                if(ansSeat.get(k)) {
                    switch ((int) type) {
                        case 0 -> {
                            this.cntWz--;
                            this.cntA1++;
                        }
                        case 1 -> this.cntA3++;
                        case 2 -> this.cntA4++;
                        case 3 -> this.cntM++;
                        case 4 -> {
                            this.cntO++;
                            this.cntWz--;
                        }
                        case 5 -> this.cntA9++;
                    }
                }
            }
            pos -= 120;
        }
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
    public BaseRoute(Train train,Integer startCode,Integer endCode) {
        this(train);
        List<TrainStation> trainStations = RedisUtil.getTsForTrain(train.getTrainNo());
        TrainStation start = null;
        TrainStation end = null;
        for(TrainStation ts:trainStations) {
            if(ts.getStationCode().equals(startCode)) {
                start = ts;
            }
            if(ts.getStationCode().equals(endCode)) {
                end = ts;
            }
        }
        this.fromStation = start.getStationName();
        this.toStation = end.getStationName();
        this.fromStationCode = start.getStationCode();
        this.toStationCode = end.getStationCode();
        this.startTime = start.getStartTime();
        this.arriveTime = end.getArriveTime();
        this.isStart = start.getStationName().equals(train.getStartStationName());
        this.isEnd = end.getStationName().equals(train.getEndStationName());


        BitSet ansSeat = null;
        this.cntWz = 120 * 10;

//        System.out.println("start = " + start.getStationNo());
//        System.out.println("end = " + end.getStationNo());

        for(int i = start.getStationNo() + 1;i <= end.getStationNo();i++) {
//            System.out.println("i = " + i);
            BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(train.getTrainNo(), i)).getOrginSeat();
            long flg = train.getSeatTypes();
            int pos = 9 * 120 + 1;
            int tcntWz = 0;
            for(int j = 0;j < 10;++j) {
                long type = flg % 6;
                flg /= 6;
                if(type == 0 || type == 4) {
                    for(int k = pos;k < pos + 120;++k) {
                        if(seat.get(k)) {
                            tcntWz++;
                        }
                    }
                }
                pos -= 120;
            }
            this.cntWz  = Math.min(this.cntWz,tcntWz);
            if(ansSeat == null) {
                ansSeat = seat;
            } else {
                ansSeat.and(seat);
            }
        }

        long flg = train.getSeatTypes();
        int pos = 9 * 120 + 1;
        for(int j = 0;j < 10;++j) {
            long type = flg % 6;
            flg /= 6;
            for(int k = pos;k < pos + 120;++k) {

                assert ansSeat != null;
                if(ansSeat.get(k)) {
                    switch ((int) type) {
                        case 0 -> {
                            this.cntWz--;
                            this.cntA1++;
                        }
                        case 1 -> this.cntA3++;
                        case 2 -> this.cntA4++;
                        case 3 -> this.cntM++;
                        case 4 -> {
                            this.cntO++;
                            this.cntWz--;
                        }
                        case 5 -> this.cntA9++;
                    }
                }
            }
            pos -= 120;
        }
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

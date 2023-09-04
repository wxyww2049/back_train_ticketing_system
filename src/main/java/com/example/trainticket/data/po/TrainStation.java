package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("train_station")
public class TrainStation {
    private Integer arriveDayDiff;//抵达日期
    private Time arriveTime;//抵达时间
    private String trainNo;//车次编号
    private String stationName;//车站名称
    private Integer stationNo;//车站抵达编号
    private Time startTime;//出发时间
    private Integer stationCode;//车站代码
    private String trainCode;//车次号
    private Double wz;//无座
    private Double  m;//一等座
    private Double  o;//二等座
    private Double  a9;//商务座
    private Double  a1;//硬座
    private Double  a4;//软卧
    private Double  a3;//硬卧

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainStation that = (TrainStation) o;
        return trainNo.equals(that.trainNo) && stationNo.equals(that.stationNo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(trainNo, stationNo);
    }
}

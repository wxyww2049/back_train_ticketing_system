package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("train_station")
public class TrainStation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer arriveDayDiff;//抵达日期
    private String arriveTime;//抵达时间
    private String trainNo;//车次编号
    private String stationName;//车站名称
    private Integer stationNo;//车站抵达编号
    private String startTime;//出发时间
    private Integer stationCode;//车站代码
    private String trainCode;//车次号
}

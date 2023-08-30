package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.trainticket.utils.SerializationUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ticket")
public class Ticket {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer status;//1: 锁定，2：已支付，3：已退票，4：已取消
    private Integer startStationCode;
    private Integer endStationCode;
    private String startStationName;
    private String endStationName;
    private String trainNo;
    private String trainCode;
    private Integer isStart;
    private Integer isEnd;
    private Double price;
    private Integer seat;
    private Time startTime;
    private Time endTime;
    private Integer arriveDayDiff;
    private Integer seatType;
    private Integer userId;
    private String name;
    private String idCode;
    private Integer orderId;
    private String date;
}

package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("order")
public class Order {
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
    private Time startTime;
    private Time endTime;
    private Integer arriveDayDiff;
    private Integer userId;
    private String OrderTime;
    public Order(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.startStationCode = order.getStartStationCode();
        this.endStationCode = order.getEndStationCode();
        this.startStationName = order.getStartStationName();
        this.endStationName = order.getEndStationName();
        this.trainNo = order.getTrainNo();
        this.trainCode = order.getTrainCode();
        this.isStart = order.getIsStart();
        this.isEnd = order.getIsEnd();
        this.startTime = order.getStartTime();
        this.endTime = order.getEndTime();
        this.arriveDayDiff = order.getArriveDayDiff();
        this.userId = order.getUserId();
    }

    public Order(Ticket ticket) {
        this.status = ticket.getStatus();
        this.startStationCode = ticket.getStartStationCode();
        this.endStationCode = ticket.getEndStationCode();
        this.startStationName = ticket.getStartStationName();
        this.endStationName = ticket.getEndStationName();
        this.trainNo = ticket.getTrainNo();
        this.trainCode = ticket.getTrainCode();
        this.isStart = ticket.getIsStart();
        this.isEnd = ticket.getIsEnd();
        this.startTime = ticket.getStartTime();
        this.endTime = ticket.getEndTime();
        this.arriveDayDiff = ticket.getArriveDayDiff();
        this.userId = ticket.getUserId();
    }
}

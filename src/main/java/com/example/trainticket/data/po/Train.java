package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivilegedExceptionAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("train")
public class Train {
    @TableId(value = "train_no")
    private String trainNo;
    private String trainClassName;
    private String startStationName;
    private String serviceType;
    private String isStart;
    private String endStationName;
    private String trainCode;

    @Override
    public String toString() {
        return "Train{" +
                "trainNo='" + trainNo + '\'' +
                ", trainClassName='" + trainClassName + '\'' +
                ", startStationName='" + startStationName + '\'' +
                ", endStationName='" + endStationName + '\'' +
                ", trainCode='" + trainCode + '\'' +
                '}';
    }
}
package com.example.trainticket.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTicket {
    private String trainNo;
    private Integer fromStationCode;
    private Integer toStationCode;
    private Integer seatType;
    private Integer seatPos;
    private List<SempleUserInfo> fellowers;
}

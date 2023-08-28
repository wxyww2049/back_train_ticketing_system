package com.example.trainticket.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SempleUserInfo {
    String idCode;
    String userName;
    Integer seatPos;
}

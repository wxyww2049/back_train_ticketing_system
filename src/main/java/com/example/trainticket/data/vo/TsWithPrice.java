package com.example.trainticket.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TsWithPrice implements Comparable<TsWithPrice> {
    private String trainNo;
    private  Integer stationCode;
    private  Double price;
    private Time time;

    @Override
    public int compareTo(TsWithPrice other) {
        if(this.stationCode.equals(other.stationCode)) {
            return this.price.compareTo(other.price);
        } else {
            return this.stationCode.compareTo(other.stationCode);
        }
    }
}

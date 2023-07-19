package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("price")
public class Price {
    private Double wz;
    private Double m;
    private Double o;
    private Double a9;
    private Double a1;
    private Double a4;
    private Double a3;
    private String trainNo;
    private Integer fromStationCode;
    private Integer toStationCode;
}

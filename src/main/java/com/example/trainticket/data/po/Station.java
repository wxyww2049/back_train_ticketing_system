package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("station")
public class Station {
    private String abbr;
    private String name;
    private String pinyin;
    private String py;
    private Integer code;
    private Integer areaCode;
    private String areaName;
}

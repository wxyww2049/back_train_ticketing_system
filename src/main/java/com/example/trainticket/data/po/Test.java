package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("train")
public class Test {
//    @TableId(type = IdType.AUTO)
//    private Integer id;
    private String t1;
    private String t2;
    private String t3;
//    private String trainClassName;
//    private String startStationName;

//    @TableField(value = "train_no")
//    private String train_no;
//    private String admin_name;
//    private String adminName;
}

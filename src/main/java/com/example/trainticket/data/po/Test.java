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
@TableName("test")
public class Test {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String t1;
    private String t2;
    private String t3;
}

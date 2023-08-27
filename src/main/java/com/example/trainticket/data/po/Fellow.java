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
@TableName("huqfellow")
public class Fellow {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String email;
    private String status;
    private String idCode;
    public Fellow(String userName, String email, String status, String idCode) {
        this.userName = userName;
        this.email = email;
        this.status = status;
        this.idCode = idCode;
    }
    public Fellow(String idCode,String userName) {
        this.userName = userName;
        this.idCode = idCode;
        this.status = "1";
    }
}

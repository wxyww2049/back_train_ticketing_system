package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    private String id;
    private String userName;
    private String role;
    private String email;
    private String password;
    private String token;
    private String idCode;
}

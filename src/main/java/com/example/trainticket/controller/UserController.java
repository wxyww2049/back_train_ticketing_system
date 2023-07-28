package com.example.trainticket.controller;

import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @PostMapping("/signup")
    public Result signup(@Param("username") String username,
                         @Param("password") String password,
                         @Param("email") String email) {
        return Result.success();
    }
    @PostMapping("/login")
    public Result login(@Param("username") String username,
                        @Param("password") String password) {
        return Result.success();
    }

    /**
     * 生成管理员账号
     */
    @Auth(identify = {"ROOT"})
    @PostMapping("/generateAdmin")
    public Result generateAdmin(@Param("username") String username,
                                @Param("password") String password,
                                @Param("email") String email) {
        return Result.success();
    }

    @PostMapping("/changePwd")
    public Result changePwd(@Param("username") String username,
                            @Param("password") String password,
                            @Param("newPassword") String newPassword) {
        return Result.success();
    }
}

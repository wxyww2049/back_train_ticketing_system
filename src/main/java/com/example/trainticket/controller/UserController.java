package com.example.trainticket.controller;

import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import com.example.trainticket.bean.StatusCode;
import com.example.trainticket.data.po.User;
import com.example.trainticket.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Result signup(@RequestBody User data) {
        log.info("start signup for user: " + data.getUserName() + " email: " + data.getEmail());
        return userService.signup(data.getUserName(), data.getPassword(), data.getEmail());
//        return Result.success("注册成功");
    }
    @PostMapping("/login")
    public Result login(@RequestBody User data) {
        return userService.login(data.getEmail(), data.getPassword());
    }

    /**
     * 生成管理员账号
     */
    @Auth(identify = {"ROOT"})
    @PostMapping("/generateAdmin")
    public Result generateAdmin(@RequestBody User data) {
        return userService.generateAdmin(data.getUserName(), data.getPassword(), data.getEmail());
    }

    @PostMapping("/changePwd")
    public Result changePwd(@RequestBody Map<String,Object> data) {
        try {
            return userService.changePwd((String)data.get("email"), (String)data.get("password"), (String)data.get("newPassword"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error(StatusCode.PARAM_NOT_COMPLETE);
        }
    }

    @Auth(identify = {"USER","ROOT","ADMIN"})
    @PostMapping("/changeIdCode")
    public Result changeIdCode(@RequestBody Map<String,Object> data, @RequestHeader("token") String token) {
        try {
            return userService.changeIdCode(token,(String)data.get("idCode"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error(StatusCode.PARAM_NOT_COMPLETE);
        }
    }
}

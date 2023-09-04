package com.example.trainticket.controller;

import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import com.example.trainticket.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    AdminMapper adminMapper;

    @Auth(identify = {"ROOT","ADMIN"})
    @GetMapping("/getProfitData")
    public Result getProfitData() {
        try {
            return Result.success("获取成功",adminMapper.getProfitData());
        }
        catch(Exception e) {
            e.printStackTrace();
            return Result.error("获取失败");
        }
    }
    @Auth(identify = {"ROOT","ADMIN"})
    @GetMapping("/getTrainData")
    public Result getTrainData() {
        try {
            return Result.success("获取成功",adminMapper.getTrainData());
        }
        catch(Exception e) {
            e.printStackTrace();
            return Result.error("获取失败");
        }
    }
    @Auth(identify = {"ROOT","ADMIN"})
    @GetMapping("/getHotCity")
    public Result getHotCity() {
        try {
            return Result.success("获取成功",adminMapper.getHotCity());
        }
        catch(Exception e) {
            e.printStackTrace();
            return Result.error("获取失败");
        }
    }
}

package com.example.trainticket.controller;

import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import com.example.trainticket.bean.StatusCode;
import com.example.trainticket.data.po.Fellow;
import com.example.trainticket.data.po.User;
import com.example.trainticket.service.FellowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class FellowController {
    @Autowired
    private FellowService fellowService;
    @GetMapping("/getFellow")
    Result queryTrain(@RequestHeader("token") String token) {
        return fellowService.getAllFellows(token);
    }

    @PostMapping("/addFellow")
    public Result addFellow(@RequestBody Fellow data, @RequestHeader("token") String token) {
        log.info("start add fellow: " +data);
        return fellowService.insert(data.getUserName(), data.getIdCode(), token);
    }



    @PostMapping("/deleteFellow")
    public Result deleteFellow(@RequestBody Fellow data, @RequestHeader("token") String token) {
        log.info("start delete fellow: " +data);
        return fellowService.delete(String.valueOf(data.getId()), token);
    }

    @PostMapping("/updateFellow")
    public Result updateFellow(@RequestBody Fellow data, @RequestHeader("token") String token) {
        log.info("start update fellow: " +data);
        return fellowService.update(String.valueOf(data.getId()), data.getUserName(), data.getIdCode(), token);
    }

//    @PostMapping("/updateFellow")
//    public Result updateFellow(@RequestBody Map<String,Object> data, @RequestHeader("token") String token) {
//        log.info("start update fellow: " +data);
//        try {
//            return fellowService.update((String)data.get("id"), (String)data.get("userName"), (String)data.get("idCode"), token);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return Result.error(StatusCode.PARAM_NOT_COMPLETE);
//        }
//    }

}

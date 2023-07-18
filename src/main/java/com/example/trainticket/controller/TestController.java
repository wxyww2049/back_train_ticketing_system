package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import com.example.trainticket.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    MainService mainService;

    @GetMapping("/test")
    Result test() {
        return Result.success("test",mainService.getAll());
    }
}

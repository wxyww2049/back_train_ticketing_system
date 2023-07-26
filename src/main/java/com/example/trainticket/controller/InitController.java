package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import com.example.trainticket.service.RequestInitService;
import io.protostuff.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private RequestInitService requestInitService;

    @GetMapping("/seat")
    public Result initSeat(){

        return requestInitService.initSeat();
    }
}

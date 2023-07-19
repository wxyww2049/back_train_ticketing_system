package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    private final RedisTemplate redisTemplate;

    public RedisController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @GetMapping("save")
    public Result save(String key, String value){
        redisTemplate.opsForValue().set("t1", "t2");
        return Result.success();
    }
}


package com.example.trainticket.controller;

import com.example.trainticket.bean.RedisConfig;
import com.example.trainticket.bean.Result;
import com.example.trainticket.utils.Assert;
import com.example.trainticket.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@RestController
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/redis")
    public Result redis() {
        Jedis jedis = redisUtil.getJedis();
        return Result.success("redis",jedis.get("wxy"));
    }

}


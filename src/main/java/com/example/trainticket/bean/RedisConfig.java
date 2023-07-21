package com.example.trainticket.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "value")
@Data
public class RedisConfig {
    private String redisAddress;
    private  Integer redisPort;
    private String redisUser;
    private String redisAuth;
}

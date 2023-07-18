package com.example.trainticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.trainticket.mapper")
public class TrainTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainTicketApplication.class, args);
    }

}

package com.example.trainticket.controller;

import com.example.trainticket.bean.Result;
import com.example.trainticket.service.TicketService;
import com.example.trainticket.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketController {
    @Autowired
    TicketService ticketService;

    @Autowired
    JwtUtil jwtUtil;
    @GetMapping("/queryTicket")
    Result queryTicket(@RequestHeader("token") String token) {
        return ticketService.queryTicket(jwtUtil.verifyToken(token).get("id").asInt());
    }

    @GetMapping("/queryOrder")
    Result queryOrder(@RequestHeader("token") String token) {
        return ticketService.queryOrder(jwtUtil.verifyToken(token).get("id").asInt());
    }

    @PostMapping("/refundTicket")
    Result refundTicket(@RequestHeader("token") String token,@RequestParam(value = "orderId",required = true) Integer OrderId) {
        return ticketService.refundTicket(jwtUtil.verifyToken(token).get("id").asInt(),OrderId);
    }

}

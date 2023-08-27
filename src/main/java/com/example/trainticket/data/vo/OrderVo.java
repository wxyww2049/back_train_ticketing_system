package com.example.trainticket.data.vo;

import com.example.trainticket.data.po.Order;
import com.example.trainticket.data.po.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo extends Order {
    List<Ticket> tickets;
    public OrderVo(Order order) {
        super(order);
    }
}

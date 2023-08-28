package com.example.trainticket.service;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Order;
import com.example.trainticket.data.po.User;
import com.example.trainticket.data.vo.OrderVo;
import com.example.trainticket.mapper.OrderMapper;
import com.example.trainticket.mapper.TicketMapper;
import com.example.trainticket.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserMapper userMapper;
    public Result queryTicket(Integer userId) {
        try {
            User user = userMapper.findUserById(userId);
            if(user.getIdCode() == null) {
                return Result.error("请先完善个人信息");
            }
            System.out.println(user.getIdCode());

            return Result.success("success",ticketMapper.findTicketsByIdCodeAndStatus(user.getIdCode(),2));
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
    public Result queryOrder(Integer userId) {
        try {
            List<Order> orderList = orderMapper.findOrdersByUserId(userId);
            List<OrderVo> orderVoList = new ArrayList<>();
            for(Order order : orderList) {
                OrderVo orderVo = new OrderVo(order);
                orderVo.setTickets(ticketMapper.findTicketsByOrderId(order.getId()));
                orderVoList.add(orderVo);
            }
            return Result.success("success",orderVoList);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
    @Transactional
    public void updOrderStatus(Integer orderId,Integer status) {
        orderMapper.updOrderStatus(orderId,status);
        ticketMapper.updStatusByOrderId(orderId,status);
    }

    public Result refundTicket(Integer usrId,Integer OrderId) {
        try {

            Order order = orderMapper.findOrderByOrderId(OrderId);
            if(!order.getUserId().equals(usrId)) {
                System.out.println(OrderId+" "+usrId+"=======================");
                return Result.error("非法操作");
            }
            updOrderStatus(OrderId,3);
            return Result.success("success");
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("退票失败");
        }
    }

}

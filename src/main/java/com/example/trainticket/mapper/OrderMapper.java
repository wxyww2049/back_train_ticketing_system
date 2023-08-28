package com.example.trainticket.mapper;

import com.example.trainticket.data.po.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("select max(id) from orders")
    Integer findMaxId();

    @Insert("insert into orders(id,status,start_station_code," +
            "end_station_code,start_station_name,end_station_name," +
            "train_no,train_code,is_start,is_end,price,start_time," +
            "end_time,arrive_day_diff,user_id) values (#{id},#{status}," +
            "#{startStationCode},#{endStationCode},#{startStationName}," +
            "#{endStationName},#{trainNo},#{trainCode},#{isStart},#{isEnd}," +
            "#{price},#{startTime},#{endTime},#{arriveDayDiff},#{userId})")
    void addOrder(Order order);

    @Select("select * from orders where id = #{id}")
    Order getOrderById(Integer id);

    @Update("update orders set status = #{status} where id = #{id}")
    void updOrderStatus(Integer id, Integer status);

    @Select("select * from orders where user_id = #{userId}")
    List<Order> findOrdersByUserId(Integer userId);

    @Select("select * from orders where id = #{orderId}")
    Order findOrderByOrderId(Integer orderId);
}

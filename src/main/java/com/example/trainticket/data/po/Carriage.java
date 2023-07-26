package com.example.trainticket.data.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.example.trainticket.utils.SerializationUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.BitSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Accessors(chain = true)
@TableName("carriage")
public class Carriage {
    private String trainNo;
    private Integer stationNo;

//    @TableField(typeHandler = FastjsonTypeHandler.class)
    private BitSet seat;



    public Carriage(String trainNo, Integer stationNo,String seat) {
        this.trainNo = trainNo;
        this.stationNo = stationNo;
        this.setSeat(seat);
    }
    public void setOriginSeat(BitSet seat) {
        this.seat = seat;
    }
    public BitSet getOrginSeat() {
        return this.seat;
    }
    public String getSeat() {
        Gson gson = new Gson();
        return gson.toJson(this.seat);
    }
    public void setSeat(String seat) {
        Gson gson = new Gson();
        this.seat = gson.fromJson(seat, new TypeToken<BitSet>(){}.getType());
    }
    public void setOneSeat(int index, boolean value) {
        this.seat.set(index,value);
    }
    


}

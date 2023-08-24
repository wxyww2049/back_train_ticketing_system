package com.example.trainticket.utils;


import com.example.trainticket.adapter.SqlTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;

@Slf4j
public class SerializationUtil {

    // 避免protostuff每次序列化都重新申请Buffer空间
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Time.class, new SqlTimeAdapter())
            .create();

    /**
     * 序列化对象到字节数组
     */
    public static  String serialize(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 序列化字节数组到指定类型对象
     */
    public static<T> T deserialize(String json,Class<T> clazz) {
        return gson.fromJson(json,clazz);
    }

}

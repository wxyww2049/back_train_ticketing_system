package com.example.trainticket.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象序列化工具类
 */
@Slf4j
public class SerializationUtil {

    // 避免protostuff每次序列化都重新申请Buffer空间
    private static LinkedBuffer buffer = LinkedBuffer.allocate();

    /**
     * 序列化对象到字节数组
     */
    public static <T> String serialize(T obj, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return new String(data);
    }

    /**
     * 但序列化字节数组到指定类型对象
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }

}

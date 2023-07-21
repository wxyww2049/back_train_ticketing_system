package com.example.trainticket.utils;

import com.example.trainticket.bean.RedisConfig;
import com.example.trainticket.data.po.TrainStation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisUtil {

    @Autowired
    private RedisConfig redisConfig;

    private final JedisPoolConfig poolConfig;
    private JedisPool jedisPool;

    public RedisUtil() {
        this.poolConfig = new JedisPoolConfig();
        this.poolConfig.setMaxIdle(10);
        this.poolConfig.setMaxTotal(10);
        this.poolConfig.setMaxWait(Duration.ofMillis(1000));
    }

    @PostConstruct
    public void generatePool(){
        if(Assert.isEmpty(redisConfig.getRedisUser()) && Assert.isEmpty(redisConfig.getRedisAuth())){
            this.jedisPool = new JedisPool(this.poolConfig,
                    redisConfig.getRedisAddress(),
                    redisConfig.getRedisPort());
        }else if(Assert.isEmpty(redisConfig.getRedisUser())) {
            this.jedisPool = new JedisPool(this.poolConfig,
                    redisConfig.getRedisAddress(),
                    redisConfig.getRedisPort(),
                    3000,
                    redisConfig.getRedisAuth());
        }
        else {
            this.jedisPool = new JedisPool(this.poolConfig,
                    redisConfig.getRedisAddress(),
                    redisConfig.getRedisPort(),
                    redisConfig.getRedisUser(),
                    redisConfig.getRedisAuth());
        }
    }
    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public <T> boolean setObj(String key,T obj,Class<T> clazz) {
        Jedis jedis = this.getJedis();
        try {
            String value = SerializationUtil.serialize(obj,clazz);
            jedis.set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }

    public <T> T getObj(String key,Class<T> clazz) {
        Jedis jedis = this.getJedis();
        try {
            String value = jedis.get(key);
            if(value == null) return null;
            return SerializationUtil.deserialize(value.getBytes(),clazz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }

    /**
     * 将ts映射到每辆列车
     */
    public boolean pushTsForTrain(TrainStation trainStation) {
        Jedis jedis = this.getJedis();
        try {
            jedis.lpush("tsForTrain" + trainStation.getTrainNo(),SerializationUtil.serialize(trainStation,TrainStation.class));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }
    public List<TrainStation> getTsForTrain(String trainNo) {
        Jedis jedis = this.getJedis();
        try {
            List<String> list = jedis.lrange("tsForTrain" + trainNo,0,-1);
            if(list == null) return null;
            List<TrainStation> trainStations = new ArrayList<TrainStation>();
            for(String s : list){
                trainStations.add(SerializationUtil.deserialize(s.getBytes(),TrainStation.class));
            }
            return trainStations;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }

    /**
     * 将ts映射到每个车站
     */
    public boolean pushTsForStation(TrainStation trainStation) {
        Jedis jedis = this.getJedis();
        try {
            jedis.lpush("tsForStation" + trainStation.getStationCode(),SerializationUtil.serialize(trainStation,TrainStation.class));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }
    public List<TrainStation> getTsForStation(String stationCode) {
        Jedis jedis = this.getJedis();
        try {
            List<String> list = jedis.lrange("tsForStation" + stationCode,0,-1);
            if(list == null) return null;
            List<TrainStation> trainStations = new ArrayList<TrainStation>();
            for(String s : list){
                trainStations.add(SerializationUtil.deserialize(s.getBytes(),TrainStation.class));
            }
            return trainStations;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }


}

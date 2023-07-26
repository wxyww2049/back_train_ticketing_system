package com.example.trainticket.utils;

import com.example.trainticket.bean.RedisConfig;
import com.example.trainticket.data.po.Carriage;
import com.example.trainticket.data.po.Station;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.mapping.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class  RedisUtil {

    @Autowired
    private RedisConfig redisConfig;

    private static JedisPool jedisPool;



    @PostConstruct
    public void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxWait(Duration.ofMillis(1000));
        if(Assert.isEmpty(redisConfig.getRedisUser()) && Assert.isEmpty(redisConfig.getRedisAuth())){
            jedisPool = new JedisPool(poolConfig,
                    redisConfig.getRedisAddress(),
                    redisConfig.getRedisPort());
        }else if(Assert.isEmpty(redisConfig.getRedisUser())) {
            jedisPool = new JedisPool(poolConfig,
                    redisConfig.getRedisAddress(),
                    redisConfig.getRedisPort(),
                    3000,
                    redisConfig.getRedisAuth());
        }
        else {
            jedisPool = new JedisPool(poolConfig,
                    redisConfig.getRedisAddress(),
                    redisConfig.getRedisPort(),
                    redisConfig.getRedisUser(),
                    redisConfig.getRedisAuth());
        }

    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void flushAll() {
        try(Jedis jedis = getJedis();) {
            jedis.flushAll();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean setObj(String key,Object obj) {
        try(Jedis jedis = getJedis();) {
            String value = SerializationUtil.serialize(obj);
            jedis.set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public <T> T getObj(String key,Class<T> clazz) {

        try(Jedis jedis = getJedis();) {
            String value = jedis.get(key);
            if(value == null) return null;
            return SerializationUtil.deserialize(value,clazz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存一辆列车
     */
    public static boolean setTrain(Train train) {
        try(Jedis jedis = getJedis();) {
            jedis.set("train:" + train.getTrainNo(),SerializationUtil.serialize(train));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取一辆列车
     */
    public static  Train getTrain(String trainNo) {

        try (Jedis jedis = getJedis();) {
            String value = jedis.get("train:" + trainNo);
            if (value == null) return null;
            return SerializationUtil.deserialize(value, Train.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存车站
     */

    public static boolean setStation(Station station) {
        try(Jedis jedis = getJedis();) {
            jedis.set("station:" + station.getCode(),SerializationUtil.serialize(station));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取车站
     */

    public static Station getStation(String stationCode) {

        try(Jedis jedis = getJedis();) {
            String value = jedis.get("station:" + stationCode);
            if(value == null) return null;
            return SerializationUtil.deserialize(value,Station.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将ts映射到每辆列车
     */
    public static boolean pushTsForTrain(TrainStation trainStation) {
        try(Jedis jedis = getJedis();) {
            jedis.lpush("tsForTrain" + trainStation.getTrainNo(),SerializationUtil.serialize(trainStation));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static List<TrainStation> getTsForTrain(String trainNo) {

        try(Jedis jedis = getJedis();) {
            List<String> list = jedis.lrange("tsForTrain" + trainNo,0,-1);
            if(list == null) return null;
            List<TrainStation> trainStations = new ArrayList<TrainStation>();
            for(String s : list){
                trainStations.add(SerializationUtil.deserialize(s,TrainStation.class));
            }
            return trainStations;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将ts映射到每个车站
     */
    public static boolean pushTsForStation(TrainStation trainStation) {
        try(Jedis jedis = getJedis();) {
            jedis.lpush("tsForStation" + trainStation.getStationCode(),SerializationUtil.serialize(trainStation));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新Carriage
     */
    public static boolean updCarriage(Carriage carriage) {
        try(Jedis jedis = getJedis();) {
            jedis.set("carriage:" + carriage.getTrainNo() + ":" + carriage.getStationNo(),carriage.getSeat());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取Carriage
     */
    public static Carriage getCarriage(String trainNo,Integer stationNo) {

        try(Jedis jedis = getJedis();) {
            String value = jedis.get("carriage:" + trainNo + ":" + stationNo);
            if(value == null) return null;
            return new Carriage(trainNo,stationNo,value);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<TrainStation> getTsForStation(Integer stationCode) {

        try(Jedis jedis = getJedis();) { //java7新特性，try-with-resource
            List<String> list = jedis.lrange("tsForStation" + stationCode,0,-1);
            if(list == null) return null;
            List<TrainStation> trainStations = new ArrayList<TrainStation>();
            for(String s : list){
                trainStations.add(SerializationUtil.deserialize(s,TrainStation.class));
            }
            return trainStations;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

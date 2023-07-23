package com.example.trainticket.utils;

import com.example.trainticket.bean.RedisConfig;
import com.example.trainticket.data.po.Station;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
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

    public boolean flushAll() {
        try(Jedis jedis = this.getJedis();) {
            jedis.flushAll();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setObj(String key,Object obj) {
        try(Jedis jedis = this.getJedis();) {
            String value = SerializationUtil.serialize(obj);
            jedis.set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public <T> T getObj(String key,Class<T> clazz) {

        try(Jedis jedis = this.getJedis();) {
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
    public boolean setTrain(Train train) {
        try(Jedis jedis = this.getJedis();) {
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
    public Train getTrain(String trainNo) {

        try (Jedis jedis = this.getJedis();) {
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

    public boolean setStation(Station station) {
        try(Jedis jedis = this.getJedis();) {
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

    public Station getStation(String stationCode) {

        try(Jedis jedis = this.getJedis();) {
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
    public boolean pushTsForTrain(TrainStation trainStation) {
        try(Jedis jedis = this.getJedis();) {
            jedis.lpush("tsForTrain" + trainStation.getTrainNo(),SerializationUtil.serialize(trainStation));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<TrainStation> getTsForTrain(String trainNo) {

        try(Jedis jedis = this.getJedis();) {
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
    public boolean pushTsForStation(TrainStation trainStation) {
        try(Jedis jedis = this.getJedis();) {
            jedis.lpush("tsForStation" + trainStation.getStationCode(),SerializationUtil.serialize(trainStation));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<TrainStation> getTsForStation(Integer stationCode) {

        try(Jedis jedis = this.getJedis();) { //java7新特性，try-with-resource
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

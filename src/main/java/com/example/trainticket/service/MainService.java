package com.example.trainticket.service;

import com.example.trainticket.data.po.Test;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MainService {
    @Autowired
    TestMapper testMapper;
    public List<Test> getAll() {
        return testMapper.getAll();
    }
    public Test getTrain(String trainNo) {
        Test res = testMapper.getTrainByTrainNo(trainNo);
        System.out.println(res);
        return res;
    }

}

package com.example.trainticket.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.AlipayConfig;
import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Alipay;
import com.example.trainticket.data.po.Carriage;
import com.example.trainticket.data.po.Fellow;
import com.example.trainticket.data.vo.OrderTicket;
import com.example.trainticket.data.vo.SempleUserInfo;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.service.CarriageService;
import com.example.trainticket.service.TrainService;
import com.example.trainticket.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class TrainController {
    @Autowired
    TrainService trainService;
    @Autowired
    CarriageService carriageService;

    @Autowired
    JwtUtil jwtUtil;



    @GetMapping("/trainDetail")
    Result trainDetail(@RequestParam(value = "trainNo", required = true) String trainNo) {
        return trainService.getTrainDetail(trainNo);
    }
    @GetMapping("/queryTrain")
    Result queryTrain(@RequestParam(value = "startStation", required = true) Integer startStation,
                      @RequestParam(value = "endStation", required = true) Integer endStation,
                      @RequestParam(value = "date", required = true) String date) {
        return trainService.queryTrain(startStation, endStation, date);
    }

    @Auth(identify = {"USER","ROOT","ADMIN"})
    @PostMapping("/queryTransfer")
    public Result queryTransfer(@RequestParam(value = "startStation", required = true) Integer startStation,
                                @RequestParam(value = "endStation", required = true) Integer endStation,
                                @RequestParam(value = "date", required = true) String date) {
        return trainService.queryTransfer(startStation, endStation, date);
    }
    @GetMapping("/queryCarriage")
    Result queryCarriage(@RequestParam(value = "trainNo", required = true) String trainNo,
                         @RequestParam(value = "stationNo", required = true) Integer stationNo) {
        return  carriageService.getCarriageByTrainNoAndStationNo(trainNo,stationNo);
    }
    @PostMapping("/buyTicket")
    Result buyTicket(@RequestHeader("token") String token, @RequestBody OrderTicket params) {
        try {

            return  trainService.buyTicket(jwtUtil.verifyToken(token).get("id").asInt(),params.getTrainNo(),
                    params.getFromStationCode(),params.getToStationCode(),params.getSeatType(),
                    params.getFellowers(),params.getDate());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("购票失败");
        }
    }

    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    String pay(@RequestParam(value = "id",required = true) Integer ticket_id)  {
        return trainService.payForTicket(ticket_id).getMessage();
    }
    @PostMapping("/payCallback")
    String payCallback(@RequestParam("out_trade_no") String trade_no) {
//        //获取支付宝POST过来反馈信息
//        log.info("支付宝回调");
//        Map< String , String > params = new HashMap< String , String >();
//        Map requestParams = request.getParameterMap();
//        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
//            String name = (String)iter.next();
//            String[] values = (String [])requestParams.get(name);
//            String valueStr = "";
//            for(int i = 0;i < values.length;i ++ ){
//                valueStr =  (i==values.length-1)?valueStr + values [i]:valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。
//            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put (name,valueStr);
//        }
//        log.info(JSON.toJSONString(params));
        if(trainService.payCallback(trade_no))
            return "success";
        else
            return "fail";
    }
}

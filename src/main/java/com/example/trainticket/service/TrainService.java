package com.example.trainticket.service;

import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.trainticket.bean.AlipayConfig;
import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.*;
import com.example.trainticket.data.vo.BaseRoute;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.mapper.CarriageMapper;
import com.example.trainticket.mapper.TicketMapper;
import com.example.trainticket.mapper.TrainMapper;
import com.example.trainticket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.constant.Constable;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TrainService {
    @Autowired
    TrainMapper trainMapper;
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    CarriageMapper carriageMapper;

    @Autowired
    AlipayConfig alipayConfig;

    private final AlipayClient alipayClient;

    public TrainService(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }
    public Result getTrainDetail(String trainNo) {
        Train train = trainMapper.getTrainByTrainNo(trainNo);
        if(train == null) {
            return Result.error("未找到对应车次");
        }
        TrainDetail res = new TrainDetail(train);
        res.setStations(trainMapper.getStationsByTrainNo(trainNo));
        return Result.success("success", res);
    }
    public Result queryTrain(Integer startStation, Integer endStation, String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            List<TrainStation>  startStations = RedisUtil.getTsForStation(startStation);
            List<TrainStation>  endStations = RedisUtil.getTsForStation(endStation);
            // 得到列车已经是按照train_no排序后的，只要用双指针求即可。
//            System.out.println("======" + startStations.size() + "=======");
            int p = 0;
            List<BaseRoute> res = new ArrayList<BaseRoute>();
            for(TrainStation ss : startStations) {

                while(p < endStations.size() && endStations.get(p).getTrainNo().compareTo(ss.getTrainNo()) > 0) {
                    p++;
                }

                if(p >= endStations.size() ) {
                    break;
                }
                if(endStations.get(p).getTrainNo().equals(ss.getTrainNo())
                        && ss.getStationNo() < endStations.get(p).getStationNo()
//                        && ss.getStartTime().compareTo(Time.valueOf(LocalDateTime.now().plusMinutes(30).format(formatter))) > 0
                ) {
                    BaseRoute baseRoute = new  BaseRoute(RedisUtil.getTrain(ss.getTrainNo()),ss,endStations.get(p));
//                    baseRoute.setPrice(redisUtil.getPrice(ss.getTrainNo(), ss.getStationNo(), endStations.get(p).getStationNo()));
                    res.add(baseRoute);
                }
            }
            if(res.size() > 0) {
                return Result.success("success", res);
            }
            else {
                return Result.error("未找到对应车次,可以考虑换乘方案");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
    boolean checkSeat(int pos, int type,int num) {//座位编号，座位类型，期望位置
        if(type == 0) {
            return pos % 5 == num;
        }
        else if(type == 1) {
            return pos % 3 == num;
        }
        else if(type == 2) {
            return pos % 2 == num;
        }
        else if(type == 3) {
            return pos % 4 == num;
        }
        else if(type == 4) {
            return pos % 5 == num;
        }
        else if(type == 5) {
            return pos % 3 == num;
        }
        return false;
    }
    public Result payForTicket(Integer ticket_id) {
        try {
            Ticket ticket = ticketMapper.getTicketById(ticket_id);
            if(ticket == null) {
                return Result.error("未找到对应车票");
            }
            if(ticket.getStatus() == 2) {
                return Result.error("该车票已经支付");
            }
            else if(ticket.getStatus() == 3) {
                return Result.error("该车票已经退票");
            }
            else if(ticket.getStatus() == 4) {
                return Result.error("该车票已经取消");
            }
            Alipay alipay = new Alipay(
                    "test2" + Integer.toString(ticket.getId()),
                    ticket.getPrice(),
                    "从" + ticket.getStartStationName() + "到" + ticket.getEndStationName() + "的" + ticket.getTrainCode() + "次列车",
                     ticket.getTrainNo() + ticket.getStartStationCode() + ticket.getEndStationCode(),
                    "FAST_INSTANT_TRADE_PAY",
                    "10m");

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setBizContent(JSON.toJSONString(alipay));
            System.out.println("====" + alipayConfig.getNotifyUrl() + "====" + alipayConfig.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());//支付成功后发送异步消息的地址（发送到后端）
            alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());//用户支付成功后跳转到的页面（跳转到前端）
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return Result.success(result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("支付失败");
        }
    }
    public Boolean payCallback(Map<String,String> params) {
        try {
            //校验是否是来自支付宝的通知
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean flag = AlipaySignature.rsaCheckV1 (params,alipayConfig.getAlipayPublicKey(), "UTF-8","RSA2");
            if(flag) {
                ticketMapper.updTicketStatus(Integer.parseInt(params.get("out_trade_no")),2);
                return true;
            }
            else return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Result insertTicket(Ticket ticket,int pos,int fromNo,int toNo,BitSet ansSeat,long seatTypes) {
        if(pos == -1) {
            for(int i = fromNo + 1;i <= toNo;++i) {
                BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(ticket.getTrainNo(), i)).getOrginSeat();
                long flg = seatTypes;
                int tpos = 9 * 120 + 1;
                for(int j = 0;j < 10;++j) {
                    long type = flg % 6;
                    flg /= 6;
                    boolean flag = false;
                    if(type == 0 || type == 4) {
                        for(int k = tpos;k < tpos + 120;++k) {
                            if(seat.get(k) && !ansSeat.get(k)) {
                                seat.set(k,false);
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag) break;
                    tpos -= 120;
                }
                carriageMapper.updateSeat(new Carriage(ticket.getTrainNo(), i, seat));
                RedisUtil.updCarriage(new Carriage(ticket.getTrainNo(), i, seat));
            }
        }
        else {
            for(int i = fromNo + 1;i <= toNo;i++) {
                BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(ticket.getTrainNo(), i)).getOrginSeat();
                seat.set(pos,false);
                carriageMapper.updateSeat(new Carriage(ticket.getTrainNo(), i, seat));
                RedisUtil.updCarriage(new Carriage(ticket.getTrainNo(), i, seat));
            }
        }

        Integer tid = ticketMapper.findMaxId();
        if(tid == null) tid = 0;
        ticket.setId(tid + 1);
        ticketMapper.insertTicket(ticket);
        return Result.success("success", ticket);
    }

    public Result buyTicket(Integer userId,String trainNo, Integer fromStationCode,Integer toStationCode,Integer seatType,Integer seatPos) {//seatType为-1表示无座票
        try {
            List<TrainStation> trainStations = RedisUtil.getTsForTrain(trainNo);
            TrainStation fromStation = null;
            TrainStation toStation = null;
            assert trainStations != null;
            for(TrainStation trainStation : trainStations) {
                if(trainStation.getStationCode().equals(fromStationCode)) {
                    fromStation = trainStation;
                }
                if(trainStation.getStationCode().equals(toStationCode)) {
                    toStation = trainStation;
                }
            }
            if(fromStation == null || toStation == null) {
                return Result.error("购票失败");
            }
            Train train = RedisUtil.getTrain(trainNo);
            BitSet ansSeat = null;

            for(int i = fromStation.getStationNo() + 1;i <= toStation.getStationNo();i++) {
                BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(trainNo, i)).getOrginSeat();
                if(ansSeat == null) {
                    ansSeat = seat;
                } else {
                    ansSeat.and(seat);
                }
            }
            if(seatType == -1) {//如果购买的是无座票
                Ticket ticket = new Ticket();
                ticket.setStatus(1);
                ticket.setStartStationCode(fromStationCode);
                ticket.setEndStationCode(toStationCode);
                ticket.setTrainNo(trainNo);
                ticket.setStartStationName(fromStation.getStationName());
                ticket.setEndStationName(toStation.getStationName());
                ticket.setTrainCode(train.getTrainCode());
                ticket.setIsStart(fromStation.getStationNo() == 0 ? 1 : 0);
                ticket.setIsEnd(toStation.getStationName().equals(train.getEndStationName()) ? 1 : 0);
                ticket.setSeat(-1);
                ticket.setStartTime(fromStation.getStartTime());
                ticket.setEndTime(toStation.getStartTime());
                ticket.setArriveDayDiff(toStation.getArriveDayDiff() - fromStation.getArriveDayDiff());
                ticket.setSeatType(-1);
                ticket.setUserId(userId);
                ticket.setPrice(toStation.getWz() - fromStation.getWz());
                return insertTicket(ticket,-1,fromStation.getStationNo(),toStation.getStationNo(),ansSeat,train.getSeatTypes());
            }
            else if(seatType > 5) {
                 return Result.error("不存在的座位类型");
            }
            else {//如果购买有座票
                int ansId = -1;
                int ansCarriage = -1;
                assert train != null;
                long flg = train.getSeatTypes();
                int pos = 9 * 120 + 1;
                for(int j = 0;j < 10;++j) {
                    long type = flg % 6;
                    flg /= 6;
                    if(type != seatType) {
                        pos -= 120;
                        continue;
                    }
                    for(int k = pos;k < pos + 120;++k) {
                        assert ansSeat != null;
                        if(ansSeat.get(k) && checkSeat(k % 120,seatType,seatPos) ) {
                            ansId = k;
                            ansCarriage = 10 - j;
                            break;
                        }
                    }
                    if(ansId != -1) {
                        break;
                    }
                }
                if(ansId != -1) {
                    Ticket ticket = new Ticket();
                    ticket.setStatus(1);
                    ticket.setStartStationCode(fromStationCode);
                    ticket.setEndStationCode(toStationCode);
                    ticket.setTrainNo(trainNo);
                    ticket.setStartStationName(fromStation.getStationName());
                    ticket.setEndStationName(toStation.getStationName());
                    ticket.setTrainCode(train.getTrainCode());
                    ticket.setIsStart(fromStation.getStationNo() == 0 ? 1 : 0);
                    ticket.setIsEnd(toStation.getStationName().equals(train.getEndStationName()) ? 1 : 0);
                    switch (seatType) {
                        case 0:
                            ticket.setPrice(toStation.getA1() - fromStation.getA1());
                            break;
                        case 1:
                            ticket.setPrice(toStation.getA3() - fromStation.getA3());
                            break;
                        case 2:
                            ticket.setPrice(toStation.getA4() - fromStation.getA4());
                            break;
                        case 3:
                            ticket.setPrice(toStation.getM() - fromStation.getM());
                            break;
                        case 4:
                            ticket.setPrice(toStation.getO() - fromStation.getO());
                            break;
                        case 5:
                            ticket.setPrice(toStation.getA9() - fromStation.getA9());
                            break;
                        default:
                            break;
                    }
                    ticket.setSeat(ansCarriage * 120 + (ansId % 120));
                    ticket.setStartTime(fromStation.getStartTime());
                    ticket.setEndTime(toStation.getStartTime());
                    ticket.setArriveDayDiff(toStation.getArriveDayDiff() - fromStation.getArriveDayDiff());
                    ticket.setSeatType(seatType);
                    ticket.setUserId(userId);
                    return insertTicket(ticket,ansId,fromStation.getStationNo(),toStation.getStationNo(),null,0);
                }
                else {
                    return Result.error("购票失败");
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("购票失败");
        }
    }
}

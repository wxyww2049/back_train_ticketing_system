package com.example.trainticket.service;

import com.example.trainticket.bean.Result;
import com.example.trainticket.data.po.Carriage;
import com.example.trainticket.data.po.Ticket;
import com.example.trainticket.data.po.Train;
import com.example.trainticket.data.po.TrainStation;
import com.example.trainticket.data.vo.BaseRoute;
import com.example.trainticket.data.vo.TrainDetail;
import com.example.trainticket.mapper.CarriageMapper;
import com.example.trainticket.mapper.TicketMapper;
import com.example.trainticket.mapper.TrainMapper;
import com.example.trainticket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

@Service
public class TrainService {
    @Autowired
    TrainMapper trainMapper;
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    CarriageMapper carriageMapper;

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
    @Transactional
    public Result insertTicket(Ticket ticket,int pos,int fromNo,int toNo) {
        Integer tid = ticketMapper.findMaxId();
        if(tid == null) tid = 0;
        ticket.setId(tid + 1);
        ticketMapper.insertTicket(ticket);

        for(int i = fromNo + 1;i <= toNo;i++) {
            BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(ticket.getTrainNo(), i)).getOrginSeat();
            seat.set(pos,false);
            RedisUtil.updCarriage(new Carriage(ticket.getTrainNo(), i, seat));
            carriageMapper.updateSeat(new Carriage(ticket.getTrainNo(), i, seat));
        }
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
            if(seatType == -1) {

                /**
                 * 填补上无座票的代码
                 */

            }
            else if(seatType > 5) {
                return Result.error("不存在的座位类型");
            }
            else {
                BitSet ansSeat = null;

                for(int i = fromStation.getStationNo() + 1;i <= toStation.getStationNo();i++) {
                    BitSet seat = Objects.requireNonNull(RedisUtil.getCarriage(trainNo, i)).getOrginSeat();

                    if(ansSeat == null) {
                        ansSeat = seat;
                    } else {
                        ansSeat.and(seat);
                    }
                }
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
                    return insertTicket(ticket,ansId,fromStation.getStationNo(),toStation.getStationNo());
                }
                else {
                    return Result.error("购票失败");
                }

            }

            return Result.success("success", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("购票失败");
        }
    }
}

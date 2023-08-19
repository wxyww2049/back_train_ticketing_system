package com.example.trainticket.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alipay {
    private String out_trade_no;//用于唯一表示一笔订单
    private double total_amount;
    private String subject;
    private String alipayTraceNo;
    private String product_code;
//    private String body;
    private String timeout_express;

}

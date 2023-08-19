package com.example.trainticket.bean;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayConfig {
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        // 设置参数（全局只需设置一次）
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
//设置网关地址
        alipayConfig.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
//设置应用ID
        alipayConfig.setAppId(appId);
//设置应用私钥
        alipayConfig.setPrivateKey(appPrivateKey);
//设置请求格式，固定值json
        alipayConfig.setFormat("JSON");
//设置字符集
        alipayConfig.setCharset("UTF-8");
//设置签名类型
        alipayConfig.setSignType("RSA2");
//设置支付宝公钥
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
//实例化客户端
        return new DefaultAlipayClient(alipayConfig);

    }

}

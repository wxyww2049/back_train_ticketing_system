package com.example.trainticket.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有的当前站点的请求地址，都支持跨域访问。
                .allowedOriginPatterns("*") // 所有的外部域都可跨域访问
                .allowCredentials(true) // 是否支持跨域用户凭证
                .allowedMethods("GET","POST","DELETE","PUT") // 当前站点支持的跨域请求类型是什么
                .allowedHeaders("*")
                .maxAge(3600); // 超时时长设置为1小时。 时间单位是秒。
    }
}
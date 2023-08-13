package com.example.trainticket.interceptor;

import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import com.example.trainticket.bean.StatusCode;
import com.example.trainticket.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

            if (auth == null) {
                log.info("auth is null");
                return true;
            }
            log.info("auth is not null");
            String[] identify = auth.identify();

            try {
                log.info("start auth");
                String token = request.getHeader("token");
                String userIdentify = jwtUtil.verifyToken(token).get("role").asString();
                for (String i : identify) {
                    if (userIdentify.equals(i)) {
                        return true;
                    }
                }
                //返回错误信息
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(Result.error("无权操作"));
                return false;
            }
            catch (Exception e) {
                log.info("auth failed");
                e.printStackTrace();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(Result.error(StatusCode.NO_PERMISSION));
                return false;
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

package com.example.trainticket.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.trainticket.data.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * 密钥
     */
    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * 过期时间
     **/
    @Value("${jwt.expiration}")
    private long EXPIRATION;//单位为秒

    /**
     * 生成用户token,设置token超时时间
     */
    public String createToken(User user) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("id", user.getId())//userId
                .withClaim("email", user.getEmail())//userName
                .withClaim("password", user.getPassword())//password
                .withClaim("role", user.getRole())//role
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
    }

    /**
     * 校验token并解析token
     */
    public Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);

            //decodedJWT.getClaim("属性").asString()  获取负载中的属性值

        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("token解码异常");
            //解码异常则抛出异常
            return null;
        }
        return jwt.getClaims();
    }

}
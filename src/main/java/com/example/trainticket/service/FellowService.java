package com.example.trainticket.service;

import cn.hutool.crypto.digest.BCrypt;
import com.auth0.jwt.interfaces.Claim;
import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import com.example.trainticket.bean.StatusCode;
import com.example.trainticket.data.po.Fellow;
import com.example.trainticket.data.po.User;
import com.example.trainticket.mapper.FellowMapper;
import com.example.trainticket.mapper.UserMapper;
import com.example.trainticket.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FellowService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FellowMapper fellowMapper;
    @Autowired
    private JwtUtil jwtUtil;

    public Result getAllFellows(String token) {
        try {
            Map<String, Claim> claimMap = jwtUtil.verifyToken(token);
            String email = claimMap.get("email").asString();
            List<Fellow> fellows = fellowMapper.getFellowByEmail(email);
            if(fellows == null) {
                return Result.error(StatusCode.USER_ACCOUNT_NOT_EXIST);
            }
            return Result.success("success", fellows);
        } catch (Exception e) {
            return Result.error("token无效");
        }
    }

    public Result insert(String userName, String idCode, String token) {
        try {
            Map<String, Claim> claimMap = jwtUtil.verifyToken(token);
            String email = claimMap.get("email").asString();
//            检查是否存在同名的同行者
            Fellow fellow = fellowMapper.getFellowByName(userName);
            if(fellow != null) {
                return Result.error(StatusCode.USER_ACCOUNT_ALREADY_EXIST);
            }
            fellow = new Fellow();
            fellow.setUserName(userName);
            fellow.setEmail(email);
            fellow.setIdCode(idCode);
            fellow.setStatus("1");
            try {
                log.info("new fellow"+ fellow);
                fellowMapper.addFellow(fellow);
            }
            catch (Exception e) {
                e.printStackTrace();
                return Result.error("添加失败");
            }
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error("token无效");
        }
    }

    public Result update(String id, String userName, String idCode, String token) {
        try {
            Map<String, Claim> claimMap = jwtUtil.verifyToken(token);
            String email = claimMap.get("email").asString();
            log.info("0");
            Fellow fellow = new Fellow();
            log.info("1");
            fellow.setId(Integer.valueOf(id));
            log.info("2");
            fellow.setUserName(userName);
            fellow.setEmail(email);
            fellow.setIdCode(idCode);
            fellow.setStatus("1");
            log.info("3");
            try {
                log.info("fellow"+ fellow);
                fellowMapper.upFellow(fellow);
            }
            catch (Exception e) {
                e.printStackTrace();
                return Result.error("更新失败");
            }
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error("token无效");
        }
    }

    public Result delete(String id, String token) {
        try {
            Map<String, Claim> claimMap = jwtUtil.verifyToken(token);
            fellowMapper.deleteFellow(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.info(String.valueOf(e));
            return Result.error("删除失败");
        }
    }

}

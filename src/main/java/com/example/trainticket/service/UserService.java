package com.example.trainticket.service;

import cn.hutool.crypto.digest.BCrypt;
import com.auth0.jwt.interfaces.Claim;
import com.example.trainticket.annotation.Auth;
import com.example.trainticket.bean.Result;
import com.example.trainticket.bean.StatusCode;
import com.example.trainticket.data.po.User;
import com.example.trainticket.mapper.UserMapper;
import com.example.trainticket.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 以后可能会添加邮箱验证
     */
    public Result signup(String username, String password, String email) {
        User user = userMapper.findUserByEmail(email);

        log.info("start signup for user: " + username + " email: " + email);
        if(user != null) {
            return Result.error(StatusCode.USER_ACCOUNT_ALREADY_EXIST);
        }

        String encodedPwd = BCrypt.hashpw(password,BCrypt.gensalt());

        user = new User();
        user.setUserName(username);
        user.setPassword(encodedPwd);
        user.setEmail(email);
        user.setRole("USER");
        String token = jwtUtil.createToken(user);
        user.setToken(token);
        try {
            System.out.println("user: " + user);
            userMapper.addUser(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败");
        }
        return Result.success("注册成功");
    }

    public Result login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if(user == null) {
            return Result.error(StatusCode.USER_ACCOUNT_NOT_EXIST);
        }
        if(!BCrypt.checkpw(password, user.getPassword())) {
            return Result.error(StatusCode.USER_CREDENTIALS_ERROR);
        }
        String token = jwtUtil.createToken(user);
        user.setToken(token);
        userMapper.updUser(user);
        user.setPassword(null);
        return Result.success("登录成功", user);
    }

    public Result generateAdmin(String userName,String password,String email) {
        User user = userMapper.findUserByEmail(email);
        if(user != null) {
            user.setRole("ADMIN");
            user.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
            String token = jwtUtil.createToken(user);
            user.setToken(token);
            userMapper.updUser(user);
            try {
                return Result.success("账户已存在，权限密码已重置");
            }
            catch (Exception e) {
                e.printStackTrace();
                return Result.error("注册失败");
            }
        }
        user = new User();
        user.setUserName(userName);
        user.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        user.setEmail(email);
        user.setRole("ADMIN");
        String token = jwtUtil.createToken(user);
        user.setToken(token);
        try {
            userMapper.addUser(user);
            return Result.success("注册成功", user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败");
        }
    }
    public Result changePwd(String email, String password, String newPassword) {
        User user = userMapper.findUserByEmail(email);
        if(user == null) {
            return Result.error(StatusCode.USER_ACCOUNT_NOT_EXIST);
        }
        if(!BCrypt.checkpw(password, user.getPassword())) {
            return Result.error(StatusCode.USER_CREDENTIALS_ERROR);
        }
        user.setPassword(BCrypt.hashpw(newPassword,BCrypt.gensalt()));
        String token = jwtUtil.createToken(user);
        user.setToken(token);
        userMapper.updUser(user);
        return Result.success("修改成功",user);
    }

    public Result changeIdCode(String token,String idCode) {
        try {
            Map<String, Claim> claimMap = jwtUtil.verifyToken(token);
            String email = claimMap.get("email").asString();
            User user = userMapper.findUserByEmail(email);
            if(user == null) {
                return Result.error(StatusCode.USER_ACCOUNT_NOT_EXIST);
            }
            user.setIdCode(idCode);
            userMapper.updUser(user);
            return Result.success("修改成功",user);
        }
        catch (Exception e) {
            return Result.error("token无效");
        }

    }
}

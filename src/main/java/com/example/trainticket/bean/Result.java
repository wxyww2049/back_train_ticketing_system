package com.example.trainticket.bean;
import com.alibaba.fastjson.JSONObject;

public class Result {
    int code;
    String message;
    Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(0, "success", true);
    }

    public static Result success(String message) {
        return new Result(0, message, null);
    }

    public static Result success(String message, Object data) {
        return new Result(0, message, data);
    }

    public static Result data(Object data) {
        return success("success", data);
    }

    public static Result data(StatusCode entity){
        return new Result(entity.getCode(), entity.getMessage(), entity.getCode() == 0);
    }

    public static Result data(){
        return success("success");
    }

    public static Result error(String message) {
        return new Result(-1, message, null);
    }

    public static Result error(String message, Object data) {
        return new Result(-1, message, data);
    }

    public static Result error(int code, String message) {
        return new Result(code, message, null);
    }

    public static Result error(StatusCode entity){
        return new Result(entity.getCode(), entity.getMessage(), null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

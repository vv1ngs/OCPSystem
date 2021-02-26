package org.vvings.ocpsystem.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author vvings
 * @version 2020/2/20 18:08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    private String token;
    private ServerResponse(int status) {
        this.code = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private ServerResponse(int status, T data) {
        this.code = status;
        this.data = data;
    }

    private ServerResponse(int status, T data, String message) {
        this.code = status;
        this.data = data;
        this.message = message;
    }

    @JsonIgnore
    private ServerResponse(int status, String message) {
        this.code = status;
        this.message = message;
    }

    public ServerResponse(int code, String message, T data, String token) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public Boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }
    public static <T> ServerResponse<T> createByLoginSuccess(String token) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(),null,token);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data, msg);

    }

    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String ErrorMsg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ErrorMsg);
    }

    public static <T> ServerResponse<T> createByErrorCode(int errorCode, String errorMsg) {
        return new ServerResponse<T>(errorCode, errorMsg);
    }
}


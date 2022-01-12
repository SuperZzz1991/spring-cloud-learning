package com.donghao.cloud.hystrixservice.pojo;

/**
 * @Author: DongHao
 * @Date: 2022/1/11 16:08
 * @Description:
 */
public class Response<T> {
    private T data;
    private String message;
    private Integer code;

    public Response(){}

    public Response(T data, String message, Integer code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public Response(String message, Integer code) {
        this(null, message, code);
    }

    public Response(T data) {
        this(data, "操作成功", 200);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

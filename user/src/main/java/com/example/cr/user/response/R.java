package com.example.cr.user.response;


public class R<T> {

    private Integer code;

    private T data;

    private String message;

    public R() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public R(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}

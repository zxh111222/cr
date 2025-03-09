package com.example.cr.common.response;

import org.springframework.http.HttpStatus;

public class R<T> {
    /*
    类似 HTTP 状态码的作用
     */
    private Integer code;

    /*
    额外消息（如：错误消息）
     */
    private String msg;

    /*
    具体数据
     */
    private T data;

    public R() {
    }

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(HttpStatus.BAD_REQUEST.value(), msg, null);
    }

    public static <T> R<T> fail(Integer code, String msg) {
        return new R<>(code, msg, null);
    }

    public static <T> R<T> failValid(String msg) {
        return new R<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), msg, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

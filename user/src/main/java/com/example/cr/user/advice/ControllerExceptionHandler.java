package com.example.cr.user.advice;

import com.example.cr.user.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 捕捉到 Exception 类型异常的统一处理
     * @param e 异常
     * @return R 统一返回结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Object> exceptionHandler(Exception e) {
        log.error("Exception: {}", e.getMessage());
        // 只要不是自定义的异常，用户只能看到 "系统异常"，保护隐私；但是管理员可以在系统日志中查看详情。
        return R.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常");
    }
}

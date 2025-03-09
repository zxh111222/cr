package com.example.cr.common.advice;

import com.example.cr.common.exception.CustomException;
import com.example.cr.common.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

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

    /**
     * 捕捉到 CustomException 自定义异常的统一处理
     * @param e 异常
     * @return R 统一返回结果
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Object> CustomExceptionHandler(Exception e) {
        log.error("CustomException: {}", e.getMessage());
        // 只要不是自定义的异常，用户只能看到 "系统异常"，保护隐私；但是管理员可以在系统日志中查看详情。
        return R.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 捕捉到 Spring 框架 BindException 校验异常的统一处理
     * @param e 异常
     * @return R 统一返回结果
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public R<Object> bindExceptionHandler(BindException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.error("BindException: {}", errorMessage);
        return R.failValid(errorMessage);
    }
}

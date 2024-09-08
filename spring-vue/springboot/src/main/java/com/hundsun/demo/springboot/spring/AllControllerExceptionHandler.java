package com.hundsun.demo.springboot.spring;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 所有controller的异常包装
 * @author hulei
 * @since 2024/9/4 0:12
 */

@Order(Integer.MIN_VALUE + 2)
@ControllerAdvice
public class AllControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleDataAccessException(Exception ex) {
        // 日志记录异常信息
        // logger.error("Data access exception occurred: " + ex.getMessage());

        // 返回适当的HTTP状态码和错误信息
        return new ResponseEntity<>("请求出现了异常, " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

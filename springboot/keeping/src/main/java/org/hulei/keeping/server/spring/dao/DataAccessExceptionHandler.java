package org.hulei.keeping.server.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Integer.MIN_VALUE + 1)
public class DataAccessExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        log.error("Data access exception occurred: ", ex);
        // 日志记录异常信息
        // logger.error("Data access exception occurred: " + ex.getMessage());

        // 返回适当的HTTP状态码和错误信息
        return new ResponseEntity<>("Data access exception occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // 可以添加其他数据访问异常的处理方法
}

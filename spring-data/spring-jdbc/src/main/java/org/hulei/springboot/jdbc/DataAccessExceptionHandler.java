package org.hulei.springboot.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
// 标记这个类为一个全局异常处理器。它可以捕获所有控制器中抛出的异常，并提供统一的异常处理逻辑。
@ControllerAdvice
@Order(Integer.MIN_VALUE + 1)
public class DataAccessExceptionHandler {

    /**
     * 指定这个方法用于处理 DataAccessException 类型的异常。
     *
     * @param ex 捕获到的异常对象。
     * @return 自定义的返回信息
     */
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

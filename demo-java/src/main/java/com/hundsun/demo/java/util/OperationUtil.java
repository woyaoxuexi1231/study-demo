package com.hundsun.demo.java.util;

import java.util.function.Consumer;

/**
 * @ProductName: Java
 * @Package:
 * @Description: 定义一个操作工具类
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */
public class OperationUtil {

    /**
     * 操作类
     */
    private Operation operation;

    public <T> void operationConsumer(Consumer<T> operation, T operationParam){

        operation.accept(operationParam);
    }
}

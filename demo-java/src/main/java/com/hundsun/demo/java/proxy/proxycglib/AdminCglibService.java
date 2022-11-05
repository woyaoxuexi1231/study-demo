package com.hundsun.demo.java.proxy.proxycglib;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * cglib-目标对象类(没有实现任何接口的一个类)
 * @ProductName: Java
 * @Package:
 * @Description: 定义一个操作工具类
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

@Slf4j
public class AdminCglibService {

    public void update() {
        log.info("修改管理系统数据");
    }

    public Object find() {
        log.info("查看管理系统数据");
        return new Object();
    }
}

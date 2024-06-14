package com.hundsun.demo.spring.jdk.pattern.structural.proxy;

/**
 * @ProductName: Java
 * @Package: com.hundsun.demo.java.jdk.proxy
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

public class MySQLServiceImpl implements MySQLService {

    public void update(String arg) {
        System.out.println("The data is changed to '" + arg + "', prepare to commit.");
    }
}

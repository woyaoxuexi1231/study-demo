package com.hundsun.demo.java.jdk.pattern.structural.proxy.normal;


import com.hundsun.demo.java.jdk.pattern.structural.proxy.MySQLService;
import lombok.Data;

/**
 * @ProductName: Java
 * @Package: com.hundsun.demo.java.jdk.proxy.normal
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */
@Data
public class MySQLServiceProxy implements MySQLService {

    /**
     * 被代理的对象
     */
    private MySQLService mySqlService;

    public MySQLServiceProxy() {
    }

    public MySQLServiceProxy(MySQLService mySqlService) {
        this.mySqlService = mySqlService;
    }

    public void update(String arg) {
        System.out.println("Mysql connected, starting the transaction...");
        mySqlService.update(arg);
        System.out.println("Transaction has been commit.");
    }
}

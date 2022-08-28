package com.hundsun.demo.java.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 *
 */

/**
 * @Description: 封装一个LogManager, 这个类用于在使用java原生的log时，自己定义log的配置,可以自己定义java原生log的配置
 * @ProductName: Java
 * @Package:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

public class LogManager {


    // 初始化LogManager
    static {
        // 读取配置文件  （这边也可以使用文件流）
        ClassLoader cl = java.util.logging.LogManager.class.getClassLoader();
        InputStream inputStream = null;
        if (cl != null) {
            inputStream = cl.getResourceAsStream("logging.properties");
        } else {
            inputStream = ClassLoader
                    .getSystemResourceAsStream("logging.properties");
        }
        java.util.logging.LogManager logManager = java.util.logging.LogManager
                .getLogManager();
        // 重新初始化日志属性并重新读取日志配置。
        try {
            logManager.readConfiguration(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志对象
     * @param className
     * @return
     */
    public static Logger getLogger(String className) {
        Logger logger = Logger
                .getLogger(className);
        return logger;
    }

}

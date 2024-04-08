package com.hundun.demo.log;


import org.apache.logging.log4j.jul.Log4jBridgeHandler;
import org.apache.logging.log4j.jul.LogManager;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author hulei42031
 * @since 2024-04-08 15:54
 */

public class JulToLog4jExample {

    // 获取日志记录器
    private static final Logger logger = Logger.getLogger(JulToLog4jExample.class.getName());

    public static void main(String[] args) {
        // 记录不同级别的日志消息
        // Log4jBridgeHandler.install();
        logger.severe("Severe log message");
        logger.warning("Warning log message");
        logger.info("Info log message");
        logger.config("Config log message");
        logger.fine("Fine log message");
        logger.finer("Finer log message");
        logger.finest("Finest log message");

        // List<Integer>
    }
}


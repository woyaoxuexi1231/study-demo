package com.hundun.demo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Slf4jExample {

    // 获取日志记录器
    private static final Logger logger = LoggerFactory.getLogger(Slf4jExample.class);

    public static void main(String[] args) {

        // 安装 JUL 到 SLF4J 的桥接器
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        // 记录不同级别的日志消息
        logger.debug("Debug log message");
        logger.info("Info log message");
        logger.warn("Warning log message");
        logger.error("Error log message");
    }
}

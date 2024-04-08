// package com.hundun.demo.log;
//
// import org.slf4j.bridge.SLF4JBridgeHandler;
//
// import java.util.logging.Logger;
//
// public class JulToSlf4jToLogbackExample {
//
//     private static final Logger logger = Logger.getLogger(JulToSlf4jToLogbackExample.class.getName());
//
//     public static void main(String[] args) {
//         // 移除现有的JUL默认处理器，并安装SLF4JBridgeHandler
//         SLF4JBridgeHandler.removeHandlersForRootLogger();
//         SLF4JBridgeHandler.install();
//         // 记录不同级别的日志消息
//         logger.severe("Severe log message");
//         logger.warning("Warning log message");
//         logger.info("Info log message");
//         logger.config("Config log message");
//         logger.fine("Fine log message");
//         logger.finer("Finer log message");
//         logger.finest("Finest log message");
//     }
// }

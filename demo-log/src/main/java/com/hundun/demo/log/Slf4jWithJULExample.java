// package com.hundun.demo.log;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import java.io.IOException;
// import java.util.logging.LogManager;
//
// public class Slf4jWithJULExample {
//
//     // 获取日志记录器
//     private static final Logger logger = LoggerFactory.getLogger(Slf4jWithJULExample.class);
//
//     public static void main(String[] args) {
//         // 读取配置文件
//         // try {
//         //     LogManager.getLogManager().readConfiguration(Slf4jWithJULExample.class.getResourceAsStream("/logging.properties"));
//         // } catch (IOException e) {
//         //     System.out.println("Could not load configuration file");
//         //     e.printStackTrace();
//         // }
//         // 记录不同级别的日志消息
//         logger.debug("Debug log message");
//         logger.info("Info log message");
//         logger.warn("Warning log message");
//         logger.error("Error log message");
//     }
// }

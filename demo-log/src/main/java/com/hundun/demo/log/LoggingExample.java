// package com.hundun.demo.log;
//
// import java.io.IOException;
// import java.util.logging.ConsoleHandler;
// import java.util.logging.Level;
// import java.util.logging.LogManager;
// import java.util.logging.Logger;
// import java.util.logging.SimpleFormatter;
//
// public class LoggingExample {
//
//     // 获取日志记录器
//     private static final Logger logger = Logger.getLogger(LoggingExample.class.getName());
//
//     public static void main(String[] args) {
//         // 读取配置文件
//         try {
//             LogManager.getLogManager().readConfiguration(LoggingExample.class.getResourceAsStream("/logging.properties"));
//         } catch (IOException e) {
//             System.out.println("Could not load configuration file");
//             e.printStackTrace();
//         }
//
//         // 配置日志记录器
//         configureLogger();
//
//         // 记录不同级别的日志消息
//         logger.finest("Finest log message");
//         logger.finer("Finer log message");
//         logger.fine("Fine log message");
//         logger.config("Config log message");
//         logger.info("Info log message");
//         logger.warning("Warning log message");
//         logger.severe("Severe log message");
//     }
//
//     // 配置日志记录器
//     private static void configureLogger() {
//         // 创建一个简单的日志记录控制器，并设置级别为 INFO
//         ConsoleHandler consoleHandler = new ConsoleHandler();
//         consoleHandler.setLevel(Level.INFO);
//
//         // 配置日志格式
//         consoleHandler.setFormatter(new SimpleFormatter());
//
//         // 将日志处理器添加到记录器中
//         logger.addHandler(consoleHandler);
//
//         // 设置记录器级别
//         logger.setLevel(Level.ALL);
//     }
// }

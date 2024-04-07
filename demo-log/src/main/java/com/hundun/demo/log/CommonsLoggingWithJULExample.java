// package com.hundun.demo.log;
//
// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.logging.LogManager;
//
// public class CommonsLoggingWithJULExample {
//
//     // 获取日志记录器
//     private static final Log logger = LogFactory.getLog(CommonsLoggingWithJULExample.class);
//
//     public static void main(String[] args) {
//         // 加载 JDK 日志框架的配置文件
//         try (InputStream stream = CommonsLoggingWithJULExample.class.getResourceAsStream("/logging.properties")) {
//             LogManager.getLogManager().readConfiguration(stream);
//         } catch (IOException e) {
//             System.err.println("Error loading logging configuration: " + e.getMessage());
//         }
//
//         // 记录不同级别的日志消息
//         logger.debug("Debug log message");
//         logger.info("Info log message");
//         logger.warn("Warning log message");
//         logger.error("Error log message");
//         logger.fatal("Fatal log message");
//     }
// }

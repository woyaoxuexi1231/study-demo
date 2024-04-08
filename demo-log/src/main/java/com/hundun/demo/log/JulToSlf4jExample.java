// package com.hundun.demo.log;
//
// import org.slf4j.bridge.SLF4JBridgeHandler;
//
// import java.util.logging.Logger;
//
// public class JulToSlf4jExample {
//
//     private static final Logger logger = Logger.getLogger(JulToSlf4jExample.class.getName());
//
//     public static void main(String[] args) {
//         // Optionally remove existing handlers attached to j.u.l root logger
//         SLF4JBridgeHandler.removeHandlersForRootLogger();
//         // Add SLF4JBridgeHandler to j.u.l's root logger
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

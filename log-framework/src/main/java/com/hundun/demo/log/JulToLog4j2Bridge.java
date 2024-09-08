// package com.hundun.demo.log;
//
// public class JulToLog4j2Bridge {
//
//     static {
//         // 在JUL使用之前设置
//         System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
//     }
//
//     public static void main(String[] args) {
//
//         // 由于已经设置了JUL的LogManager为Log4j的版本，下面的代码将利用log4j2进行日志记录
//         java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(JulToLog4j2Bridge.class.getName());
//         julLogger.info("This JUL message is managed by log4j2");
//
//         // 假设你的现有代码库已经广泛使用了JUL日志记录，现在无需修改现有日志调用代码，
//         // 即可享受log4j2提供的高级功能和改进的性能。
//     }
// }
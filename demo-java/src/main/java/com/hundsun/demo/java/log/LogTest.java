package com.hundsun.demo.java.log;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.log
 * @className: LogTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/6 19:27
 */

public class LogTest {

    /*
    日志框架的进化史 - log4j[Apache] -> JUL[sun] -> JCL(commons logging) -> slf4j&logback -> log4j2[Apache]

    日志框架分为日志门面(只提供相应API而不提供接口实现)和日志系统(提供具体的接口实现)
    日志门面: commons-logging, slf4j
    日志系统: log4j, JUL, Logback, slf4j-simple

    日志库适配器: jul-to-slf4j, log4j-over-slf4j, jcl-over-slf4j
    日志门面适配器: slf4j-jdk, slf4j-jcl, slf4j-log4j12

    常用的日志搭配:
        slf4j+logback 不需要桥接包
        slf4j+log4j2 需要桥接包 log4j-slf4j-impl
        commons logging+log4j2 不需要桥接包
     */
}

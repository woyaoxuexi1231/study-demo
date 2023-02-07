package com.hundsun.demo.java.log;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.log
 * @className: JULTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 21:28
 */
@Log
public class JULTest {

    public static void main(String[] args) throws IOException {
        // 自定义配置文件
        InputStream is = JULTest.class.getClassLoader().getResourceAsStream("logging.properties");
        LogManager logManager = LogManager.getLogManager();
        logManager.readConfiguration(is);
        log.info("java.util.logging!");
    }
}

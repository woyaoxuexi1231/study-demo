package org.hulei.log.framework;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * @author hulei
 * @since 2024/11/9 10:06
 */

@Slf4j
@SuppressWarnings("ALL")
public class LombokLogExample {

    // static {
    //     // JUL 加载配置文件使用
    //     try (InputStream inputStream = LombokLogExample.class.getClassLoader().getResourceAsStream("logging.properties")) {
    //         if (inputStream != null) {
    //             LogManager.getLogManager().readConfiguration(inputStream);
    //             System.out.println("Logging configuration loaded successfully");
    //         } else {
    //             System.err.println("Could not find logging.properties file");
    //         }
    //     } catch (Exception e) {
    //         System.err.println("load jul configuration error" + e.getMessage());
    //         e.printStackTrace();
    //     }
    // }

    public static void main(String[] args) {
        // Logger logger = LoggerFactory.getLogger(LombokLogExample.class);
        // logger.info("Hello World !!");
        log.info("Hello World !!");
    }
}

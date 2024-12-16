package org.hulei.springboot.thymeleaf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author woaixuexi
 * @since 2024/3/10 13:38
 */

@Slf4j
@SpringBootApplication
public class SpringbootThymeleafApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringbootThymeleafApplication.class);
        log.info("启动完成");
    }
}

package org.hulei.spring.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/11/17 15:46
 */

@Slf4j
@SpringBootApplication
public class SpringReactiveApplication {

    public static void main(String[] args) {
        log.info("hello reactive");
        SpringApplication.run(SpringReactiveApplication.class, args);
    }
}

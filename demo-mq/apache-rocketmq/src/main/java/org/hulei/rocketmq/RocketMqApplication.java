package org.hulei.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/8/21 20:17
 */

@RestController
@SpringBootApplication
public class RocketMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketMqApplication.class, args);
    }
}

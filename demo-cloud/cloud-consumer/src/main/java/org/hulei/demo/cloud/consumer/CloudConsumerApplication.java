package org.hulei.demo.cloud.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hulei
 * @since 2025/7/31 14:05
 */

@EnableFeignClients(basePackages = {"org.hulei.springcloud.client.api","org.hulei.demo.cloud.consumer.feign"})
@SpringBootApplication
public class CloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerApplication.class, args);
    }


}

package org.hulei.springcloudalibaba.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/10/30 11:22
 */

@Slf4j
@SpringBootApplication
public class NacosApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NacosApplication.class, args);
    }

    @Value("${nacos.value}")
    public String nacosValue;


    @Override
    public void run(String... args) throws Exception {
        log.info("nacosValue: {}", nacosValue);
    }
}

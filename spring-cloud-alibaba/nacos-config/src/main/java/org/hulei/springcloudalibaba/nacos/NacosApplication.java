package org.hulei.springcloudalibaba.nacos;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hulei
 * @since 2024/10/30 11:22
 */

@Slf4j
@SpringBootApplication
public class NacosApplication {

    public static void main(String[] args) {
        // System.setProperty("spring.cloud.bootstrap.enabled", "true");
        SpringApplication.run(NacosApplication.class, args);
    }
}

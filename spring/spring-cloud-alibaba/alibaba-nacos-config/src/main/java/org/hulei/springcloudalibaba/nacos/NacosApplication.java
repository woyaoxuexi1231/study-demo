package org.hulei.springcloudalibaba.nacos;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/30 11:22
 */

@Slf4j
@SpringBootApplication
@RequestMapping("/application")
@RestController
public class NacosApplication {

    @Value("${remote-config:null}")
    public String remoteConfig;

    public static void main(String[] args) {
        // System.setProperty("spring.cloud.bootstrap.enabled", "true");
        SpringApplication.run(NacosApplication.class, args);
    }

    @RequestMapping(value = "/getRemoteConfig", method = RequestMethod.GET)
    public void getRemoteConfig() {
        log.info("remoteConfig: {}", remoteConfig);
    }
}

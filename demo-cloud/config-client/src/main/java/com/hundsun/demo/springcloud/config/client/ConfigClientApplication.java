package com.hundsun.demo.springcloud.config.client;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@RefreshScope
@RestController
@RequiredArgsConstructor
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClientApplication {

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {

        SpringApplication.run(ConfigClientApplication.class, args);
    }

    private final EurekaClient eurekaClient;
    private final ApplicationInfoManager applicationInfoManager;

    @GetMapping("/get-port")
    public String getPort() {
        System.out.println(port);
        return port;
    }

    @PostMapping("/changeStatus")
    public String changeStatus(@RequestParam String status) {
        /*
        DiscoveryClient
            instanceInfoReplicator 可以重新注册服务的信息
        这里就是以服务状态为例，可以动态的修改当前服务的注册信息
        但是 port 端口信息并不会造成服务当前的 http 端口的改动(不会重启web服务器)
         */
        InstanceInfo.InstanceStatus newStatus = InstanceInfo.InstanceStatus.valueOf(status.toUpperCase());
        applicationInfoManager.setInstanceStatus(newStatus);
        return "Status changed to " + newStatus.name();
    }
}

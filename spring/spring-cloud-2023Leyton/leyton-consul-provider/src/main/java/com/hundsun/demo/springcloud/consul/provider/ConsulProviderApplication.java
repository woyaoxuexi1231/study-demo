package com.hundsun.demo.springcloud.consul.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author h1123
 * @since 2023/5/5 20:44
 */

// 开启服务注册发现
@EnableDiscoveryClient
@SpringBootApplication
public class ConsulProviderApplication {

    public static void main(String[] args) {

        /*
        consul 配置文件加载顺序为
            config/consul-provider-dev
            config/consul-provider
            config/consulProvider-dev
            config/application-dev
            config/application
            每个路径下都有一个名为 data 的文件
         */
        SpringApplication.run(ConsulProviderApplication.class, args);
    }
}

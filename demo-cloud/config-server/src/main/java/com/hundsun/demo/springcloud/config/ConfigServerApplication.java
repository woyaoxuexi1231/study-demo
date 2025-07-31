package com.hundsun.demo.springcloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

// 开启配置中心
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {

        /*

        https://springdoc.cn/spring-cloud-config/
        spring-cloud-config-server 是 Spring Cloud 提供的集中式配置管理服务
        用于将应用的配置集中存放在一个地方（比如 Git 仓库或本地文件夹），并提供 HTTP 接口供客户端（如微服务）远程读取配置。
        native形式：本地形式要做到实时刷新配置，那么要注意的一个点就是配置文件的更新，在idea内要重新build一下，不然target内的文件是没有更新的

        Spring Cloud Config 有它的一套访问规则，我们通过这套规则在浏览器上直接访问就可以。
        /{application}/{profile}[/{label}]
        /{application}-{profile}.yml
        /{label}/{application}-{profile}.yml
        /{application}-{profile}.properties
        /{label}/{application}-{profile}.properties


        http://localhost:10014/config-client/dev/master
        http://localhost:10014/config-client/prod
        http://localhost:10014/config-client-dev.yml
        http://localhost:10014/config-client-pro.yml
        http://localhost:10014/master/config-client-pro.yml

        git 仓库配置：
        http://localhost:10014/config-client/dev 可以直接查看配置
         */
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}

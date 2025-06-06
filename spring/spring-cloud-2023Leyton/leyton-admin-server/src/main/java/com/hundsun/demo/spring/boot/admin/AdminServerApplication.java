package com.hundsun.demo.spring.boot.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminServerApplication {
    public static void main(String[] args) {
        /*
        Spring Boot Admin 是一个可视化管理工具，可以监控多个 Spring Boot 应用的：
            应用状态	在线、离线、端口等
            内存、线程、GC	JVM 运行指标
            日志级别动态调整	不用重启就能修改日志级别
            Actuator 端点查看	如 /health、/metrics、/env 等
            指标展示（与 Micrometer 配合）	线程数、内存占用、请求次数、响应时间等
            应用版本、构建时间等	从 info.* 获取
         */
        SpringApplication.run(AdminServerApplication.class, args);
    }
}

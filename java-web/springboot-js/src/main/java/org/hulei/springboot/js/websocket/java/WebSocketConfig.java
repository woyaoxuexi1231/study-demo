package org.hulei.springboot.js.websocket.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.websocket
 * @className: WebSocketConfig
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/11 19:25
 */

@Configuration
public class WebSocketConfig {

    /**
     * ServerEndpointExporter是Spring Boot提供的一个用于自动注册WebSocket服务的类。它是实现WebSocket服务器端的关键组件之一。
     * <p>
     * 在使用WebSocket技术开发服务器端时，需要注册并管理WebSocket的端点（Endpoint）。
     * 通常情况下，我们需要手动配置和注册这些端点，以便能够处理WebSocket请求。
     * 而ServerEndpointExporter提供了一种自动注册的机制，可以简化WebSocket端点的注册过程。
     * <p>
     * ServerEndpointExporter是一个Spring Bean，在Spring Boot应用的启动阶段，它会自动扫描并注册所有使用@ServerEndpoint注解标注的类。
     * 这些类会被识别为WebSocket端点，并且自动注册到内置的WebSocket容器中，以便能够处理WebSocket请求。
     * 通过自动注册，我们无需手动配置每个端点，使得WebSocket的使用更加方便和高效。
     * <p>
     * 使用ServerEndpointExporter的步骤如下：
     * <p>
     * 在Spring Boot项目中，添加依赖 spring-boot-starter-websocket。
     * 创建一个类，并使用 @ServerEndpoint 注解标注该类来定义WebSocket的端点。
     * 在Spring配置类中添加 @Bean 注解，创建一个名为 serverEndpointExporter 的方法，返回一个 ServerEndpointExporter 的实例。
     * 启动应用，ServerEndpointExporter 会自动扫描并注册所有的WebSocket端点。
     *
     * @return ServerEndpointExporter
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

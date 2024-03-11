package com.hundsun.demo.springboot.utils.websocket;

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

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

package org.hulei.springboot.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.websocket
 * @className: SpringWebSocketConfig
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/11 20:50
 */

@Slf4j
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    SpringWebSocketHandler springWebSocketHandler;

    @Autowired
    SpringWebSocketInterceptor springWebSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(springWebSocketHandler, "/myWsSpring")
                .addInterceptors(springWebSocketInterceptor)
                .setAllowedOrigins("*");
    }
}

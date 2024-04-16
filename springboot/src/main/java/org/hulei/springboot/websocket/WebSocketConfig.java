package org.hulei.springboot.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: org.hulei.springboot.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-11 16:03
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic");
    }

    /**
     * 用于注册 STOMP 端点
     *
     * @param registry StompEndpointRegistry 是用于注册 STOMP（Simple Text Oriented Messaging Protocol）端点的类。STOMP 是一种简单的消息传递协议，通常用于在客户端和服务器之间进行异步消息通信。
     *                 通过 StompEndpointRegistry 可以注册 STOMP 端点，客户端可以使用这些端点来建立 WebSocket 连接，并在连接上发送和接收消息。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // // 表示注册了一个 STOMP 端点，客户端可以使用这个端点来建立 WebSocket 连接。
        // registry.addEndpoint("/gs-guide-websocket")
        // // 这行代码设置允许的跨域来源。* 是一个特殊的通配符，表示允许来自任何域的请求。但是，这在 allowCredentials 设置为 true 时是无效的，因为浏览器会阻止使用凭证的请求从任意域接受。
        // .setAllowedOrigins("*")
        // // 启用 SockJS 支持。这是一种备选方案，可以在浏览器不支持原生 WebSocket 时使用，提供更好的兼容性。
        // .withSockJS();
        /*
        使用上面的代码报错了
        java.lang.IllegalArgumentException: When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
         */
        registry.addEndpoint("/gs-guide-websocket")
                // 这行代码设置允许的跨域来源。* 是一个特殊的通配符，表示允许来自任何域的请求。但是，这在 allowCredentials 设置为 true 时是无效的，因为浏览器会阻止使用凭证的请求从任意域接受。
                .setAllowedOrigins("http://localhost:10099")
                // 启用 SockJS 支持。这是一种备选方案，可以在浏览器不支持原生 WebSocket 时使用，提供更好的兼容性。
                .withSockJS();
    }
}

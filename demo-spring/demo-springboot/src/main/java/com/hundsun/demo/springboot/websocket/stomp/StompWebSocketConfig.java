package com.hundsun.demo.springboot.websocket.stomp;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
// 这个注解启用了基于消息代理的 WebSocket，它允许在应用中使用基于消息的 WebSocket，并且提供了方便的配置选项。
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 用于配置消息代理
     *
     * @param config MessageBrokerRegistry 是用于配置消息代理的类。在 WebSocket 应用中，消息代理负责处理客户端发送的消息，并将消息路由到相应的目的地（例如其他客户端或服务器端处理器）。
     *               通过 MessageBrokerRegistry 可以配置消息代理的一些参数，比如启用哪种类型的消息代理（如简单消息代理或者其他的消息代理实现），以及设置客户端订阅的主题前缀等。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /*
        调用了 config.enableSimpleBroker("/topic")，表示启用了一个简单的消息代理, 并指定了前缀为 “/topic”，意味着客户端订阅的主题以 “/topic” 开头。
        启用简单消息代理：这允许应用程序使用简单的in-memory 消息代理来处理以 /topic 开头的目的地。
        消息传递：enableSimpleBroker 允许服务器向所有订阅了特定主题的客户端广播消息。例如，所有订阅了 /topic/greetings 的客户端将接收到所有发送到这个目的地的消息。

        @SendTo("/topic/greetings")：
        这是一个方法级注解，用来指示消息发送到特定目的地，即 /topic/greetings。
        它通常与控制器方法一起使用，以处理接收到的消息并将响应或更新发送到客户端。

        相当于就是这里配置一个发消息的工具,如果不配这个工具,即使指定了sendto注解,没有发送工具,消息也发不出去.
         */
        config.enableSimpleBroker("/topic");
        /*
        设置了应用程序目的地的前缀为 “/app”，意味着客户端发送消息的目的地应该以 “/app” 开头。
        相当于整个应用只接收app为前缀的消息了
         */
        // config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 用于注册 STOMP 端点
     *
     * @param registry StompEndpointRegistry 是用于注册 STOMP（Simple Text Oriented Messaging Protocol）端点的类。STOMP 是一种简单的消息传递协议，通常用于在客户端和服务器之间进行异步消息通信。
     *                 通过 StompEndpointRegistry 可以注册 STOMP 端点，客户端可以使用这些端点来建立 WebSocket 连接，并在连接上发送和接收消息。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // 表示注册了一个 STOMP 端点，客户端可以使用这个端点来建立 WebSocket 连接。
        // registry.addEndpoint("/gs-guide-websocket");

        registry.addEndpoint("/gs-guide-websocket")
                // 使用*通配符会报错
                // java.lang.IllegalArgumentException: When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
                // .setAllowedOrigins("*")
                // 这行代码设置允许的跨域来源。* 是一个特殊的通配符，表示允许来自任何域的请求。但是，这在 allowCredentials 设置为 true 时是无效的，因为浏览器会阻止使用凭证的请求从任意域接受。
                .setAllowedOrigins("http://localhost:10099")
                // 启用 SockJS 支持。这是一种备选方案，可以在浏览器不支持原生 WebSocket 时使用，提供更好的兼容性。
                .withSockJS();
    }

}

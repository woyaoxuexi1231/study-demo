package org.hulei.springboot.js.websocket.spring;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Websocket配置类
 *
 * @author lucky_fd
 * @since 2024-01-17
 */
@Configuration
@EnableWebSocket
public class WebSocketSpringConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册websocket处理器和拦截器
        registry.addHandler(webSocketHandler(), "/websocket/server")
                .addInterceptors(webSocketHandleInterceptor()).setAllowedOrigins("*");
        registry.addHandler(webSocketHandler(), "/sockjs/server").setAllowedOrigins("*")
                .addInterceptors(webSocketHandleInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler();
    }

    @Bean
    public WebSocketHandleInterceptor webSocketHandleInterceptor() {
        return new WebSocketHandleInterceptor();
    }
}

/**
 * websocket处理类
 * 实现WebSocketHandler接口
 * <p>
 * - websocket建立连接后执行afterConnectionEstablished回调接口
 * - websocket关闭连接后执行afterConnectionClosed回调接口
 * - websocket接收客户端消息执行handleTextMessage接口
 * - websocket传输异常时执行handleTransportError接口
 *
 * @author lucky_fd
 * @since 2024-01-17
 */

class WebSocketHandler extends TextWebSocketHandler {

    /**
     * 存储websocket客户端连接
     */
    private static final Map<String, WebSocketSession> connections = new HashMap<>();

    /**
     * 建立连接后触发
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立websocket连接");
        // 建立连接后将连接以键值对方式存储，便于后期向客户端发送消息
        // 以客户端连接的唯一标识为key,可以通过客户端发送唯一标识
        connections.put(session.getRemoteAddress().getHostName(), session);
        System.out.println("当前客户端连接数：" + connections.size());
    }

    /**
     * 接收消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到消息: " + message.getPayload());

        // 收到客户端请求消息后进行相应业务处理，返回结果
        this.sendMessage(session.getRemoteAddress().getHostName(), new TextMessage("收到消息: " + message.getPayload()));
    }

    /**
     * 传输异常处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("触发关闭websocket连接");
        // 移除连接
        connections.remove(session.getRemoteAddress().getHostName());
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    /**
     * 向连接的客户端发送消息
     *
     * @param clientId 客户端标识
     * @param message  消息体
     * @author lucky_fd
     **/
    public void sendMessage(String clientId, TextMessage message) {
        for (String client : connections.keySet()) {
            if (client.equals(clientId)) {
                try {
                    WebSocketSession session = connections.get(client);
                    // 判断连接是否正常
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }
}


/**
 * Websocket拦截器类
 *
 * @author lucky_fd
 * @since 2024-01-17
 */

class WebSocketHandleInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Exception ex) {
        System.out.println("拦截器后置触发");
        super.afterHandshake(request, response, wsHandler, ex);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("拦截器前置触发");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}






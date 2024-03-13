package com.hundsun.demo.springboot.websocket.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.websocket
 * @className: WebSocketInterceptor
 * @description: 握手拦截器
 * @author: woaixuexi
 * @createDate: 2024/3/11 20:39
 */

@Slf4j
@Component
public class SpringWebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("beforeHandshake -> remoteAddress: {}", request.getRemoteAddress());
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("afterHandshake -> remoteAddress: {}", request.getRemoteAddress());
        super.afterHandshake(request, response, wsHandler, ex);
    }
}

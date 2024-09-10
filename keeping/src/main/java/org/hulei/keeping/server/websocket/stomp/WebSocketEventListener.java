package org.hulei.keeping.server.websocket.stomp;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * WebSocket 连接和断开的监听类
 *
 * @author hulei
 * @since 2024/9/10 17:17
 */

@Slf4j
@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket连接开启: {}", headerAccessor.getSessionId());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        /*
        vue+stomp的时候,时不时会出现
        24-04-17 00:26:36.404 INFO [http-nio-10088-exec-7] org.springframework.web.socket.messaging.SubProtocolWebSocketHandler(503) - No messages received after 730655 ms. Closing XhrStreamingSockJsSession[id=bssbfzpz].
        24-04-17 00:26:36.404 INFO [http-nio-10088-exec-7] org.hulei.springboot.websocket.WebSocketEventListener(25) - WebSocket连接断开: bssbfzpz

        解释：这条日志表明在 729735 毫秒（大约 12 分钟）后没有接收到任何消息，因此 WebSocket 连接被关闭。这种情况通常是由于连接超时而导致的。
        我估计是因为经常改代码之后刷新导致
         */
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket连接断开: {}", headerAccessor.getSessionId());
    }
}

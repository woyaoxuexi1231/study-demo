package com.hundsun.demo.springboot.websocket.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.websocket
 * @className: WebSocketHandler
 * @description: websocket处理程序
 * @author: woaixuexi
 * @createDate: 2024/3/11 20:41
 */

@Slf4j
@Component
public class SpringWebSocketHandler extends AbstractWebSocketHandler {

    Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        log.info("afterConnectionEstablished, session: {}", session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("handleTextMessage, session: {}, message: {}", session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        log.error("handleTransportError, session: {}", session, exception);
        sessionMap.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("handleTextMessage, session: {}, status: {}", session, status);
        sessionMap.remove(session.getId());
    }

    @Scheduled(fixedRate = 2000)
    public void execute() {
        sessionMap.forEach(new BiConsumer<String, WebSocketSession>() {
            @SneakyThrows
            @Override
            public void accept(String s, WebSocketSession webSocketSession) {
                webSocketSession.sendMessage(new TextMessage("hello, this is websocket server"));
            }
        });
    }
}

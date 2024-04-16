package org.hulei.springboot.websocket;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.model.Greeting;
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

    /**
     * 保存每一个已经建立的 websocket
     * sessionid - 会话
     */
    Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 方法用于在WebSocket连接成功建立后进行一些自定义的操作，例如存储会话信息、记录日志等。
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        log.info("websocket连接已建立, session: {}", session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("websocket接收到消息, session: {}, message: {}", session, message);
        // 收到消息后发送一个消息出去
        sessionMap.forEach(new BiConsumer<String, WebSocketSession>() {
            @SneakyThrows
            @Override
            public void accept(String s, WebSocketSession webSocketSession) {
                webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(new Greeting("Hello, " + (message.getPayload() + "!")))));
            }
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // handleTransportError 方法在 WebSocket 传输过程中发生错误时被调用，用于处理这些错误。
        super.handleTransportError(session, exception);
        log.error("websocket发生错误, session: {}", session, exception);
        // 从session中移除
        sessionMap.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("websocket连接关闭, session: {}, status: {}", session, status);
        sessionMap.remove(session.getId());
    }

    // @Scheduled(fixedRate = 2000)
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

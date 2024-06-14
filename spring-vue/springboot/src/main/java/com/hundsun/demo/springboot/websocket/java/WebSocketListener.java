package com.hundsun.demo.springboot.websocket.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.listener
 * @className: WebSocketListener
 * @description: 以Java注解形式实现的websocket
 * @author: woaixuexi
 * @createDate: 2024/3/11 19:15
 */

@Slf4j
@ServerEndpoint("/myWs")
@Component
public class WebSocketListener {

    /**
     * ServerEndpoint 注解的一个关键特性是，每当一个新的WebSocket连接建立时，WebSocket服务器会为每个新连接创建一个新的端点实例。
     * 这意味着每个连接都有自己的 @ServerEndpoint 注解的类实例。这种设计有助于隔离不同客户端的状态，使得每个连接可以拥有自己独立的会话和状态信息
     * 这种情况下，如果类内部的变量不是static的，即每个实例都有自己的变量副本，那么这些变量的状态只能在该实例中保持一致，而无法跨实例共享。
     * 所以这里需要使用 static
     * 1. WebSocket服务器为每个新连接创建新的端点实例，导致非静态变量无法跨实例共享。
     * 2. 在WebSocket的使用场景中，通常需要跨多个连接共享数据或状态，static变量提供了这种跨实例共享的能力。
     */
    static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 建立连接
     *
     * @param session session
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), session);
        log.info("websocket is onOpen, session: {}", session);
    }

    /**
     * 收到客户端的消息
     *
     * @param req req
     * @return rsp
     */
    @OnMessage
    public String onMessage(String req) {
        log.info("websocket is onMessage: {}", req);
        return "hello, this is server";
    }

    /**
     * 连接关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("websocket is onClose");
    }

    @Scheduled(fixedRate = 2000)
    public void execute() {
        for (String s : sessionMap.keySet()) {
            try {
                sessionMap.get(s).getBasicRemote().sendText("hell, this is execute");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

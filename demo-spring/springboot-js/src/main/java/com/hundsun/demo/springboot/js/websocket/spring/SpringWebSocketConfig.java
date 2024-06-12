package com.hundsun.demo.springboot.js.websocket.spring;

import com.alibaba.fastjson.JSON;
import com.hundsun.demo.springboot.js.websocket.stomp.Greeting;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

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

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(new SpringWebSocketSessionManage(), "/myWsSpring")
                .addInterceptors(new SpringWebSocketInterceptor())
                .setAllowedOrigins("*");
    }

    /**
     * TODO 2024年4月14日 就目前来说,又没报错了.
     * a.根据报错中defaultSockJsTaskScheduler找到websocket源码如下，
     * 在初始化websocket时WebSocketConfigurationSupport.class 类中会执行defaultSockJsTaskScheduler()方法，返回一TaskScheduler对象，
     * 此时this.TaskScheduler 为null , 但是在判断中 initHandlerRegistry().requiresTaskScheduler() 检验调度器是否必须时，
     * 由于使用@Bean及@Nullable允许创建一个Bean对象为Null（这就是为报错埋下的一个大坑）
     * <p>
     * b. 在此时初始化spring容器中，就存在类名为defaultSockJsTaskScheduler，类型TaskScheduler的对象，但是此对象为null
     * <p>
     * c.由于使用@EnableScheduling注解开启spring任务调度器，之后系统在初始化Spring自带的调度器时，
     * ScheduledAnnotationBeanPostProcessor.class 在初始化是会在Spring容器中加载TaskScheduler对象时会根据类型（TaskScheduler）去spring容器中获取对象，
     * 这时候就获取到了在websocket加载后创建为null的defaultSockJsTaskScheduler对象。（这才是导致报错的根本原因）
     * <p>
     * <p>
     * 解决方案依旧是自己重新创建一个TaskScheduler Bean对象到spring容器，来填上websocket配置初始化加载创建一个TaskScheduler为 null的对象 产生的坑
     *
     * @return
     */
    // @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduling = new ThreadPoolTaskScheduler();
        scheduling.setPoolSize(10);
        scheduling.initialize();
        return scheduling;
    }
}

/**
 * websocket处理程序
 * <p>
 * AbstractWebSocketHandler 是一个抽象类，用于处理 WebSocket 消息和事件。
 * 它定义了一组方法，可以重写这些方法来处理 WebSocket 连接的不同阶段，例如连接建立、消息处理、连接关闭等。
 * 开发人员可以通过继承 AbstractWebSocketHandler 并重写其方法来实现自定义的 WebSocket 处理逻辑。
 * 提供了一些便捷方法用于发送消息、获取会话信息等。
 *
 * @author woaixuexi
 * @since 2024/3/11 20:41
 */
@Slf4j
class SpringWebSocketSessionManage extends AbstractWebSocketHandler {

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

/**
 * 握手拦截器,用于拦截WebSocket握手过程中的请求。
 * <p>
 * HttpSessionHandshakeInterceptor 是用于 WebSocket 握手过程的拦截器。
 * 在 WebSocket 建立连接之前，会经过一次 HTTP 握手过程，这个过程中可以通过拦截器来进行一些预处理或校验。
 * 开发人员可以通过实现 HttpSessionHandshakeInterceptor 接口，并重写其中的方法来定义自己的握手拦截逻辑。
 * 其中最常用的方法是 beforeHandshake()，可以在 WebSocket 握手之前执行一些逻辑，比如校验用户身份、设置一些 WebSocket 相关的属性等。
 *
 * @author woaixuexi
 * @since 2024/3/11 20:39
 */
@Slf4j
class SpringWebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // beforeHandshake方法在WebSocket握手之前被调用，在这个方法中可以针对握手请求做一些处理。在这个例子中，它打印了握手请求的远程地址，然后调用父类的beforeHandshake方法。
        log.info("websocket握手前 -> remoteAddress: {}, attribute: {}", request.getRemoteAddress(), attributes);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // afterHandshake方法在WebSocket握手之后被调用，可以在这个方法中处理握手完成后的逻辑。在这个例子中，它同样打印了握手请求的远程地址。如果握手过程中出现异常，该方法也会被调用。最后也调用了父类的afterHandshake方法。
        log.info("websocket握手后 -> remoteAddress: {}", request.getRemoteAddress());
        super.afterHandshake(request, response, wsHandler, ex);
    }
}

/*
在这个上下文中，握手指的是 WebSocket 握手。WebSocket 握手是指客户端（浏览器或应用程序）与服务器之间建立 WebSocket 连接的初始握手过程。

具体来说，WebSocket 握手包括以下步骤：

1. **客户端发起握手请求**：
   当客户端尝试通过 WebSocket 连接到服务器时，它会发送一个特殊的 HTTP 请求，称为 WebSocket 握手请求。这个请求会带有一些特定的标头，其中包括协议版本、连接方式等信息。

2. **服务器响应握手请求**：
   服务器接收到客户端的 WebSocket 握手请求后，会进行解析并验证该请求。如果服务器支持 WebSocket 并且请求格式正确，服务器会发送一个 HTTP 响应，称为 WebSocket 握手响应。这个响应中包含了一些信息，如同意切换协议至 WebSocket、加密算法等。

3. **建立 WebSocket 连接**：
   一旦客户端收到了服务器的握手响应，WebSocket 连接就正式建立起来了。此时客户端和服务器之间就可以通过 WebSocket 协议进行全双工的通信，而不再需要通过传统的 HTTP 请求和响应来交换数据。

在上述代码中，`SpringWebSocketInterceptor` 类继承自 `HttpSessionHandshakeInterceptor`，重写了 `beforeHandshake` 和 `afterHandshake` 方法。这意味着这个拦截器可以介入 WebSocket 握手的过程，在握手之前和之后执行一些逻辑处理，例如记录远程地址、设置属性等操作。
 */
package com.hundsun.demo.springboot.js.websocket.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 握手拦截器,用于拦截WebSocket握手过程中的请求。
 *
 * @author woaixuexi
 * @since 2024/3/11 20:39
 */

@Slf4j
@Component
public class SpringWebSocketInterceptor extends HttpSessionHandshakeInterceptor {

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

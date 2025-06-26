package org.hulei.springboot.spring.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WebSocketScopedBean {
    public WebSocketScopedBean() {
        System.out.println("创建了 WebSocketScopedBean 实例");
    }
}

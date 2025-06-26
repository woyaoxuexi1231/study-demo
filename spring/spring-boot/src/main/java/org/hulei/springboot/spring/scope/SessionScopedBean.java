package org.hulei.springboot.spring.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
/*
每个 Session 内部共享同一个实例，不同 Session 拥有不同实例。
 */
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedBean {
    public SessionScopedBean() {
        System.out.println("创建了 SessionScopedBean 实例");
    }
}

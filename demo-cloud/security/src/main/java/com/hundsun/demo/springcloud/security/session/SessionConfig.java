package com.hundsun.demo.springcloud.security.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author hulei
 * @since 2025/8/1 17:45
 */

@Configuration
public class SessionConfig {

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        /*
        HttpSessionEventPublisher 本身继承 HttpSessionListener
        接收有关 HttpSession 生命周期更改的通知事件

        在收到会话销毁的通知时会传递一个 HttpSessionDestroyedEvent 事件，这个事件会被 SessionRegistryImpl 捕获
        SessionRegistryImpl 收到事件后会根据 session id 清除相关的会话信息

        本身 web 服务器负责存储 session 信息，tomcat中使用 StandardManager 来存储session，内部使用 map 存储

        💡主要解决的问题：
            在不配置这个监听器的情况下，会存在无法正常处理HttpSessionDestroyedEvent事件而导致的退出后session信息依旧存在，后续登录全部无法正常登录
         */
        return new HttpSessionEventPublisher();
    }
}

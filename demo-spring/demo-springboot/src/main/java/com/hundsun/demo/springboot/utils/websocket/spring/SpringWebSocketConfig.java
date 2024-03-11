package com.hundsun.demo.springboot.utils.websocket.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

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

    @Autowired
    SpringWebSocketHandler springWebSocketHandler;

    @Autowired
    SpringWebSocketInterceptor springWebSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(springWebSocketHandler, "/myWsSpring")
                .addInterceptors(springWebSocketInterceptor)
                .setAllowedOrigins("*");
    }

    /**
     * TODO
     * a.根据报错中defaultSockJsTaskScheduler找到websocket源码如下，
     * 在初始化websocket时WebSocketConfigurationSupport.class 类中会执行defaultSockJsTaskScheduler()方法，返回一TaskScheduler对象，
     * 此时this.TaskScheduler 为null , 但是在判断中 initHandlerRegistry().requiresTaskScheduler() 检验调度器是否必须时，
     * 由于使用@Bean及@Nullable允许创建一个Bean对象为Null（这就是为报错埋下的一个大坑）
     *
     * b. 在此时初始化spring容器中，就存在类名为defaultSockJsTaskScheduler，类型TaskScheduler的对象，但是此对象为null
     *
     * c.由于使用@EnableScheduling注解开启spring任务调度器，之后系统在初始化Spring自带的调度器时，
     * ScheduledAnnotationBeanPostProcessor.class 在初始化是会在Spring容器中加载TaskScheduler对象时会根据类型（TaskScheduler）去spring容器中获取对象，
     * 这时候就获取到了在websocket加载后创建为null的defaultSockJsTaskScheduler对象。（这才是导致报错的根本原因）
     *
     *
     * 解决方案依旧是自己重新创建一个TaskScheduler Bean对象到spring容器，来填上websocket配置初始化加载创建一个TaskScheduler为 null的对象 产生的坑
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduling = new ThreadPoolTaskScheduler();
        scheduling.setPoolSize(10);
        scheduling.initialize();
        return scheduling;
    }
}

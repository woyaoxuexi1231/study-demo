package org.hulei.springboot.redis.advanced;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Configuration
@Slf4j
@RestController
@RequestMapping(value = "/sub-pub")
@Component
public class RedisSubPubController {


    /*========================================== redis消息队列 =======================================*/

    /*
    Redis 的 Pub/Sub（发布/订阅）是一种消息通信模式，允许客户端订阅（Subscribe）频道（Channel）并接收其他客户端发布（Publish）到该频道的消息。
    它适用于 实时消息推送、事件通知、聊天系统 等场景。

    Pub/Sub 基础命令
        SUBSCRIBE channel1 channel2	订阅一个或多个频道
        PUBLISH channel "message"	向指定频道发布消息
        UNSUBSCRIBE [channel]	退订频道（不指定则退订所有）
        PSUBSCRIBE pattern*	使用通配符订阅（如 news.*）
        PUNSUBSCRIBE pattern*	退订通配符频道
        PUBSUB CHANNELS [pattern]	查看活跃频道


     */

    /**
     * 创建一个消息通道类, 可以用这个类来发送和接收消息
     * <p>
     * ChannelTopic 是 Spring Data Redis 中用于表示消息通道的类。
     * 它提供了一种简单的方式来定义和操作 Redis 中的消息通道。
     * 通常在使用 Redis 进行消息发布和订阅时，你需要指定一个通道（channel）来发送和接收消息。ChannelTopic 就是用来表示这个通道的。
     *
     * @return ChannelTopic
     */
    // @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("spring-channel-mq");
    }

    /**
     * 创建一个监听容器, 这个监听容器主要用来管理和启动我们定义的监听器和监听连接
     * <p>
     * `RedisMessageListenerContainer` 是 Spring Data Redis 中的一个类，用于管理 Redis 消息监听器（`MessageListener`）的容器。它负责管理消息监听器的注册和启动，以及处理接收到的消息。
     * 在 Spring Data Redis 中，`RedisMessageListenerContainer` 类是 `org.springframework.data.redis.listener.RedisMessageListenerContainer`。它是一个用于注册和管理 Redis 消息监听器的重要组件。
     * `RedisMessageListenerContainer` 主要负责以下几个任务：
     * 1. 注册消息监听器：你可以通过 `addMessageListener` 方法将消息监听器注册到 `RedisMessageListenerContainer` 中，以便监听指定的 Redis 通道或模式。
     * 2. 启动消息监听器：一旦消息监听器注册完成，`RedisMessageListenerContainer` 负责启动这些监听器，以开始接收 Redis 服务器发送的消息。
     * 3. 处理消息：当消息到达时，`RedisMessageListenerContainer` 会将消息委派给相应的消息监听器，并调用其 `onMessage` 方法来处理消息。
     * 4. 管理连接：`RedisMessageListenerContainer` 还负责管理与 Redis 服务器的连接，确保连接的正确性和稳定性。
     * 总之，`RedisMessageListenerContainer` 提供了一个高级别的接口，简化了在 Spring 应用程序中使用 Redis 进行消息监听和处理的复杂性。
     *
     * @param connectionFactory redis的连接工厂
     * @return 返回这个监听容器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // container.afterPropertiesSet();
        container.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
        // container.start();
        container.setMaxSubscriptionRegistrationWaitingTime(10000000);
        // this.container = container;
        return container;
    }

    /**
     * redis操作类
     */
    @Autowired
    @Qualifier(value = "strObjRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisMessageListenerContainer container;

    Map<String, MessageListener> listeners = new HashMap<>();


    /* ==================================================== 动态订阅 ===================================================== */
    /*
    🚨目前这个订阅存在问题，在订阅后，取消订阅，再次订阅是就报错了

    25-08-07 15:51:56.301 ERROR[http-nio-10026-exec-3] org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet](175) - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.IllegalStateException: Subscription registration timeout exceeded.] with root cause
        java.util.concurrent.TimeoutException: null
            at java.util.concurrent.CompletableFuture.timedGet(CompletableFuture.java:1888) ~[?:?]
            at java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2023) ~[?:?]
            at org.springframework.data.redis.listener.RedisMessageListenerContainer.lazyListen(RedisMessageListenerContainer.java:269) ~[spring-data-redis-2.7.18.jar:2.7.18]
            at org.springframework.data.redis.listener.RedisMessageListenerContainer.addListener(RedisMessageListenerContainer.java:665) ~[spring-data-redis-2.7.18.jar:2.7.18]
            at org.springframework.data.redis.listener.RedisMessageListenerContainer.addMessageListener(RedisMessageListenerContainer.java:537) ~[spring-data-redis-2.7.18.jar:2.7.18]
            at org.springframework.data.redis.listener.RedisMessageListenerContainer.addMessageListener(RedisMessageListenerContainer.java:548) ~[spring-data-redis-2.7.18.jar:2.7.18]
            at org.hulei.springboot.redis.advanced.RedisSubPubController.subscribe(RedisSubPubController.java:143) ~[classes/:?]


    💡debug源码的过程中始终没有找到问题，最后我在代码中发现最终调用订阅时会分发到不同的客户端，而这里我是引入了 redisson的，我猜测可能是 redisson 的问题
      去掉 redisson 的客户端后果然没问题了
     */

    @PostMapping("/subscribe/{topic}")
    public void subscribe(@PathVariable(value = "topic") String topic) {
        // 用于向容器中添加消息监听器，并指定要监听的主题（Topic）。
        // 这里实际执行 subscribe channel spring-channel-mq
        // redisMessageListenerContainer 需要我们自己定义
        MessageListener messageListener = (message, pattern) -> {
            StringRedisSerializer serializer = new StringRedisSerializer();
            String channel = serializer.deserialize(message.getChannel());
            String msg = serializer.deserialize(message.getBody());
            log.info("redis MessageListener 收到消息： channel: {}, 信道(主题) {}, 消息为: {}",
                    channel,
                    Objects.isNull(pattern) ? null : new String(pattern, StandardCharsets.UTF_8),
                    msg
            );
        };
        container.addMessageListener(
                messageListener,
                new ChannelTopic(topic)
        );
        listeners.put(topic, messageListener);
        log.info("成功订阅 {}", topic);
    }

    @PostMapping("/unsubscribe/{topic}")
    public void unsubscribe(@PathVariable(value = "topic") String topic) {
        container.removeMessageListener(listeners.get(topic));
        listeners.remove(topic);
        log.info("成功退订 {}", topic);
    }

    @PostMapping(value = "/publish/{topic}")
    public void publish(@PathVariable(value = "topic") String topic) {
        // 这里实际执行的是 PUBLISH channel "message"	向指定频道发布消息
        redisTemplate.convertAndSend(topic, DateUtil.date().toString());
        log.info("使用 redisTemplate 发消息, 主题: {}, msg: {}", topic, DateUtil.date());
    }

}

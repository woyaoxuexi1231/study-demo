package org.hulei.keeping.server.redis;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Configuration
@Slf4j
@RestController
@RequestMapping(value = "/redisChannelSubscribe")
@Component
public class RedisSpringForMQ {


    /*========================================== redis消息队列 =======================================*/

    /**
     * 创建一个消息通道类, 可以用这个类来发送和接收消息
     * <p>
     * ChannelTopic 是 Spring Data Redis 中用于表示消息通道的类。
     * 它提供了一种简单的方式来定义和操作 Redis 中的消息通道。
     * 通常在使用 Redis 进行消息发布和订阅时，你需要指定一个通道（channel）来发送和接收消息。ChannelTopic 就是用来表示这个通道的。
     *
     * @return ChannelTopic
     */
    @Bean
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
        // 用于向容器中添加消息监听器，并指定要监听的主题（Topic）。
        container.addMessageListener(
                (message, pattern) -> {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    String channel = serializer.deserialize(message.getChannel());
                    String msg = serializer.deserialize(message.getBody());
                    log.info("channel: {}, 信道(主题) {}, 消息为: {}", channel, Objects.isNull(pattern) ? null : new String(pattern, StandardCharsets.UTF_8), msg);
                },
                channelTopic());
        return container;
    }

    /**
     * redis操作类
     */
    @Autowired
    @Qualifier(value = "strObjRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 消息通道类,这里通过这个类进行发送消息
     */
    @Autowired
    private ChannelTopic channelTopic;

    @RequestMapping(value = "/publish")
    public void publish() {
        redisTemplate.convertAndSend(channelTopic.getTopic(), DateUtil.date().toString());
        log.info("使用 redisTemplate 发消息, 主题: {}, msg: {}", channelTopic.getTopic(), DateUtil.date());
    }

}

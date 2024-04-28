package com.hundsun.demo.springboot.redis.pub;

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
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    public ChannelTopic channelTopic() {
        /*
        ChannelTopic 是 Spring Data Redis 中用于表示消息通道的类。
        它提供了一种简单的方式来定义和操作 Redis 中的消息通道。
        通常在使用 Redis 进行消息发布和订阅时，你需要指定一个通道（channel）来发送和接收消息。ChannelTopic 就是用来表示这个通道的。
         */
        return new ChannelTopic("message_channel");
    }

    @Bean(name = "redisMessageSubscriber")
    public MessageListener redisMessageSubscriber() {
        /*
        MessageListener 是 Spring Data Redis 中用于监听 Redis 消息的接口。它定义了处理接收到的消息的方法。
        在使用 Redis 进行消息订阅时，你通常会创建一个类来实现 MessageListener 接口，并实现 onMessage 方法来处理接收到的消息。

        Message message: 这是接收到的消息对象。在 Spring Data Redis 中，Message 是一个包含了消息内容的对象，通常是字节数组（byte array）。你可以通过调用 message.getBody() 方法来获取消息的内容。
        byte[] pattern: 这是匹配的模式，用于指示消息是由哪个模式匹配的。在 Redis 中，你可以使用订阅模式（subscribe pattern）来订阅多个通道，当消息到达时，Redis 会通知所有匹配模式的订阅者。如果你没有使用订阅模式，这个参数通常为 null。
         */
        return (message, pattern) -> {
            StringRedisSerializer serializer = new StringRedisSerializer();
            String channel = serializer.deserialize(message.getChannel());
            String msg = serializer.deserialize(message.getBody());

            log.info(String.format("Received message: %s from channel: %s, pattern %s", msg, channel, Objects.isNull(pattern) ? null : new String(pattern, StandardCharsets.UTF_8)));
        };
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            @Autowired @Qualifier(value = "redisMessageSubscriber") MessageListener redisMessageSubscriber) {
        /*
        `RedisMessageListenerContainer` 是 Spring Data Redis 中的一个类，用于管理 Redis 消息监听器（`MessageListener`）的容器。它负责管理消息监听器的注册和启动，以及处理接收到的消息。
        在 Spring Data Redis 中，`RedisMessageListenerContainer` 类是 `org.springframework.data.redis.listener.RedisMessageListenerContainer`。它是一个用于注册和管理 Redis 消息监听器的重要组件。
        `RedisMessageListenerContainer` 主要负责以下几个任务：
        1. 注册消息监听器：你可以通过 `addMessageListener` 方法将消息监听器注册到 `RedisMessageListenerContainer` 中，以便监听指定的 Redis 通道或模式。
        2. 启动消息监听器：一旦消息监听器注册完成，`RedisMessageListenerContainer` 负责启动这些监听器，以开始接收 Redis 服务器发送的消息。
        3. 处理消息：当消息到达时，`RedisMessageListenerContainer` 会将消息委派给相应的消息监听器，并调用其 `onMessage` 方法来处理消息。
        4. 管理连接：`RedisMessageListenerContainer` 还负责管理与 Redis 服务器的连接，确保连接的正确性和稳定性。
        总之，`RedisMessageListenerContainer` 提供了一个高级别的接口，简化了在 Spring 应用程序中使用 Redis 进行消息监听和处理的复杂性。
         */
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 用于向容器中添加消息监听器，并指定要监听的主题（Topic）。
        container.addMessageListener(redisMessageSubscriber, channelTopic());
        return container;
    }

    @Bean(name = "stringObjRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }
}

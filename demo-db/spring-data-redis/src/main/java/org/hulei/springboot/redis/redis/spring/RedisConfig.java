package org.hulei.springboot.redis.redis.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

// @EnableTransactionManagement
@Slf4j
@Configuration
public class RedisConfig {

    @Bean(name = "strObjRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        // template.setEnableTransactionSupport(true);
        return template;
    }

    /**
     * 设置 redisTemplate 的编码
     *
     * @param redisTemplate
     */
    @Autowired
    @Qualifier(value = "redisTemplate")
    public void redisTemplateSerializerInit(RedisTemplate<Object, Object> redisTemplate) {
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
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
        return container;
    }
}

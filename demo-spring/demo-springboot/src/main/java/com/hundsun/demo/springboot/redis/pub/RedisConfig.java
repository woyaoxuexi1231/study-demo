package com.hundsun.demo.springboot.redis.pub;

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

@Configuration
public class RedisConfig {

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("message_channel");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            @Autowired @Qualifier(value = "redisMessageSubscriber") MessageListener redisMessageSubscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisMessageSubscriber, channelTopic());
        return container;
    }

    @Bean
    public MessageListener redisMessageSubscriber() {
        return (message, pattern) -> {
            StringRedisSerializer serializer = new StringRedisSerializer();
            String channel = serializer.deserialize(message.getChannel());
            String msg = serializer.deserialize(message.getBody());

            System.out.println(String.format("Received message: %s from channel: %s", msg, channel));
        };
    }


    // @Bean
    // RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
    //                                         MessageListenerAdapter listenerAdapter) {
    //
    //     RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    //     container.setConnectionFactory(connectionFactory);
    //     container.addMessageListener(listenerAdapter, new PatternTopic("message_channel"));
    //
    //     return container;
    // }
    //
    // @Bean
    // MessageListenerAdapter listenerAdapter(RedisMessageSubscriber subscriber) {
    //     return new MessageListenerAdapter(subscriber);
    // }
    //
    // @Bean
    // RedisMessageSubscriber subscriber() {
    //     return new RedisMessageSubscriber();
    // }

    @Bean(name = "stringObjRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }
}

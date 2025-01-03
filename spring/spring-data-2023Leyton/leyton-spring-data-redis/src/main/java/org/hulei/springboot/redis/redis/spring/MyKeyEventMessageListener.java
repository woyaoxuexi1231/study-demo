package org.hulei.springboot.redis.redis.spring;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/10/16 21:32
 */

@Slf4j
@Configuration
public class MyKeyEventMessageListener {

    /**
     * 通过使用  notify-keyspace-events 配置和 KeyExpirationEventMessageListener 可以实现redis的延迟队列
     * 但是存在以下问题:
     * - 1. 消息可靠性无法保证,redis空间键通知本身是一种尽力而为的通知机制,并不保证消息可靠性
     * - 2. 没有消息的确认机制,无法确保消息能否被成功消费
     * - 3. 多个订阅者都会收到消息,还需要额外的机制确保不会重复消费
     * - 4. 消息不会持久化,也就是说键过期了,如果没有消费者订阅消费这个消息,那么在那个时刻之后,这个消息就已经消失了
     * 还有一些其他存在的问题:
     * - 1. 性能问题,如果存在大量频繁的过期操作,redis会消耗大量的资源来做这件事
     * - 2. 扩展性问题,这并不是一个完整的延迟队列的解决方案
     *
     * @param listenerContainer
     * @return
     */
    @Bean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        return new KeyExpirationEventMessageListener(listenerContainer) {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                log.info("收到 message body: {}, message channel: {}, pattern: {}",
                        new String(message.getBody(), StandardCharsets.UTF_8),
                        new String(message.getChannel(), StandardCharsets.UTF_8),
                        new String(pattern, StandardCharsets.UTF_8));
                StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
                stringRedisTemplate.opsForValue().set(new String(message.getBody(), StandardCharsets.UTF_8), "true");
                stringRedisTemplate.expire(new String(message.getBody(), StandardCharsets.UTF_8), 10, TimeUnit.SECONDS);
            }
        };
    }

}


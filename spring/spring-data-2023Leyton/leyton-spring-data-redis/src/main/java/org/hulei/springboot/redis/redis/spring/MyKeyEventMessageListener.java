package org.hulei.springboot.redis.redis.spring;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
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

    /*
    通过使用 notify-keyspace-events 配置和 KeyExpirationEventMessageListener 可以实现redis的延迟队列
    但是存在以下问题:
        1. 消息可靠性无法保证,redis空间键通知本身是一种尽力而为的通知机制,并不保证消息可靠性
        2. 没有消息的确认机制,无法确保消息能否被成功消费
        3. 多个订阅者都会收到消息,还需要额外的机制确保不会重复消费
        4. 消息不会持久化,也就是说键过期了,如果没有消费者订阅消费这个消息,那么在那个时刻之后,这个消息就已经消失了
    还有一些其他存在的问题:
        1. 性能问题,如果存在大量频繁的过期操作,redis会消耗大量的资源来做这件事
        2. 扩展性问题,这并不是一个完整的延迟队列的解决方案

    notify-keyspace-events 可以设置为以下各项的组合：
        – K 键空间通知，所有通知事件都以keyspace@为前缀，并通过 PUBLISH 命令发送
        – E 键事件通知，所有通知事件都以keyevent@为前缀，并通过 PUBLISH 命令发送
        – g DEL、EXPIRE、RENAME等生成通知
        – s SET、EXPIREAT等生成通知
        – h HSET、HDEL等生成通知
        – l LSET、LREM等生成通知
        – z ZADD、ZREM等生成通知
        – x 过期事件：当键被设置了过期时间时，会生成通知
        – e 驱逐事件：当键因为空间被驱逐出去时，会生成通知
        – A 任何事件都会生成通知
    config get notify-keyspace-events 这个命令可以获取通知事件的配置
    config get * 可以获取所有的配置
     */


    /**
     * 使用 spring-data-redis 提供的 KeyExpirationEventMessageListener 来订阅过期键的通知信息
     *
     * @param listenerContainer
     * @return
     */
    @Bean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        /*
        其实使用这个监听类 可以理解为使用 __keyevent@0__:expire
         */
        return new KeyExpirationEventMessageListener(listenerContainer) {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                log.info("KeyExpirationEventMessageListener 收到键过期的通知信息 message body: {}, message channel: {}, pattern: {}",
                        new String(message.getBody(), StandardCharsets.UTF_8),
                        new String(message.getChannel(), StandardCharsets.UTF_8),
                        new String(pattern, StandardCharsets.UTF_8));
                StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

                log.info("KeyExpirationEventMessageListener 将进行过期键的续约...");
                stringRedisTemplate.opsForValue().set(
                        new String(message.getBody(), StandardCharsets.UTF_8),
                        "true",
                        10,
                        TimeUnit.SECONDS
                );

                // redisTemplate 也提供了更底层的执行方法，可以用但没必要
                // stringRedisTemplate.execute((RedisCallback<Object>) connection ->
                //         connection.setEx(
                //                 message.getBody(),
                //                 TimeUnit.SECONDS.toSeconds(10),
                //                 "true".getBytes(StandardCharsets.UTF_8)));
            }
        };
    }

    @Autowired
    public void registry(RedisMessageListenerContainer listenerContainer) {

        /*
        键空间通知（Keyspace Notification），它表示某个具体 key 上发生了什么事件。
        频道格式：__keyspace@<db_index>__:<key_name>
        消息内容：事件类型，如 set、expired、del 等。
                比如监听某一类事件的发生
         */
        // listenerContainer.addMessageListener((msg, pattern) -> {
        //     log.info("键空间事件(__keyspace)监听: msgBody: {}, msgChannel: {}", new String(msg.getBody(), StandardCharsets.UTF_8), new String(msg.getChannel(), StandardCharsets.UTF_8));
        // }, new PatternTopic("__keyspace@0__:*"));

        /*
        键事件通知（Keyevent Notification），它表示某种事件发生时，哪些 key 触发了这个事件。
        频道格式：__keyevent@<db_index>__:<event_type>
        消息内容：具体是哪个 key 触发了事件。
                比如监听某一个键发生的变化
         */
        // listenerContainer.addMessageListener((msg, pattern) -> {
        //     log.info("键事件(__keyevent): msgBody: {}, msgChannel: {}", new String(msg.getBody(), StandardCharsets.UTF_8), new String(msg.getChannel(), StandardCharsets.UTF_8));
        // }, new PatternTopic("__keyevent@0__:*"));

    }

}


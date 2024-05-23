package com.hundsun.demo.springboot.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * redis消息队列,这是比较原始的方式,直接我们自己写实现
 * <p>
 * 使用redis的list这种数据结构,使用队列的左侧加入,右侧阻塞弹出来实现
 */

@Slf4j
@Component
public class RedisMessageQueue {

    /**
     * 使用到的消息队列名
     */
    private static final String QUEUE_NAME = "message_queue";

    /**
     * spring-redis默认提供的 k,v 都是字符串类型的工具类
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送消息
     * 使用 lpop 往队列内塞入消息
     *
     * @param message msg
     */
    public void produce(String message) {
        redisTemplate.opsForList().leftPush(QUEUE_NAME, message);
    }

    /**
     * 消费消息
     *
     * @return msg
     */
    public String consume() {
        return redisTemplate.opsForList().rightPop(QUEUE_NAME);
    }

    /**
     * 阻塞式获取消息
     *
     * @return msg
     */
    public String consumeWithBlock() {
        // 超时时间为0,将一直阻塞
        return redisTemplate.opsForList().rightPop(QUEUE_NAME, 0, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                try {
                    this.produce(Calendar.getInstance().getTime().toString());
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                log.info(consumeWithBlock());
            }
        }).start();
    }
}


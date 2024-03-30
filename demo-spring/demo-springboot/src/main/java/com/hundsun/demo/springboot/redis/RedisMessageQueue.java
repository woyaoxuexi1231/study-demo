package com.hundsun.demo.springboot.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Calendar;

@Slf4j
// @Component
public class RedisMessageQueue {
    private static final String QUEUE_NAME = "message_queue";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void produce(String message) {
        redisTemplate.opsForList().leftPush(QUEUE_NAME, message);
        // System.out.println("Produced message: " + message);
    }

    public String consume() {
        return redisTemplate.opsForList().rightPop(QUEUE_NAME);
    }

    public String consumeWithBlock() {
        return redisTemplate.opsForList().rightPop(QUEUE_NAME, Duration.ofDays(0));
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


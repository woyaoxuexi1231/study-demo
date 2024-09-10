package org.hulei.keeping.server.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * redis消息队列,这是比较原始的方式,直接我们自己写实现
 * <p>
 * 使用redis的list这种数据结构,使用队列的左侧加入,右侧阻塞弹出来实现
 */

@Slf4j
@RestController("/redisListMQ")
public class RedisMQByList {

    /**
     * 使用到的消息队列名
     */
    private static final String QUEUE_NAME = "redis-list-mq";

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
        // rightPop 将从队列的右侧阻塞获取消息
        return redisTemplate.opsForList().rightPop(QUEUE_NAME);
    }

    /**
     * 阻塞式获取消息
     *
     * @return msg
     */
    public String consumeWithBlock() {
        // 超时时间为 0,将一直阻塞
        return redisTemplate.opsForList().rightPop(QUEUE_NAME, 0, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                try {
                    log.info("这是一条使用 redis list 实现的消息队列收到的消息, {}", consumeWithBlock());
                } catch (Exception e) {
                    log.error("消息接收异常,线程将停止", e);
                    break;
                }
            }
        }, "redis-list-queue-consume").start();
    }

    @GetMapping("/produce")
    public void produceAsync(String message) {
        produce(message);
    }
}


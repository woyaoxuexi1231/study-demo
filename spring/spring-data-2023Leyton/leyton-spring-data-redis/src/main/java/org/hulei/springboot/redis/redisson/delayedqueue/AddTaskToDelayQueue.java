package org.hulei.springboot.redis.redisson.delayedqueue;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class AddTaskToDelayQueue {

    @Autowired
    RedissonClient redissonClient;

    /**
     * 添加任务到延时队列里面
     *
     * @param timeout 过期时间
     */
    public void addTaskToDelayQueue(Integer timeout) {
        // RBlockingDeque的实现类为:new RedissonBlockingDeque
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque("orderQueue");
        // RDelayedQueue的实现类为:new RedissonDelayedQueue
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "添加任务到延时队列里面");
        delayedQueue.offer("testRedissonDelayQueue", timeout, TimeUnit.SECONDS);
        // delayedQueue.offer(orderId +"添加二个任务", 20, TimeUnit.SECONDS);
        // delayedQueue.offer(orderId +"添加三个任务", 30, TimeUnit.SECONDS);

    }
}

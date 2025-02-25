package org.hulei.springboot.redis.redisson.delayedqueue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
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

        /*
        redisson_delay_queue_timeout:{队列名} 有序集合 过期时间作为分数
        redisson_delay_queue:{队列名} 队列list, 按顺序保存所有元素
        redisson_delay_queue_channel:{队列名} 会创建一个监听信道

        保存的每个元素即使是一样的, redisson也会处理成不一样的, 会使用随机数来保证所有元素的唯一性

        RedissonDelayedQueue被创建时会创建一个 task
        task会订阅redisson_delay_queue_channel这个信道
        task:
            订阅成功时会触发事件 - 会触发task的pushTaskAsync逻辑,也就是会触发一次redisson_delay_queue_timeout的过期键处理
                               会获取最近的一个未过期的键的过期时间还剩多少, 如果有 会创建一个延迟任务,到时间后会继续执行 pushTask




         */
        log.info("contains : {}", delayedQueue.contains("testRedissonDelayQueue"));
        if (!delayedQueue.contains("testRedissonDelayQueue")) {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "添加任务到延时队列里面");
            delayedQueue.offer("testRedissonDelayQueue", timeout, TimeUnit.SECONDS);
        }
        // delayedQueue.offer(orderId +"添加二个任务", 20, TimeUnit.SECONDS);
        // delayedQueue.offer(orderId +"添加三个任务", 30, TimeUnit.SECONDS);

    }
}

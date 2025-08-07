package org.hulei.springboot.redis.redisson.basic;

import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

public class QueueExample {
    public static void main(String[] args) {

        RedissonClient redisson = RedissonConnectionFactory.getRedissonClient();

        RQueue<String> queue = redisson.getQueue("myDistributedQueue");
        queue.add("task1");
        queue.add("task2");

        String task = queue.poll();
        System.out.println("Processing: " + task);

        redisson.shutdown();
    }
}

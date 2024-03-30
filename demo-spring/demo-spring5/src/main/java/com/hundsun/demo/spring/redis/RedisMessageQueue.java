package com.hundsun.demo.spring.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisMessageQueue {
    private static final String QUEUE_NAME = "message_queue";
    private static final String REDIS_HOST = "192.168.80.128";
    private static final int REDIS_PORT = 6379;

    private Jedis jedis;

    public RedisMessageQueue() {
        this.jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        jedis.auth("123456");
    }

    public void produce(String message) {
        jedis.lpush(QUEUE_NAME, message);
        System.out.println("Produced message: " + message);
    }

    public List<String> consume() {
        return jedis.brpop(0, QUEUE_NAME);
    }

    public static void main(String[] args) {

        // TODO 事实是,这个代码有问题,只会收到一条消息,但是我还不知道原因


        RedisMessageQueue messageQueue = new RedisMessageQueue();

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交生产者任务
        executor.execute(() -> {
            for (int i = 1; i <= 10; i++) {
                String message = "Message " + i;
                try {
                    messageQueue.produce(message);
                    Thread.sleep(1000); // 生产者每隔1秒生产一个消息
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 提交消费者任务
        executor.execute(() -> {
            while (true) {
                try {
                    System.out.println("Consumed message: " + messageQueue.consume());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // 关闭线程池
        executor.shutdown();
    }
}

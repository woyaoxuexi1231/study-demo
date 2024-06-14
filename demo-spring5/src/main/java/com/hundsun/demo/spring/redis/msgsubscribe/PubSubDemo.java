package com.hundsun.demo.spring.redis.msgsubscribe;

public class PubSubDemo {
    public static void main(String[] args) {
        String channel = "testChannel";

        // 启动订阅者监听
        SubscriberListeningThread subscriberThread = new SubscriberListeningThread("192.168.80.128", 6379, "123456", channel);
        subscriberThread.start();

        try {
            Thread.sleep(2000); // 等待订阅者准备就绪
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 发布消息
        RedisPublisher publisher = new RedisPublisher();
        publisher.publish(channel, "Hello, Redis!", "192.168.80.128", 6379, "123456");
    }
}
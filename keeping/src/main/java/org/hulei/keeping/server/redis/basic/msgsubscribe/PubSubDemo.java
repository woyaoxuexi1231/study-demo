package org.hulei.keeping.server.redis.basic.msgsubscribe;

import redis.clients.jedis.Jedis;

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

        try (Jedis jedis = new Jedis("192.168.80.130", 6379)) {
            jedis.auth("123456");
            jedis.publish(channel, "Hello, Redis!");
        }

        System.out.println("结束");
    }
}
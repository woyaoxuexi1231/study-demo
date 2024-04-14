package com.hundsun.demo.spring.redis.msgsubscribe;

import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("接收到消息: Channel: " + channel + ", Msg: " + message);
    }

    // 你可以根据需要重写更多的方法来响应订阅中的不同事件
}
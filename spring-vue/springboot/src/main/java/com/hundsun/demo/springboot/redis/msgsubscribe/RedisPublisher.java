package com.hundsun.demo.springboot.redis.msgsubscribe;

import redis.clients.jedis.Jedis;

public class RedisPublisher {

    public void publish(final String channel, final String message, String host, int port, String auth) {
        try (Jedis jedis = new Jedis(host, port)) {
            jedis.auth(auth);
            jedis.publish(channel, message);
        }
    }
}
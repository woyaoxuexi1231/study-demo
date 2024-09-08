package com.hundsun.demo.springboot.redis.basic;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceExample {

    public static void main(String[] args) {
        // 创建 RedisClient 对象
        // RedisClient redisClient = RedisClient.create("redis://192.168.80.128:6379");

        // 创建 RedisURI 对象，指定 Redis 服务器的连接信息
        RedisURI redisUri = RedisURI.Builder.redis("192.168.80.128", 6379)
                .withPassword("123456")
                .build();

        // 创建 RedisClient 对象，指定 Redis 服务器的连接信息
        RedisClient redisClient = RedisClient.create(redisUri);

        // 创建连接
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            // 获取同步执行命令的 RedisCommands 对象
            RedisCommands<String, String> syncCommands = connection.sync();

            // 执行 Redis 命令
            syncCommands.set("key1", "value1");
            String value = syncCommands.get("key1");
            System.out.println("Value of key1: " + value);

            // 执行批量操作
            syncCommands.multi();
            syncCommands.set("key2", "value2");
            syncCommands.set("key3", "value3");
            syncCommands.exec();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭 RedisClient
            redisClient.shutdown();
        }
    }
}

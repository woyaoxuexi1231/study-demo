package org.hulei.keeping.server.redis.basic;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class JedisPoolExample {
    private static final String REDIS_HOST = "192.168.80.128";
    private static final int REDIS_PORT = 6379;

    private static final JedisPool jedisPool;

    static {
        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // 最大连接数
        poolConfig.setMaxIdle(5); // 最大空闲连接数
        poolConfig.setMinIdle(1); // 最小空闲连接数

        // 创建 Jedis 连接池
        jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT, Protocol.DEFAULT_TIMEOUT, "123456");
    }

    public static void main(String[] args) {
        // 从连接池获取 Jedis 实例
        try (Jedis jedis = jedisPool.getResource()) {
            // 设置键值对
            jedis.set("example_key", "example_value");

            // 获取键值对
            String value = jedis.get("example_key");
            System.out.println("Value: " + value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 关闭连接池
        jedisPool.close();
    }
}

package com.hundsun.demo.spring.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisPipelineExample {
    public static void main(String[] args) {
        // 创建 Jedis 客户端连接
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 创建 Pipeline 对象
        Pipeline pipeline = jedis.pipelined();

        // 向 Pipeline 中添加命令
        for (int i = 0; i < 1000; i++) {
            pipeline.set("key_" + i, "value_" + i);
        }

        // 执行 Pipeline 中的所有命令
        pipeline.sync();

        // 关闭 Jedis 连接
        jedis.close();
    }
}

package com.hundsun.demo.spring.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisDataException;

public class RedisTransactionExample {
    public static void main(String[] args) {
        // 创建 Jedis 客户端连接
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 开启事务
        Transaction tx = jedis.multi();

        try {
            // 将命令添加到事务队列中
            tx.set("key1", "value1");
            tx.set("key2", "value2");

            // 执行事务
            tx.exec();
        } catch (JedisDataException e) {
            // 如果出现异常，回滚事务
            tx.discard();
            e.printStackTrace();
        }

        // 关闭 Jedis 连接
        jedis.close();
    }
}

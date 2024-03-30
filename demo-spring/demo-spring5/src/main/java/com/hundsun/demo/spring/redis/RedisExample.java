package com.hundsun.demo.spring.redis;

import redis.clients.jedis.Jedis;

public class RedisExample {
    public static void main(String[] args) {
        // 连接 Redis 服务器
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 字符串（String）
        jedis.set("key1", "value1");
        System.out.println("setex: " + jedis.setex("key2", 10, "value2"));
        System.out.println("setnx: " + jedis.setnx("key3", "value3"));
        System.out.println("String value: " + jedis.get("key1"));

        // 列表（List）
        jedis.lpush("list1", "item1");
        jedis.rpush("list1", "item2");
        System.out.println("List values: " + jedis.lrange("list1", 0, -1));

        // 哈希（Hash）
        jedis.hset("hash1", "field1", "value1");
        System.out.println("Hash value: " + jedis.hget("hash1", "field1"));

        // 集合（Set）
        jedis.sadd("set1", "member1");
        jedis.sadd("set1", "member2");
        System.out.println("Set members: " + jedis.smembers("set1"));

        // 有序集合（Sorted Set）
        jedis.zadd("zset1", 1, "member1");
        jedis.zadd("zset1", 2, "member2");
        System.out.println("Sorted Set members: " + jedis.zrangeWithScores("zset1", 0, -1));

        // 关闭连接
        jedis.close();
    }
}

package com.hundsun.demo.spring.redis.basic;

import redis.clients.jedis.Jedis;

public class RedisExample {

    public static void main(String[] args) {

        // 连接 Redis 服务器
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 字符串（String）
        System.out.println("set: " + jedis.set("key1", "value1"));
        System.out.println("setex: " + jedis.setex("key2", 10L, "value2"));
        System.out.println("setnx: " + jedis.setnx("key3", "value3"));
        System.out.println("String value: " + jedis.get("key1"));

        // 列表（List）
        System.out.println("lpush: " + jedis.lpush("list1", "item1"));
        System.out.println("rpush: " + jedis.rpush("list1", "item2"));
        System.out.println("List values: " + jedis.lrange("list1", 0, -1));

        // 哈希（Hash）
        System.out.println("hset: " + jedis.hset("hash1", "field1", "value1"));
        System.out.println("Hash value: " + jedis.hget("hash1", "field1"));

        // 集合（Set）
        System.out.println("sadd: " + jedis.sadd("set1", "member1"));
        System.out.println("sadd: " + jedis.sadd("set1", "member2"));
        System.out.println("Set members: " + jedis.smembers("set1"));

        // 有序集合（Sorted Set）
        System.out.println("zadd: " + jedis.zadd("zset1", 1, "member1"));
        System.out.println("zadd: " + jedis.zadd("zset1", 2, "member2"));
        System.out.println("Sorted Set members: " + jedis.zrangeWithScores("zset1", 0, -1));

        // 关闭连接
        jedis.close();
    }
}

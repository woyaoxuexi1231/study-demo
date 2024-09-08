package com.hundsun.demo.springboot.redis.basic;

import redis.clients.jedis.Jedis;

public class JedisExample {

    public static void main(String[] args) {

        // 连接 Redis 服务器
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // -------------------------------------------------字符串-------------------------------------------------- //
        // 简单的设置一个 key-value
        System.out.println("set: " + jedis.set("key1", "value1"));
        // 带过期时间 key-value
        System.out.println("setex: " + jedis.setex("key2", 10L, "value2"));
        // 不存在才设置的 kye-value
        System.out.println("setnx: " + jedis.setnx("key3", "value3"));
        // 得到一个 value
        System.out.println("String value: " + jedis.get("key1"));

        // -------------------------------------------------列表-------------------------------------------------- //
        // 左侧插入
        System.out.println("lpush: " + jedis.lpush("list1", "item3"));
        // 右侧插入
        System.out.println("rpush: " + jedis.rpush("list1", "item4"));
        // 范围查询list
        System.out.println("List values: " + jedis.lrange("list1", 0, -1));

        // -------------------------------------------------哈希-------------------------------------------------- //
        // 设置 hash 表
        System.out.println("hset: " + jedis.hset("hash1", "field1", "value1"));
        // 得到 hash 表的值
        System.out.println("Hash value: " + jedis.hget("hash1", "field1"));
        // 得到所有 hash 值
        System.out.println("Hash values: " + jedis.hgetAll("hash1"));

        // -------------------------------------------------集合-------------------------------------------------- //
        // 设置集合值
        System.out.println("sadd: " + jedis.sadd("set1", "member1"));
        System.out.println("sadd: " + jedis.sadd("set1", "member2"));
        System.out.println("sadd: " + jedis.sadd("set1", "member2"));
        System.out.println("Set members: " + jedis.smembers("set1"));

        // -------------------------------------------------有序集合-------------------------------------------------- //
        System.out.println("zadd: " + jedis.zadd("zset1", 1, "member1"));
        System.out.println("zadd: " + jedis.zadd("zset1", 2, "member2"));
        System.out.println("Sorted Set members: " + jedis.zrangeWithScores("zset1", 0, -1));

        // 关闭连接
        jedis.close();
    }
}

package org.hulei.redis.basic.advanced;

import redis.clients.jedis.Jedis;

/*
位图 应用于布隆过滤器
 */

public class RedisBitmap {

    public static void main(String[] args) {

        // 创建 Jedis 客户端连接
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 位图本身不是一种数据结构,他是基于字符串,对字符串的位进行运算

        // 设置位图
        jedis.setbit("user:1:login", 0, true); // 用户1登录
        jedis.setbit("user:2:login", 1, true); // 用户2登录
        jedis.setbit("user:3:login", 2, true); // 用户3登录

        // 获取位图
        boolean user1Login = jedis.getbit("user:1:login", 0); // 获取用户1的登录状态
        boolean user2Login = jedis.getbit("user:2:login", 1); // 获取用户2的登录状态
        boolean user3Login = jedis.getbit("user:3:login", 2); // 获取用户3的登录状态

        // 打印结果
        System.out.println("User 1 login status: " + user1Login);
        System.out.println("User 2 login status: " + user2Login);
        System.out.println("User 3 login status: " + user3Login);

        // 关闭 Jedis 连接
        jedis.close();
    }
}

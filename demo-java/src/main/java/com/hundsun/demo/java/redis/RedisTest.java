package com.hundsun.demo.java.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.redis
 * @className: RedisTest
 * @description: 取自《Redis实战》
 * @author: h1123
 * @createDate: 2023/2/19 14:01
 */

public class RedisTest {

    /*
    Redis - 非关系型数据库
    可以存储五种不同的数据类型 - String, List, Set, Hash, ZSet(有序集合)

    1. 五种不同数据类型的操作指令
    2. Redis 提供了发布/订阅模式
    3. 排序, 基本的事务, 过期时间

    持久化
        快照持久化 - 保存某个时间点上的副本, 提供 BGSAVE 和 SAVE, 前者创建子进程进行备份(不阻塞), 后者不创建子进程(阻塞)
        AOF持久化 - 将被执行的写命令写到 AOF 文件的末尾, 以此来记录数据发生的变化
    复制
        主从复制 - slaveof
            从服务器连接主服务器时的步骤
                主服务器
                    1. 等待从服务器的连接命令
                    2. 开始执行 BGSAVE, 并使用缓冲区记录 BGSAVE 之后执行的所有写命令
                    3. BGSAVE 执行完毕, 想从服务器发送快照文件, 并在发送期间继续使用缓冲区记录的被执行的写命令
                    4. 快照文件发送完毕, 开始向从服务器发送存储在缓冲区里面的写命令
                    5. 缓冲区存储的写命令发送完毕, 从现在开始每执行一个写命令, 就向从服务器发送相同的写命令
                从服务器
                    1. 连接(或者重新连接)主服务器, 发送 SYNC 命令
                    2. 根据配置选项来决定时继续使用现有的数据来处理客户端的命令请求, 还是向发送请求的客户端返回错误
                    3. 丢弃所有旧数据, 开始载入主服务器发来的快照文件
                    4. 完成对快照文件的解释操作, 像往常一样开始接受命令请求
                    5. 执行主服务器发来的所有存储在缓冲区里面的写命令, 并从现在开始, 接受并执行主服务器传来的每一个写命令
        主从链
        更换故障主服务器
            1. 发送快照
            2. 设置新的主服务器
    Redis事务
        WATCH, MULTI, EXEC


     */


    private final static String REDIS_ADDRESS = "192.168.175.128";
    private final static int REDIS_PORT = 6379;
    private final static String PASSWORD = "123456";

    public static void main(String[] args) {
        /*
        Java 中一般有三种连接 redis 的客户端, Jedis, Redisson, Lettuce
         */

        // 使用 jedis 连接 redis

        new Thread(()->{
            Jedis jedis = new Jedis(REDIS_ADDRESS, REDIS_PORT);
            jedis.auth(PASSWORD);
            for (int i = 0; i < 1000; i++) {
                jedis.incr("int2");
            }

            jedis.close();
        }).start();

        new Thread(()->{
            Jedis jedis = new Jedis(REDIS_ADDRESS, REDIS_PORT);
            jedis.auth(PASSWORD);
            for (int i = 0; i < 1000; i++) {
                jedis.incr("int2");
            }
            jedis.close();
        }).start();



    }
}

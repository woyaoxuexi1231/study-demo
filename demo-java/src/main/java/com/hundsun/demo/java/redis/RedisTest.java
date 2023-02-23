package com.hundsun.demo.java.redis;

import cn.hutool.core.collection.CollectionUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

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
        1. WATCH, MULTI, EXEC, UNWATCH
        2. SETNX
        3. Lua 脚本

    Redis的应用
        1. 作为注册中心
        2. 分布式锁
        3. 信号量
        4. 消息队列
     */


    private final static String REDIS_ADDRESS = "192.168.175.128";
    private final static int REDIS_PORT = 6379;
    private final static String PASSWORD = "123456";
    private final static Integer TIMEOUT = 60 * 1000;

    private final static Jedis JEDIS;
    private final static JedisPool JEDIS_POOL;
    private final static RedissonClient REDISSON_CLIENT;
    private final static RedisClient LETTUCE_CLIENT;

    static {
        /*
        Java 中一般有三种连接 redis 的客户端, Jedis, Redisson, Lettuce
         */

        // 1. 使用 Jedis 或者 JedisPool 连接
        JEDIS = new Jedis(REDIS_ADDRESS, REDIS_PORT);
        JEDIS.auth(PASSWORD);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        JEDIS_POOL = new JedisPool(jedisPoolConfig, REDIS_ADDRESS, REDIS_PORT, TIMEOUT, PASSWORD);

        // 2. 使用 Redisson
        Config redissonConfig = new Config();
        redissonConfig.useSingleServer()
                .setAddress("redis://" + REDIS_ADDRESS + ":" + REDIS_PORT)
                .setPassword(PASSWORD);
        REDISSON_CLIENT = Redisson.create(redissonConfig);

        // 3. 使用 Lettuce
        RedisURI redisURI = RedisURI.builder()
                .withHost(REDIS_ADDRESS)
                .withPort(REDIS_PORT)
                .withPassword(PASSWORD)
                .build();
        LETTUCE_CLIENT = RedisClient.create(redisURI);
    }

    private static void closeResources() {
        JEDIS.close();
        JEDIS_POOL.close();
        REDISSON_CLIENT.shutdown();
    }

    public static void main(String[] args) {

        transaction();
        setnxLock();
        luaLock();

        closeResources();
    }

    /**
     * 使用 WATCH, MULTI, EXEC, UNWATCH 的 Redis 事务
     * 这种方式是乐观锁, 乐观的认为在整个事务期间不会有其他事务来修改我们 WATCH 的值
     * 如果在 EXEC 时, 我们检测到 WATCH 的值被改变了, 那么会回滚提交
     */
    public static void transaction() {
        // 监视指定键
        JEDIS.watch("JEDIS::TRANSACTION::LOCK");
        if (JEDIS.get("JEDIS::TRANSACTION::KEY") != null && JEDIS.get("JEDIS::TRANSACTION::KEY").equals("PASS")) {
            // 判断一下是否满足条件, 如果满足条件的话可以使用 UNWATCH 不执行事务了
            JEDIS.unwatch();
            return;
        }
        // 开启事务
        Transaction transaction = JEDIS.multi();
        transaction.set("JEDIS::TRANSACTION::LOCK", "LOCKED");
        transaction.set("JEDIS::TRANSACTION::KEY", "VALUE");
        // 提交事务
        transaction.exec();
    }

    /**
     * 使用 setnx 来保证 Redis 事务
     */
    public static void setnxLock() {

        if (JEDIS.setnx("JEDIS::TRANSACTION::SETNXLOCK", "LOCKED") == 0) {
            // 上锁失败
            return;
        }
        // 加个超时时间, 避免死锁
        JEDIS.expire("JEDIS::TRANSACTION::SETNXLOCK", 30);
        // 或者直接使用 setex
        // JEDIS.setex("JEDIS::TRANSACTION::SETNXLOCK", 30, "LOCKED");
        // 用完释放锁
        // JEDIS.del("JEDIS::TRANSACTION::SETNXLOCK");
    }

    /**
     * 使用 Lua 脚本来保证 Redis 事务
     */
    public static void luaLock() {

        /*
        EVAL 和 EVALSHA 使用 Lua 解释器执行脚本
        EVAL 执行的脚本不从缓存里拿, 而 EVALSHA 执行的脚本从缓存里拿, 根据 sha1 校验码从服务器缓存里拿
        EVAL script numkeys key [key ...] arg [arg ...]
            script - 参数是一段 Lua 5.1 脚本程序, 在 redis 服务器上下文执行, 脚本不必(也不应该)定义为一个 Lua 函数
            numkeys - 用于指定键名参数的个数
            key [key ...] - 从 EVAL 的第三个参数开始算起, 表示在脚本中所用到的那些 Redis 键(key), 这些键名参数可以在 Lua 中通过全局变量 KEYS 数组, 用 1 为基址的形式访问( KEYS[1], KEYS[2], 以此类推)
            arg [arg ...] - 附加参数, 在 Lua 中通过全局变量 ARGV 数组访问, 访问的形式和 KEYS 变量类似( ARGV[1], ARGV[2], 诸如此类)

            eval "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}" 2 key1 key2 first second
            "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}" - script
            2 - numkeys
            key1 key2 - key
            first second - arg
        在 lua 脚本里, 可以使用两个不同的函数来执行 redis 的命令
            redis.call() - 将会抛出错误给 eval 命令的调用者
            redis.pcall() - 会捕获异常错误, 并返回 Lua table 表示错误
            eval "return redis.call('set', KEYS[1], 'bar')" 1 foo - 把 foo 的值设为 bar

        lua 脚本实现的锁比直接使用 setnx 快
            单个客户端 -  快 40%
            两个客户端 - 快 87%
            五个或者是个 - 快一倍以上
         */

        String uuid = "luaLock";
        String lockName = "JEDIS::TRANSACTION::LUALOCK";
        String timeout = "30";
        String lua = "if redis.call('exists', KEYS[1]) == 0 then return redis.call('setex', KEYS[1], ARGV[1], ARGV[2]) end";
        String unpackLua = "if redis.call('exists', KEYS[1]) == 0 then return redis.call('setex', KEYS[1], unpack(ARGV)) end";

        // 成功返回 OK, 失败返回 null
        Object result = JEDIS.eval(lua, CollectionUtil.newArrayList(lockName), CollectionUtil.newArrayList(timeout, uuid));
    }
}

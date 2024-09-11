package org.hulei.keeping.server.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/*
使用lua脚本来实现锁
 */

public class RedisLuaLock {

    private static final String LOCK_KEY = "my_distributed_lock";
    private static final int LOCK_EXPIRE_TIME = 30; // 锁的过期时间，单位秒
    private static final String LOCK_VALUE = "lock_value"; // 锁的值，可以是独特的请求 ID 保证释放的是自己持有的锁

    private final JedisPool jedisPool;

    public RedisLuaLock(String redisHost, int redisPort) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, Protocol.DEFAULT_TIMEOUT, "123456");
    }

    public boolean acquireLock() {
        try (Jedis jedis = jedisPool.getResource()) {
            String luaScript = "return redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2])";
            String result = (String) jedis.eval(luaScript, 1, LOCK_KEY, LOCK_VALUE, String.valueOf(LOCK_EXPIRE_TIME));
            return "OK".equals(result);
        }
    }

    public boolean releaseLock() {
        try (Jedis jedis = jedisPool.getResource()) {
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else return 0 end";
            Long result = (Long) jedis.eval(luaScript, 1, LOCK_KEY, LOCK_VALUE);
            return result == 1;
        }
    }

    public static void main(String[] args) {
        RedisLuaLock lock = new RedisLuaLock("192.168.80.128", 6379);

        if (lock.acquireLock()) {
            try {
                System.out.println("Successfully acquired the lock!");
                // 在这里执行需要加锁的逻辑
                Thread.sleep(20000);    // 模拟处理时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.releaseLock()) {
                    System.out.println("Successfully released the lock!");
                } else {
                    System.out.println("Failed to release the lock.");
                }
            }
        } else {
            System.out.println("Failed to acquire the lock.");
        }
    }
}
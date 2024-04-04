package com.hundsun.demo.spring.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class RedisDistributedLock {

    private static final String LOCK_KEY = "my_lock";
    private static final String LOCK_VALUE = "locked";
    private static final long LOCK_EXPIRE_TIME = 30000; // 锁的过期时间，单位毫秒

    private Jedis jedis;

    public RedisDistributedLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean acquireLock() {
        SetParams params = SetParams.setParams().nx().px(LOCK_EXPIRE_TIME);
        String result = jedis.set(LOCK_KEY, LOCK_VALUE, params);
        return "OK".equals(result);
    }

    public void releaseLock() {
        jedis.del(LOCK_KEY);
    }

    public static void main(String[] args) {
        // 连接到 Redis 服务器
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        boolean isLocked = false;
        RedisDistributedLock lock = new RedisDistributedLock(jedis);
        try {
            // 尝试获取锁
            if (lock.acquireLock()) {
                isLocked = true;
                System.out.println("获取锁成功，执行业务逻辑...");
                Thread.sleep(15000);
                // 在这里执行业务逻辑
            } else {
                System.out.println("获取锁失败，未能执行业务逻辑");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (isLocked) {
                lock.releaseLock();
            }
            jedis.close();
        }
    }
}


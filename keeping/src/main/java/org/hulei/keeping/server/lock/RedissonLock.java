package org.hulei.keeping.server.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;


public class RedissonLock {

    public static void main(String[] args) {
        // 创建 Redisson 配置对象
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.80.130:6379")
                .setPassword("123456");

        // 创建 Redisson 客户端
        RedissonClient redisson = Redisson.create(config);

        // 获取分布式锁对象
        RLock lock = redisson.getLock("myLock");

        Thread thread = new Thread(() -> {

            boolean isLocked = false;

            try {
                // 尝试获取锁
                isLocked = lock.tryLock();
                if (isLocked) {
                    // 成功获取锁，执行业务逻辑
                    System.out.println("Lock acquired, executing business logic...");
                    Thread.sleep(50 * 1000); // 模拟业务逻辑执行时间
                } else {
                    // 获取锁失败，处理逻辑
                    System.out.println("Failed to acquire lock, do something else...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "redissonlock");


        thread.start();

        // 关闭 Redisson 客户端
        // redisson.shutdown();
    }
}

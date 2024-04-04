package com.hundsun.demo.springboot.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redission")
public class DistributedLockController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/lock")
    public String acquireLock() {
        // 获取分布式锁对象
        RLock lock = redissonClient.getLock("myLock");
        boolean isLocked = false;
        try {
            // 尝试获取锁
            isLocked = lock.tryLock();
            if (isLocked) {
                // 成功获取锁，执行业务逻辑
                System.out.println("Lock acquired, executing business logic...");
                Thread.sleep(5000); // 模拟业务逻辑执行时间
            } else {
                // 获取锁失败，处理逻辑
                System.out.println("Failed to acquire lock, do something else...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            if (isLocked) {
                lock.unlock();
            }
        }

        return "Lock operation completed";
    }
}

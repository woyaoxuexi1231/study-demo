package com.hundsun.demo.spring.jdk.juc;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Lock;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 16:33
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class ReentrantLock {

    public static void main(String[] args) {
        reentrantLock();
    }

    @SneakyThrows
    public static void reentrantLock() {
        // 创建一个 ReentrantLock 实例
        Lock lock = new java.util.concurrent.locks.ReentrantLock();

        // 创建一个线程，模拟对共享资源的访问
        Thread thread = new Thread(() -> {
            // 获取锁
            lock.lock();
            // 多次获取锁会使重入次数增加,需要相应的增加释放次数
            // lock.lock();
            try {
                // 在临界区内执行需要同步的操作
                System.out.println("Thread is executing the critical section.");
                // 模拟线程执行时间
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
                System.out.println("Thread has released the lock.");
            }
        });

        // 启动线程
        thread.start();

        Thread.sleep(100);
        lock.lock();
        System.out.println("this is main");
        lock.unlock();
    }
}

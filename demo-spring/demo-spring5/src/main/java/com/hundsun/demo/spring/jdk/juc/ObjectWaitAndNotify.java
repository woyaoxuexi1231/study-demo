package com.hundsun.demo.spring.jdk.juc;

import lombok.SneakyThrows;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 16:00
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class ObjectWaitAndNotify {

    public static void main(String[] args) {
        waitAndNotify();
    }

    static final Object lock = new Object();

    @SneakyThrows
    private static void waitAndNotify() {
        Thread one = new Thread(() -> {
            synchronized (lock) {
                System.out.println("准备wait()");
                try {
                    // 这个操作会释放synchronized获取到的锁
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("结束wait()");
            }
        });

        Thread two = new Thread(() -> {
            synchronized (lock) {
                System.out.println("准备notify()");
                lock.notify(); // 唤醒第一个在等待的线程
                // lock.notifyAll() 同时唤醒所有正在等待的线程
                System.out.println("结束notify()");
            }
        });

        one.start();
        Thread.sleep(1000);
        two.start();
    }

}

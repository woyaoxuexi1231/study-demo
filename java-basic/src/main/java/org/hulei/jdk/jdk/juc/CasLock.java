package org.hulei.jdk.jdk.juc;

import java.util.concurrent.atomic.AtomicReference;

public class CasLock {
    private static int counter = 0;
    private static final SpinLock spinLock = new SpinLock();

    public static void main(String[] args) {
        // 创建10个线程并启动
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementCounter();
                }
            }).start();
        }

        // 等待所有线程执行完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 输出最终的计数器值
        System.out.println("Counter: " + counter);
    }

    private static void incrementCounter() {
        spinLock.lock(); // 获取锁
        counter++; // 进入临界区
        spinLock.unlock(); // 释放锁
    }
}

class SpinLock {
    private final AtomicReference<Thread> owner = new AtomicReference<>();

    public void lock() {
        Thread currentThread = Thread.currentThread();
        // 尝试获取锁，如果成功则返回，否则自旋等待
        while (!owner.compareAndSet(null, currentThread)) {
            // 自旋等待
        }
    }

    public void unlock() {
        Thread currentThread = Thread.currentThread();
        // 只有持有锁的线程才能释放锁
        owner.compareAndSet(currentThread, null);
    }
}

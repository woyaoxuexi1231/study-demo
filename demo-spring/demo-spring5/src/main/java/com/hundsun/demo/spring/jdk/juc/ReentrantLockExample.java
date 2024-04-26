package com.hundsun.demo.spring.jdk.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean conditionMet = false;

    public void awaitCondition() throws InterruptedException {
        lock.lock();
        try {
            // while (!conditionMet) {
            log.info("开始等待");
            condition.await();
            Thread.sleep(1000);
            // }
            System.out.println("Condition is met!");
        } finally {
            lock.unlock();
        }
    }

    public void signalCondition() {
        lock.lock();
        try {
            conditionMet = true;
            log.info("唤醒");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockExample example = new ReentrantLockExample();

        Thread thread1 = new Thread(() -> {
            try {
                example.awaitCondition();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            example.signalCondition();
        });

        thread1.start();
        thread2.start();
    }
}

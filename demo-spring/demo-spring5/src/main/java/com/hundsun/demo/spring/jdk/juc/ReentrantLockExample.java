package com.hundsun.demo.spring.jdk.juc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean conditionMet = false;

    @SneakyThrows
    public void awaitCondition() {
        lock.lock();
        try {
            while (!conditionMet) {
                log.info("开始等待");
                // 从AbstractQueuedSynchronizer中移除本身Node,然后park当前线程,被唤醒后,又把当前Node加入队列
                condition.await();
                Thread.sleep(1000);
            }
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
            // condition.signal();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockExample example = new ReentrantLockExample();

        Thread thread1 = new Thread(example::awaitCondition);
        Thread thread3 = new Thread(example::awaitCondition);
        Thread thread2 = new Thread(example::signalCondition);

        thread1.start();
        thread3.start();
        thread2.start();
    }
}

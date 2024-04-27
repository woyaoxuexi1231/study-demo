package com.hundsun.demo.spring.jdk.juc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link java.util.concurrent.locks.AbstractQueuedSynchronizer}
 *
 * @author hulei42031
 * @since 2024-04-26 16:33
 */

@Slf4j
public class ReentrantLockCondition {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition firstCondition = lock.newCondition();
    private final Condition secondCondition = lock.newCondition();
    private volatile boolean conditionMet = false;

    @SneakyThrows
    public void awaitCondition(Condition condition) {
        lock.lock();
        try {
            while (!conditionMet) {
                log.info("开始等待");
                // 从AbstractQueuedSynchronizer中移除本身Node,然后park当前线程,被唤醒后,又把当前Node加入队列
                condition.await();
                Thread.sleep(1000);
            }
            log.info("Condition is met!");
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public void signalCondition() {
        lock.lock();
        try {
            conditionMet = true;
            log.info("唤醒firstCondition");
            // signal就唤醒firstNode
            // condition.signal();
            // signalAll将循环唤醒Node
            // 这里虽然唤醒同时唤醒了两个条件,但是锁只有一把,线程1和线程2会去抢占 这个逻辑是由AQS实现的
            firstCondition.signalAll();
            // Thread.sleep(2000);
            log.info("唤醒secondCondition");
            secondCondition.signalAll();
        } finally {
            lock.unlock();
        }
        log.info("结束");
    }

    @SneakyThrows
    public static void main(String[] args) {

        ReentrantLockCondition example = new ReentrantLockCondition();

        Thread thread1 = new Thread(() -> example.awaitCondition(example.firstCondition),"1");
        Thread thread2 = new Thread(() -> example.awaitCondition(example.secondCondition),"2");
        Thread thread3 = new Thread(example::signalCondition,"3");

        thread1.start();
        thread2.start();

        Thread.sleep(100);
        thread3.start();
    }
}

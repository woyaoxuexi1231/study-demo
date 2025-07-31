package org.hulei.basic.jdk.juc.aqs;

import lombok.SneakyThrows;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;

/**
 * {@link java.util.concurrent.locks.AbstractQueuedSynchronizer}
 *
 * @author hulei42031
 * @since 2024-04-26 16:33
 */

public class ReentrantLockTest {

    public static void main(String[] args) {
        reentrantLock();
    }

    @SneakyThrows
    public static void reentrantLock() {
        /*
        通过 CAS 和 AQS 实现的可重复锁
        Lock lock = new java.util.concurrent.locks.ReentrantLock(true);
        默认非公平锁


        AbstractQueuedSynchronizer - AQS, 实现同步器的基础组件, 锁底层使用 AQS 实现

        实现原理如下: 只提供内部维护的'阻塞队列的功能',内部维护了两个Node(链表对象)对象,一个头节点,一个尾节点
        1. 尝试获取锁的 tryAcquire 由具体子类实现, ReentrantLock 通过一个 state 参数的CAS来尝试获取锁
        2. 每个获取锁的线程都创建新的 Node 对象尝试用尾插法插入链表中,然后阻塞当前线程
        3. 释放锁的 release 方法从链表中移除当前头节点,然后循环往下唤醒等待的线程
        4. 支持可重入,但是具体的逻辑需要子类实现

        AbstractQueuedSynchronizer abstractQueuedSynchronizer;
         */
        Lock lock = new java.util.concurrent.locks.ReentrantLock();

        // 创建一个线程，模拟对共享资源的访问
        Thread thread = new Thread(() -> {
            // 获取锁
            // CAS->入列->入列成功park当前线程->被唤醒后尝试获取锁,重复这个操作直到获取到锁
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
        }, "reentrantLock-test-thread");

        new Thread(() -> {
            while (true) ;
        }, "reentrantLock-test2-thread").start();


        // 启动线程
        thread.start();

        Thread.sleep(100);
        // ReentrantLock的等待可中断: 如果其他线程已经持有锁，当前线程可以选择等待一段时间后放弃获取锁，而不是一直等待下去
        // if (lock.tryLock(6, TimeUnit.SECONDS)) {
        //     System.out.println("this is main");
        //     lock.unlock();
        // } else {
        //     System.out.println("timeout!");
        // }
        lock.lock();
    }
}

package org.hulei.basic.jdk.jvm;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDeadlock {
    static final Lock lock1 = new ReentrantLock();
    static final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> {
            lock1.lock();
            System.out.println("Thread 1: Holding lock 1...");
            sleep(100);
            lock2.lock();
            System.out.println("Thread 1: Holding lock 2...");
            // 永远不会执行到这里
            lock2.unlock();
            lock1.unlock();
        }).start();

        new Thread(() -> {
            lock2.lock();
            System.out.println("Thread 2: Holding lock 2...");
            sleep(100);
            lock1.lock();
            System.out.println("Thread 2: Holding lock 1...");
            lock1.unlock();
            lock2.unlock();
        }).start();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}

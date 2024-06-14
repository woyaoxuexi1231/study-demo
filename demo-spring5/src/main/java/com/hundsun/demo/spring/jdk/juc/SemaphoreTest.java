package com.hundsun.demo.spring.jdk.juc;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 15:36
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class SemaphoreTest {


    public static void main(String[] args) {
        semaphore();
    }


    public static void semaphore() {

        // 创建一个 Semaphore 实例，允许同时访问的线程数量为2
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(2);

        // 创建五个线程，并将 Semaphore 传递给它们
        Thread t1 = new Thread(new Task(semaphore, "Thread 1"));
        Thread t2 = new Thread(new Task(semaphore, "Thread 2"));
        Thread t3 = new Thread(new Task(semaphore, "Thread 3"));
        Thread t4 = new Thread(new Task(semaphore, "Thread 4"));
        Thread t5 = new Thread(new Task(semaphore, "Thread 5"));

        // 启动这五个线程
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

    static class Task implements Runnable {
        private final java.util.concurrent.Semaphore semaphore;
        private final String name;

        Task(java.util.concurrent.Semaphore semaphore, String name) {
            this.semaphore = semaphore;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                // 获取信号量许可
                semaphore.acquire();
                System.out.println(name + " is accessing the resource.");
                // 模拟线程执行任务
                Thread.sleep(2000);
                System.out.println(name + " has released the resource.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放信号量许可
                semaphore.release();
            }
        }
    }

}

package org.hulei.basic.jdk.juc.aqs;

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

        /*
        Semaphore - 信号量
        acquire()/acquire(int permits) - 调用该线程的方法会被阻塞, 直到 Semaphore 的信号量值达到 1/permits, 内部采用具体的公平策略在 AQS 中选择线程进行唤醒
        release() - 调用该方法的线程会使 Semaphore 的信号量递增

        原理: 内部使用AQS作为基础实现,和线程池有些类似,多个线程抢占permits,没有抢到的进行阻塞,相当于就是线程池的任务都抢占线程执行任务
            1. 每次执行acquire()的时候对permits进行削减,如果为0,那么直接阻塞
            2. 直到有线程主动释放资源,也就是执行release()方法,然后被唤醒的线程继续执行acquire()的逻辑,反复的尝试去获取permits
         */
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

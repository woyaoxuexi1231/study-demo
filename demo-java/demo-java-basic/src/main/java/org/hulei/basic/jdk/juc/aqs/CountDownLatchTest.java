package org.hulei.basic.jdk.juc.aqs;

import java.util.concurrent.CountDownLatch;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 16:30
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class CountDownLatchTest {

    public static void main(String[] args) {
        countDownLatch();
    }

    public static void countDownLatch() {
        /*
        CountDownLatch - 指定数量的线程被执行后, 调用了 CountDownLatch.await() 方法的线程会被唤醒
        countDown() - 线程调用该方法后, CountDownLatch 内部的计数器会递减, 递减后如果计数器的值为 0, 则唤醒所有因调用 await() 方法被阻塞的线程
        await() - 线程调用该方法后会被阻塞, 直到 CountDownLatch 内部的计数器值为 0 或者其他线程调用了当先线程的 interrupt() 方法中断了该线程

        原理: 内部有一个AQS变量,通过这个变量来控制并发
            1. 初始化会赋值给定的State值,这个State值代表需要等待的计数器值
            2. 调用countDown()的线程会cas自旋进行state值的递减,直到state=0,那么会唤醒所有在等待的线程
            3. 调用await()的线程会尝试是否state=0,如果不等于0,加入AQS等待队列
         */
        CountDownLatch latch = new CountDownLatch(3);

        // 创建三个线程，并将 CountDownLatch 传递给它们
        Thread worker1 = new Thread(new Worker(latch, "Worker 1"));
        Thread worker2 = new Thread(new Worker(latch, "Worker 2"));
        Thread worker3 = new Thread(new Worker(latch, "Worker 3"));

        // 启动这三个线程
        worker1.start();
        worker2.start();
        worker3.start();

        try {
            // 主线程等待，直到计数器为0
            latch.await();
            // 所有工作线程都完成后，执行这里的代码
            System.out.println("All workers have completed their tasks.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Worker implements Runnable {

        private final CountDownLatch latch;
        private final String name;

        Worker(CountDownLatch latch, String name) {
            this.latch = latch;
            this.name = name;
        }

        @Override
        public void run() {
            // 模拟执行任务
            System.out.println(name + " is working...");
            try {
                // 假设任务执行耗时为随机的1到5秒
                Thread.sleep((long) (Math.random() * 5000 + 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " has completed its task.");
            // 每个工作线程完成后，将计数器减1
            latch.countDown();
        }
    }
}

package org.hulei.jdk.root.juc;

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
        // 创建一个 CountDownLatch 实例，计数器初始化为3
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(3);

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

        private final java.util.concurrent.CountDownLatch latch;
        private final String name;

        Worker(java.util.concurrent.CountDownLatch latch, String name) {
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

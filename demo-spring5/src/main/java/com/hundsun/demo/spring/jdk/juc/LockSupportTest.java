package com.hundsun.demo.spring.jdk.juc;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 16:37
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class LockSupportTest {

    public static void main(String[] args) {
        lockSupport();
    }

    public static void lockSupport() {
        Thread thread = new Thread(() -> {
            System.out.println("Thread starts.");
            // 当前线程进入等待状态
            java.util.concurrent.locks.LockSupport.park();
            System.out.println("Thread resumes.");
        });

        thread.start();

        // 主线程等待一段时间
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 主线程唤醒子线程
        java.util.concurrent.locks.LockSupport.unpark(thread);
    }
}

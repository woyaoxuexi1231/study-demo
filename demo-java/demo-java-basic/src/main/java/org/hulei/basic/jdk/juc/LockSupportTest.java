package org.hulei.basic.jdk.juc;

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

        /*
        LockSupport 工具类 - 主要作用是挂起和唤醒线程
            park() 类似于 wait() , 被 interrupt() 不会抛异常
            unpark(Thread thread) 类似与 notify()
            parkNanos(long nanos)
         */


        Thread thread = new Thread(() -> {
            System.out.println("Thread starts.");
            // 当前线程进入等待状态
            // 还提供了一个park(对象 blocker)方法, blocker 是一个 Object 类型的参数，通常用于描述或标识当前线程正在被哪个对象阻塞。
            // 它的主要作用是协助调试和线程监控工具。通过这个 blocker 对象，线程监控工具（例如 JDK 自带的 jstack 工具）可以在监控线程状态时，提供更有意义的信息，帮助开发人员了解线程被阻塞的原因或阻塞的对象。
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

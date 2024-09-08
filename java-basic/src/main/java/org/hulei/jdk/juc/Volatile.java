package org.hulei.jdk.juc;

import lombok.SneakyThrows;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 16:16
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class Volatile {

    public static void main(String[] args) {
        volatileTest();
    }

    // private static volatile int count = 0;
    private static int count = 0;
    // private static boolean flag = false;
    private static volatile boolean flag = false;

    @SneakyThrows
    private static void volatileTest() {
        Thread readerThread = new Thread(() -> {
            while (!flag) {
                /*
                空循环等待 flag变为 true
                1. 如果flag没有使用volatile修饰,只要启动后读线程率先进入这个方法(也就是说在读线程修改flag之前在主存中获取了flag的值),那么就会在这里形成死锁
                2. 如果flag使用volatile修饰,那么此处没有问题
                 */
            }
            System.out.println("Count: " + count);
        });

        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                count++;
            }
            System.out.println("已结束");
            flag = true; // 修改flag为true
        });

        readerThread.start();
        writerThread.start();
    }
}

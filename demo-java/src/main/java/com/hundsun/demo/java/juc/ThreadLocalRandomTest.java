package com.hundsun.demo.java.juc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-12-23 14:27
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class ThreadLocalRandomTest {

    public static void main(String[] args) throws InterruptedException {

        /*
        对于 Random 这个类来说, 他其实是线程安全的,《Java并发变成之美》一书中说到的, 在多个线程下产生的新种子有可能是一样的, 其实这个概率是比较小的, 因为初始种子会以System.nanoTime()这个返回的值作为生成种子的条件
        但是由于 Random 内部使用cas操作,再多个线程使用同一个 Random 生成随机数的时候, 失败的会进行自旋重试, 这导致降低并发性能, 所以产生了 ThreadLocalRandom

        ThreadLocalRandom 和 ThreadLocal 原理类似
        ThreadLocalRandom 负责初始化线程持有的种子, 以及负责通过线程给定的种子计算随机数并产生新的种子
         */

        System.out.println(System.currentTimeMillis());
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        CountDownLatch countDownLatch = new CountDownLatch(50000);
        for (int i = 0; i < 50000; i++) {
            new Thread(() -> {

                int j = threadLocalRandom.nextInt(5);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis());
        CountDownLatch countDownLatch2 = new CountDownLatch(50000);
        Random random = new Random();
        for (int i = 0; i < 50000; i++) {
            new Thread(() -> {
                int j = random.nextInt(5);
                countDownLatch2.countDown();
            }).start();
        }
        countDownLatch2.await();
        System.out.println(System.currentTimeMillis());
    }
}

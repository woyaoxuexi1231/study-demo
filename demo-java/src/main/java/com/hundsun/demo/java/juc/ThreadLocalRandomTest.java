package com.hundsun.demo.java.juc;

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
        ThreadLocalRandom
        对于 Random 这个类来说, 他其实是线程安全的,《Java并发变成之美》一书中说到的, 在多个线程下产生的新种子有可能是一样的, 其实这个概率是比较小的, 因为初始种子会以System.nanoTime()这个返回的值作为生成种子的条件
        但是由于 Random 内部使用cas操作,再多个线程使用同一个 Random 生成随机数的时候, 失败的会进行自旋重试, 这导致降低并发性能, 所以产生了 ThreadLocalRandom

        ThreadLocalRandom 和 ThreadLocal 原理类似
        ThreadLocalRandom 负责初始化线程持有的种子, 以及负责通过线程给定的种子计算随机数并产生新的种子
        当我们在多个线程中使用同一个 ThreadLocalRandom 的时候会导致所有使用这个 ThreadLocalRandom 的线程生成呈某种规律的一组所谓的随机数, 但是这个不是随机数, 这个是个错误的使用方法
        当我们以这种方法使用时, 我们并没有初始化我们线程中的 seed 值, 导致我们一直使用 ThreadLocalRandom 中的 GAMMA 的常量作为种子
        正确的使用方法应该是和这个类的设计初衷是一样的, 我们在每个线程的内部维护一个 ThreadLocalRandom , 使用 ThreadLocalRandom.current() 来初始化每个线程的 seed
        线程内部使用 initialSeed() 这个方法来创建一个随机的初始的种子

         */
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        System.out.println(threadLocalRandom.nextInt(5));
        System.out.println(threadLocalRandom.nextInt(5));

        // 这里一定会输出 4
        new Thread(() -> System.out.println(threadLocalRandom.nextInt(5))).start();


    }


}

package org.hulei.basic.jdk.juc;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author hulei
 * @since 2025/6/25 10:32
 */

public class AtomicClassTest {


    @SneakyThrows
    public static void atomicTest() {
        /*
         * 原理类似, 内部都是通过 Unsafe 类来实现原子性的递增或者递减
         * 在没有这些原子操作类的时候我们可以通过 synchronized 来保证线程安全, 但是使用 synchronized 是一种阻塞算法
         * 这些原子操作类都是基于 cas 非阻塞算法实现的, 所以性能会更好
         */
        AtomicInteger atomicInteger = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                atomicInteger.addAndGet(1);
            }
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                atomicInteger.addAndGet(1);
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println(atomicInteger);
    }

    @SneakyThrows
    public static void longAdderTest() {
        /*
         * 为了解决高并发下大量线程同时竞争一个原子变量而造成的大量线程不断自旋尝试 cas 操作, 这会浪费 cpu 资源
         * LongAdder应运而生,原理如下:
         * 由一个 base 值 和 cell[] 数组构成
         * 1. 并发比较低的情况下,数值全部由base记录,所有线程对base进行cas操作
         * 2. 并发逐渐提升,base的cas失败会触发cell[]的初始化,初始大小为 2,base cas操作失败的线程会随机选一个 cell进行cas操作进行数值的累加
         * 3. cell[]最大只能是cpu数量,达到cpu数量之后将不在触发扩容,会一直尝试随机在某一个cell里面进行操作
         * 4. 得到最终值是通过计算base+cell[]所有的数值得到
         */
        LongAdder longAdder = new LongAdder();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                longAdder.add(1);
            }
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                longAdder.add(1);
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println(longAdder);

    }

}

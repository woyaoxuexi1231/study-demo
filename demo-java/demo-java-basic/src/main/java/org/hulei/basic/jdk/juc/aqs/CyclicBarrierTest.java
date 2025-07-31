package org.hulei.basic.jdk.juc.aqs;

import lombok.SneakyThrows;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2025/6/24 22:14
 */

public class CyclicBarrierTest {

    public static void main(String[] args) {

        /*
        回环屏蔽的设计理念是 多个线程同时时，在某个点需要每个线程都达到一定程度后，所有的线程才能够继续往下执行
        举例：游戏中选人的过程，五个人同时加载好游戏后才能一起选择人物，所有人都选择好了人物才能够进入游戏
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        new Thread(() -> {
            try {
                System.out.println("线程1 cyclicBarrier.await() ");
                cyclicBarrier.await();

                System.out.println("线程1 继续执行");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                System.out.println("线程2 cyclicBarrier.await() ");
                cyclicBarrier.await();

                System.out.println("线程2 继续执行");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                System.out.println("线程3 cyclicBarrier.await() ");
                cyclicBarrier.await();

                System.out.println("线程3 继续执行");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}

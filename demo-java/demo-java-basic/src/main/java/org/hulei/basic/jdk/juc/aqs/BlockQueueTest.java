package org.hulei.basic.jdk.juc.aqs;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 16:03
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
public class BlockQueueTest {

    public static void main(String[] args) {
        blockQueue();
    }

    @SneakyThrows
    private static void blockQueue() {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        Thread one = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    log.info("queue正在放入第{}个元素", i);
                    queue.put(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread two = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Integer take = queue.take();
                    log.info("queue获取元素成功, {}", take);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        one.start();
        Thread.sleep(2000);
        two.start();
    }

}

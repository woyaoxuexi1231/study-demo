package com.hundsun.demo.dubbo.consumer.service.impl;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.service.LocalLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-02 10:25
 */

@Service
@Slf4j
public class LocalLockServiceImpl implements LocalLockService {


    /**
     * ThreadPoolExecutor
     */
    private static final ThreadPoolExecutor CONSUMER_TEST_POOL = new ThreadPoolExecutor(1, 2, 20,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), new ThreadFactoryBuilder()
            .setNamePrefix("consumer-test-thread-").build(), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 共享资源
     */
    public static Integer PUBLIC_INTEGER = 0;

    /**
     * ReentrantLock是Java中常用的锁，属于乐观锁类型，多线程并发情况下，能保证共享数据安全性，线程间有序性
     * ReentrantLock通过原子操作和阻塞实现锁原理，一般使用lock获取锁，unlock释放锁
     */
    public static ReentrantLock REENTRANTLOCK = new ReentrantLock();

    @Override
    public void decreaseSharedResource() {
        CONSUMER_TEST_POOL.execute(new LocalLockTaskReentrantLock());
        CONSUMER_TEST_POOL.execute(new LocalLockTaskReentrantLock());
    }

    @Override
    public ResultDTO getSharedResource() {
        return ResultDTOBuild.resultSuccessBuild(PUBLIC_INTEGER);
    }

    /**
     * synchronized
     */
    static class LocalLockTaskSynchronized implements Runnable {

        @Override
        public void run() {
            log.info("当前" + Thread.currentThread().getName() + "线程开始处理");
            for (int i = 0; i < 2000; i++) {
                // 使用synchronized锁住共享资源
                synchronized (this) {
                    PUBLIC_INTEGER--;
                }
            }
            log.info("当前" + Thread.currentThread().getName() + "处理完成");
        }
    }

    /**
     * ReentrantLock
     */
    static class LocalLockTaskReentrantLock implements Runnable {

        @Override
        public void run() {

            log.info("当前" + Thread.currentThread().getName() + "线程开始处理");
            for (int i = 0; i < 2000; i++) {
                REENTRANTLOCK.lock();
                try {
                    PUBLIC_INTEGER--;
                } finally {
                    REENTRANTLOCK.unlock();
                }

            }
            log.info("当前" + Thread.currentThread().getName() + "处理完成");
        }
    }

}

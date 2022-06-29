package com.hundsun.demo.dubbo.consumer.service.impl;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.common.api.utils.ResultDTOBuild;
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
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Service
@Slf4j
public class LocalLockServiceImpl implements LocalLockService {

    /**
     * ThreadPoolExecutor（无脑使用ArrayBlockingQueue+CallerRunsPolicy策略这个不会丢掉任何一个任务）
     *
     * <li>corePoolSize:指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；</li>
     * <li>maximumPoolSize:指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；</li>
     * <li>keepAliveTime:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；</li>
     * <li>unit:keepAliveTime的单位</li>
     * <li>workQueue:任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列(SynchronousQueue)、有界任务队列(ArrayBlockingQueue)、无界任务队列
     * (LinkedBlockingQueue)、优先任务队列（PriorityBlockingQueue）几种；
     * <ol>
     *     <li>
     *         <em>
     *             使用SynchronousQueue队列，提交的任务不会被保存，总是会马上提交执行。如果用于执行任务的线程数量小于maximumPoolSize
     *             ，则尝试创建新的进程，如果达到maximumPoolSize设置的最大值，则根据你设置的handler
     *             执行拒绝策略。因此这种方式你提交的任务不会被缓存起来，而是会被马上执行，在这种情况下，你需要对你程序的并发量有个准确的评估，才能设置合适的maximumPoolSize数量，否则很容易就会执行拒绝策略；
     *         </em>
     *     </li>
     *     <li>
     *         <em>
     *             使用ArrayBlockingQueue有界任务队列，若有新的任务需要执行时，线程池会创建新的线程，直到创建的线程数量达到corePoolSize
     *             时，则会将新的任务加入到等待队列中。若等待队列已满，即超过ArrayBlockingQueue初始化的容量，则继续创建线程，直到线程数量达到maximumPoolSize
     *             设置的最大线程数量，若大于maximumPoolSize
     *             ，则执行拒绝策略。在这种情况下，线程数量的上限与有界任务队列的状态有直接关系，如果有界队列初始容量较大或者没有达到超负荷的状态，线程数将一直维持在corePoolSize
     *             以下，反之当任务队列已满时，则会以maximumPoolSize为最大线程数上限。
     *         </em>
     *     </li>
     *     <li>
     *         <em>
     *             使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，也就是说在这种情况下maximumPoolSize
     *             这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize
     *             后，就不会再增加了；若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
     *         </em>
     *     </li>
     *     <li>
     *         <em>
     *             PriorityBlockingQueue它其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize
     *             的数量，只不过其他队列一般是按照先进先出的规则处理任务，而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
     *         </em>
     *     </li>
     * </ol>
     * </li>
     * <li>threadFactory:线程工厂，用于创建线程，一般用默认即可；</li>
     * <li>handler:拒绝策略；当任务太多来不及处理时，如何拒绝任务；
     * <ol>
     * <p>1、AbortPolicy策略(会丢弃任务)：该策略会直接抛出异常，阻止系统正常工作；</p>
     * <p>2、CallerRunsPolicy策略(不会丢弃任务)：如果线程池的线程数量达到上限，该策略会把任务队列中的任务放在调用者线程当中运行；</p>
     * <p>3、DiscardOledestPolicy策略(会丢弃任务)：该策略会丢弃任务队列中最老的一个任务，也就是当前任务队列中最先被添加进去的，马上要被执行的那个任务，并尝试再次提交；</p>
     * <p>4、DiscardPolicy策略(会丢弃任务)：该策略会默默丢弃无法处理的任务，不予任何处理。当然使用此策略，业务场景中需允许任务的丢失；</p>
     * </ol>
     * </li>
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

package com.hundsun.demo.java.juc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-12-26 15:01
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class LockTest {

    /**
     * park() 类似于 wait() , 被 interrupt() 不会抛异常
     * unpark(Thread thread) 类似与 notify()
     * parkNanos(long nanos)
     */
    LockSupport lockSupport;

    /**
     * FIFO 双向队列
     * Node 用来存放 thread 变量, 然后放入 ASQ 队列中
     * 主要通过 state 状态值来对线程的同步进行操作
     * acquire(int arg) 和 release(int arg) 用来获取独占资源和释放资源, 由具体子类实现
     * AQS 如何入队?? 1. 先初始化 head 和 tail 节点 2. 入队的 node 都插入到队列的末尾与上一个 tail 节点形成双向链表
     * AQS 中的条件变量 condition, condition 包括 await 和 signal, 类似与 wait 和 notify, 用来配合锁来达到与后者差不多的效果
     * ReentrantLock 可重入独占锁, Java常见的锁
     * ReentrantReadWriteLock 读写分离的锁, 分写锁和读锁
     * JDK8新增的 StampedLock
     */
    AbstractQueuedSynchronizer abstractQueuedSynchronizer;
}

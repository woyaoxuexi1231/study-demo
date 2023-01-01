package com.hundsun.demo.java.juc;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.juc
 * @className: QueueTest
 * @description:
 * @author: h1123
 * @createDate: 2023/1/1 15:18
 * @updateUser: h1123
 * @updateDate: 2023/1/1 15:18
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class QueueTest {

    /**
     * JDK 提供了一些并发队列 - 按照实现方式不同可以分为阻塞队列和非阻塞队列, 顾名思义, 阻塞队列使用锁实现 非阻塞队列使用 CAS 实现
     * 非阻塞队列
     *      ConcurrentLinkedQueue
     * 阻塞队列
     *      LinkedBlockingQueue(有界) - 内部主要使用 ReentrantLock,
     *      ArrayBlockingQueue(有界)
     *      PriorityBlockingQueue(无界) - 通过比较优先级来确定出列顺序
     *      DelayQueue(无界) - 元素带有过期时间, 只有过期元素才会出列, 队列头元素是最快要过期的元素. 元素需要继承 Delayed 接口
     */
}

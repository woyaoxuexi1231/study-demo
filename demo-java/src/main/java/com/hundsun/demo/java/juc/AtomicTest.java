package com.hundsun.demo.java.juc;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.juc
 * @className: AtomicTest
 * @description: Java并发编程之美 - 原子操作类
 * @author: h1123
 * @createDate: 2022/12/24 14:54
 * @updateUser: h1123
 * @updateDate: 2022/12/24 14:54
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class AtomicTest {

    /**
     * AtomicInter AtomicLong AtomicBoolean
     * 原理类似, 内部都是通过 Unsafe 类来实现原子性的递增或者递减
     * 在没有这些原子操作类的时候我们可以通过 synchronized 来保证线程安全, 但是使用 synchronized 是一种阻塞算法
     * 这些原子操作类都是基于 cas 非阻塞算法实现的, 所以性能会更好
     */
    AtomicLong atomicLong = new AtomicLong();

    /**
     * JDK 8 新增比这些性能更好的 LongAdder
     * 为了解决高并发下大量线程同时竞争一个原子变量而造成的大量线程不断自旋尝试 cas 操作, 这会浪费 cpu 资源
     * LongAdder 内部通过 base 和 cell 来减少竞争, 线程会对 cell 进行争抢, 最后的结果由 base 计算所有 cell 的值得到
     * cell 结构简单, 由一个被 volatile 修饰的 long 类型变量组成, 修改 cell 的值时采用 cas 保证原子性
     * 具体哪个线程访问哪个 cell 由 ( getProbe() & cell数组元素个数-1 ) 得到, 这个值作为 cell 数组的下标
     * cells 被初始化时初始大小为 2, 后续扩容为之前的 2 倍, 并复制 cell 的元素到扩容之后的数组
     */
    LongAdder longAdder = new LongAdder();
}

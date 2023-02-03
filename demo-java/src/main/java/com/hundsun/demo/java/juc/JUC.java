package com.hundsun.demo.java.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.juc
 * @className: TaskTest
 * @description: Java并发编程之美 -- 第一章, 第二章主要内容
 * @author: h1123
 * @createDate: 2022/12/22 19:30
 * @updateUser: h1123
 * @updateDate: 2022/12/22 19:30
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class JUC {

    /* 创建线程的三种方式  */

    /**
     * 通过继承 Thread 类来创建线程
     */
    private static class MyThread extends Thread {

        @Override
        public void run() {
            System.out.println("1. 通过继承 Thread 类的方法来创建线程");
        }
    }

    /**
     * 通过实现 Runnable 接口来创建线程
     */
    private static class RunnableTask implements Runnable {

        @Override
        public void run() {
            System.out.println("2. 通过实现 Runnable 接口来创建任务, 并通过 Thread 类来创建线程");
        }
    }

    /**
     * 通过 Callable + FutureTask 接口来创建线程
     * 这种方式创建的任务允许一个返回值
     */
    private static class CallerTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            return "3. 通过实现 Callable 接口来创建任务, 然后创建一个 FutureTask 实例, 通过 Thread 类来创建线程";
        }
    }

    /* 线程间同步 */
    /**
     * 线程间同步 - Object 提供的方法
     * <p>
     * 关于 Object.wait()方法
     * 调用该方法必须持有对象的监视器锁, 否则会抛出 IllegalMonitorStateException 异常
     * 调用该方法的线程会被阻塞挂起, 直到以下情况, 会尝试重新获取监视器锁, 然后往下执行:
     * 1. 其他线程调用 notify() 或者 notifyAll() 方法
     * 2. 其他线程调用该线程的 interrupt() 方法, 该线程会抛出 InterruptedException 异常而返回
     * 需要注意的是, 可能会存在虚假唤醒, 所以在实际使用中, 会在使用wait方法的地方使用 while 循环一直判断条件是否满足, 不满足则继续wait
     * <p>
     * wait(long timeout) 方法, 没有在执行的 timeout 时间内被唤醒会因为超时而返回, wait(0) 等价于 wait 方法
     * wait(long timeout, int nanos)函数
     * notify() 和 notifyAll() 也都是必须获得对象的监视器锁才能使用, 否则会抛出 IllegalMonitorStateException 异常
     * notifyAll() 只会唤醒在调用这个方法前 调用了 wait 系列函数而被放入共享变量等待集合里面的线程, 后面放入的是不会被唤醒的
     * <p>
     * 这样使用会直接抛异常
     * object.wait();
     */
    Object object;

    /**
     * Thread 提供的方法
     * <p>
     * join 方法, 阻塞调用线程, 去等待被使用 join 方法的线程执行完毕后恢复 - CountDownLatch 相比 join 粒度更细
     * sleep 方法, 调用线程会让出 cpu 时间片, 并且线程被阻塞挂起, 但是持有的锁是不会被释放的, 指定时间后, 转为就绪状态继而继续争抢 cpu 时间片
     * yield 方法, 暗示线程调度器让出自己的 cpu 时间片, 但是实际上不一定会让出, 不会被阻塞挂起, 而是直接处于就绪状态, 使用较少
     * interrupt 方法, 调用该方法会设置线程的中断状态为 true
     * isInterrupt 方法, 返回当前的线程是否被中断
     * interrupted() , 返回当前线程是否被中断, 并且清楚中断标志
     * <p>
     * 线程的切换会伴随着线程上下文的切换
     */
    Thread thread;


    /*
    synchronized 原子性内置锁, 监视器锁, 内部锁
    在 synchronized 内的变量, 不会从线程的工作内存中获取, 而是会直接从主内存中获取, 退出 synchronized 块会把共享变量的修改直接刷新到主内存

    volatile 关键字, 不加锁解决内存可见性问题
    被 volatile 修饰的变量在被线程写入值的时候会直接刷新到主内存, 读取该变量时会直接从主内存读取最新值
    但是使用 volatile 在进行非原子性操作时依旧会产生问题, a++和++a这种操作就不是原子性的

    jvm 中的守护线程与用户线程
    可以通过 Thread.setDaemon(true)来设置线程为守护线程
    守护线程不会影响jvm的退出, jvm的退出条件是判断是否还存在用户线程没有结束, 如果存在jvm正常情况下是不会退出的

    死锁的形成条件
    1. 互斥条件 -- 指线程对己经获取到的资源进行排他性使用, 即该资源同时只由一个线程占用。如果此时还有其线程请求获取该资源，则请求者只能等待，直至占有资线程释放该资源。
    2. 请求并持有条件 -- 指一个线程己经持有了至少一个资源, 但又提出了新的资源请求, 而新资源己被其他线程占有, 所以当前线程会被阻塞，但阻塞的同时并不释放自己经获取的资源。
    3. 不可剥夺条件 -- 指线程获取到的资源在自己使用完之前不能被其他线程抢占, 只有在自己使用完之后才由自己释放该资源。
    4. 环路等待条件 -- 指在发生死锁时, 必然存在一个线程-资源的环形链, 即线程集合{T0, T1, T2, …， Tn}中 T0 正在等待一个 T1 占用的资源, T1 正在等待一个 T2 占用的资源, ……Tn 在等待己被 T0 占用的资源。
    想要避免死锁, 只需要破坏其中一个条件即可, 目前只有请求并持有和环路等待条件是可以被破坏的
    举个例子: 在遇到两个线程同时抢占两个资源时, 可以让两个线程通过相同的顺序获得两个资源的锁, 破坏形成环路等待的条件

    多线程下存在的问题:
    1. 线程安全问题 - 多个线程同时操作共享内存的值时, 不考虑内存可见性问题, 在事务A提交之前事务B读取了值, 事务A和事务B都对变量进行修改会造成线程安全问题
    2. 内存可见性问题 - 多核cpu存在一级缓存和二级缓存, 一级缓存会造成内存可见性问题,

    Java CAS - compare and swap
    ABA问题
    Unsafe类 - JDK rt.jar包下的提供硬件级别的原子性操作类
    Java指令重排序 - Java内存模型允许编译器和处理器对指令进行重排序以提高运行性能, 并且只会对不存在数据依赖性的指令进行重排序
    伪共享 - cpu和主内存之间存在一级或者多级缓存(Cache), 在缓存内部按行存储,
            如果几个连续的变量在同一个缓存行内又同时被多个cpu的一级缓存存储,
            每个线程操作某个变量时会导致其他cpu的一级缓存内的该变量失效,
            下次读取该变量时又会去主存获取数据导致性能下降, 这就是伪内存
            JDK 8 之前为了避免伪共享的问题, 做法是手动将多个变量放在同一个缓存行内
            JDK 8 之后提供了一个@Contended注解用来解决伪共享问题, 默认情况下这个注解只用于Java核心类
    乐观锁与悲观锁, 公平锁与非公平锁, 独占锁和共享锁, 可重入锁, 自旋锁
     */


    /**
     * ThreadLocal
     * 帮助我们为每个线程创建一个线程本地变量, 其实每个线程的本地变量不是存放在 ThreadLocal 里面, 而是存放在调用线程的 threadLocals 变量里
     * 就是说 ThreadLocal 类型的本地变量存放在具体的线程内存空间中.
     * ThreadLocal 就是个工具壳，它通过 set 方法把 value 值放入调用线程的 threadLocals 里面并存放起来, 当调用线程调用它的 get 时，再从当前线程的 threadLocals 里面将其拿出来使用
     * 如果调用线程一直不终止, 那么这个变量会一直存放在调用线程的 threadLocals 里面
     * 所以当不需要使用本地变量时可以通过调用 ThreadLocal 变量的 remove 方法, 从当前线程 threadLocals 里面删除该本地变量
     * <p>
     * 存放的时候, 其实是以 ThreadLocal 为key, 具体的值为 value 来保存到调用线程的 threadLocals 里的
     * <p>
     * ThreadLocal 不支持继承性, 即子线程是不能获取父线程的 ThreadLocal 的
     * InheritableThreadLocal 可以获取到父线程的 ThreadLocal
     * 具体的操作方式是在 初始化线程的时候把父类的 threadLocals 存放到子类的 inheritableThreadLocals 中, 最后通过 get 方法去拿 inheritableThreadLocals 这里面的值
     */
    ThreadLocal<?> threadLocal;

    /**
     * AtomicInter AtomicLong AtomicBoolean
     * 原理类似, 内部都是通过 Unsafe 类来实现原子性的递增或者递减
     * 在没有这些原子操作类的时候我们可以通过 synchronized 来保证线程安全, 但是使用 synchronized 是一种阻塞算法
     * 这些原子操作类都是基于 cas 非阻塞算法实现的, 所以性能会更好
     */
    AtomicLong atomicLong;

    /**
     * JDK 8 新增比这些性能更好的 LongAdder
     * 为了解决高并发下大量线程同时竞争一个原子变量而造成的大量线程不断自旋尝试 cas 操作, 这会浪费 cpu 资源
     * LongAdder 内部通过 base 和 cell 来减少竞争, 线程会对 cell 进行争抢, 最后的结果由 base 计算所有 cell 的值得到
     * cell 结构简单, 由一个被 volatile 修饰的 long 类型变量组成, 修改 cell 的值时采用 cas 保证原子性
     * 具体哪个线程访问哪个 cell 由 ( getProbe() & cell数组元素个数-1 ) 得到, 这个值作为 cell 数组的下标
     * cells 被初始化时初始大小为 2, 后续扩容为之前的 2 倍, 并复制 cell 的元素到扩容之后的数组
     */
    LongAdder longAdder;

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

    /*
     JDK 提供了一些并发队列 - 按照实现方式不同可以分为阻塞队列和非阻塞队列, 顾名思义, 阻塞队列使用锁实现 非阻塞队列使用 CAS 实现
     非阻塞队列
           ConcurrentLinkedQueue
     阻塞队列
           LinkedBlockingQueue(有界) - 内部主要使用 ReentrantLock,
           ArrayBlockingQueue(有界)
           PriorityBlockingQueue(无界) - 通过比较优先级来确定出列顺序
           DelayQueue(无界) - 元素带有过期时间, 只有过期元素才会出列, 队列头元素是最快要过期的元素. 元素需要继承 Delayed 接口
     */


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建线程的三种方式
        MyThread myThread = new MyThread();
        Thread thread = new Thread(new RunnableTask());
        FutureTask<String> futureTask = new FutureTask<>(new CallerTask());
        Thread thread2 = new Thread(futureTask);
        myThread.start();
        thread.start();
        thread2.start();
        System.out.println(futureTask.get());


    }


}

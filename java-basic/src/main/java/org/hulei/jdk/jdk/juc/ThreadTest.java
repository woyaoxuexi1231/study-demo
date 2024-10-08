package org.hulei.jdk.jdk.juc;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.juc
 * @className: TaskTest
 * @description: 笔记来自 《Java并发编程之美》
 * @author: h1123
 * @createDate: 2022/12/22 19:30
 */

@Slf4j
public class ThreadTest {

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

    /* 线程间同步 */
    /**
     * 线程间同步 - Object 提供的方法
     * Object.wait() - 调用该方法的线程会释放持有对象的监视器锁
     * wait(long timeout) 方法, 没有在执行的 timeout 时间内被唤醒会因为超时而返回, wait(0) 等价于 wait 方法
     * wait(long timeout, int nanos)函数
     * 调用该方法必须持有对象的监视器锁, 否则会抛出 IllegalMonitorStateException 异常
     * 调用该方法的线程会被阻塞挂起, 直到以下情况, 会尝试重新获取监视器锁, 然后往下执行:
     * 1. 其他线程调用 notify() 或者 notifyAll() 方法
     * 2. 其他线程调用该线程的 interrupt() 方法, 该线程会抛出 InterruptedException 异常而返回
     * 需要注意的是, 可能会存在虚假唤醒 (虚假唤醒出现在 notifyAll() 的时候, 当多个线程同时被唤醒由只有一个能获得监视器锁的时候, 前面获得监视器锁的线程把限制条件给改掉了, 导致后面获得监视器锁的线程在错误的条件下执行)
     * 所以在实际使用中, 会在使用 wait 方法的地方使用 while 循环一直判断条件是否满足, 不满足则继续 wait
     * Object.notify() - 调用该方法的线程会唤醒调用了持有对象的 wait 方法的线程
     * notify() 和 notifyAll() 也都是必须获得对象的监视器锁才能使用, 否则会抛出 IllegalMonitorStateException 异常
     * notifyAll() 只会唤醒在调用这个方法前 调用了 wait 系列函数而被放入共享变量等待集合里面的线程, 后面放入的是不会被唤醒的
     */
    Object object;

    /**
     * Thread 提供的方法
     * join - 阻塞调用线程, 去等待被使用 join 方法的线程执行完毕后恢复 - CountDownLatch 相比 join 粒度更细
     * sleep - 调用线程会让出 cpu 时间片, 并且线程被阻塞挂起, 但是持有的锁是不会被释放的, 指定时间后, 转为就绪状态继而继续争抢 cpu 时间片
     * yield - 暗示线程调度器让出自己的 cpu 时间片, 但是实际上不一定会让出, 不会被阻塞挂起, 而是直接处于就绪状态, 使用较少
     * interrupt - 调用该方法会设置线程的中断状态为 true
     * isInterrupt - 返回当前的线程是否被中断
     * interrupted() - 返回当前线程是否被中断, 并且清楚中断标志
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
     * <p>
     * 帮助我们为每个线程创建一个线程本地变量, 其实每个线程的本地变量不是存放在 ThreadLocal 里面, 而是存放在调用线程的 threadLocals 变量里
     * 就是说 ThreadLocal 类型的本地变量存放在具体的线程内存空间中.
     * <p>
     * ThreadLocal 就是个工具壳，它通过 set 方法把 value 值放入调用线程的 threadLocals 里面并存放起来, 当调用线程调用它的 get 时, 再从当前线程的 threadLocals 里面将其拿出来使用
     * 如果调用线程一直不终止, 那么这个变量会一直存放在调用线程的 threadLocals 里面
     * 所以当不需要使用本地变量时可以通过调用 ThreadLocal 变量的 remove 方法, 从当前线程 threadLocals 里面删除该本地变量,
     * 存放的时候, 其实是以 ThreadLocal 为 key, 具体的值为 value 来保存到调用线程的 threadLocals 里的
     * <p>
     * ThreadLocal 不支持继承性, 即子线程是不能获取父线程的 ThreadLocal 的.
     * InheritableThreadLocal 可以获取到父线程的 ThreadLocal.
     * 具体的操作方式是在 初始化线程的时候把父类的 threadLocals 存放到子类的 inheritableThreadLocals 中, 最后通过 get 方法去拿 inheritableThreadLocals 这里面的值
     */
    ThreadLocal<?> threadLocal;

    /**
     * ThreadLocalRandom
     * <p>
     * <p>
     * 对于 Random 这个类来说, 他其实是线程安全的, Random 内部使用cas操作,在多个线程使用同一个 Random 生成随机数的时候, 失败的会进行自旋重试, 这导致降低并发性能, 所以产生了 ThreadLocalRandom
     * 但是 ThreadLocalRandom 由于是多线程,所以对于多个线程来说可能会产生重复的值,所以使用场景需要允许我们存在这种重复的值, 在LongAdder中使用了这个类
     * <p>
     * ThreadLocalRandom 和 ThreadLocal 原理类似
     * ThreadLocalRandom 负责初始化线程持有的种子, 以及负责通过线程给定的种子计算随机数并产生新的种子
     * 当我们在多个线程中使用同一个 ThreadLocalRandom 的时候会导致所有使用这个 ThreadLocalRandom 的线程生成呈某种规律的一组所谓的随机数, 但是这个不是随机数, 这个是个错误的使用方法
     * 当我们以这种方法使用时, 我们并没有初始化我们线程中的 seed 值, 导致我们一直使用 ThreadLocalRandom 中的 GAMMA 的常量作为种子
     * 正确的使用方法应该是和这个类的设计初衷是一样的, 我们在每个线程的内部维护一个 ThreadLocalRandom , 使用 ThreadLocalRandom.current() 来初始化每个线程的 seed
     * 线程内部使用 initialSeed() 这个方法来创建一个随机的初始的种子
     */
    ThreadLocalRandom threadLocalRandom;

    /**
     * LockSupport 工具类 - 主要作用是挂起和唤醒线程
     * <p>
     * park() 类似于 wait() , 被 interrupt() 不会抛异常
     * unpark(Thread thread) 类似与 notify()
     * parkNanos(long nanos)
     */
    LockSupport lockSupport;

    /**
     * AbstractQueuedSynchronizer - AQS, 实现同步器的基础组件, 锁底层使用 AQS 实现
     * <p>
     * 实现原理如下: 只提供内部维护的'阻塞队列的功能',内部维护了两个Node(链表对象)对象,一个头节点,一个尾节点
     * 1. 尝试获取锁的 tryAcquire 由具体子类实现, ReentrantLock 通过一个 state 参数的CAS来尝试获取锁
     * 2. 每个获取锁的线程都创建新的 Node 对象尝试用尾插法插入链表中,然后阻塞当前线程
     * 3. 释放锁的 release 方法从链表中移除当前头节点,然后循环往下唤醒等待的线程
     * 4. 支持可重入,但是具体的逻辑需要子类实现
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


    /*
     * ThreadPoolExecutor (无脑使用ArrayBlockingQueue+CallerRunsPolicy策略这个不会丢掉任何一个任务)
     *
     *  corePoolSize: 指定了线程池中的线程数量, 它的数量决定了添加的任务是开辟新的线程去执行, 还是放到workQueue任务队列中去
     *  maximumPoolSize: 指定了线程池中的最大线程数量, 这个参数会根据你使用的workQueue任务队列的类型, 决定线程池会开辟的最大线程数量,这个参数在无界队列时是无效的(因为任务不会达到上限,也就不会触发线程扩容,更不会触发拒绝策略)
     *  keepAliveTime: 当线程池中空闲线程数量超过corePoolSize时, 多余的线程会在多长时间内被销毁
     *  unit: keepAliveTime的单位
     *  workQueue: 任务队列, 被添加到线程池中, 但尚未被执行的任务; 它一般分为直接提交队列(SynchronousQueue)、有界任务队列(ArrayBlockingQueue)、无界任务队列(LinkedBlockingQueue)、优先任务队列（PriorityBlockingQueue）几种；
     *      SynchronousQueue - 提交的任务不会被保存，总是会马上提交执行。如果用于执行任务的线程数量小于maximumPoolSize
     *                          ，则尝试创建新的进程，如果达到maximumPoolSize设置的最大值，则根据你设置的handler
     *                          执行拒绝策略。因此这种方式你提交的任务不会被缓存起来，而是会被马上执行，在这种情况下，你需要对你程序的并发量有个准确的评估，才能设置合适的maximumPoolSize数量，否则很容易就会执行拒绝策略；
     *      ArrayBlockingQueue - 若有新的任务需要执行时，线程池会创建新的线程，直到创建的线程数量达到corePoolSize
     *                      时，则会将新的任务加入到等待队列中。若等待队列已满，即超过ArrayBlockingQueue初始化的容量，则继续创建线程，直到线程数量达到maximumPoolSize
     *                      设置的最大线程数量，若大于maximumPoolSize
     *                      ，则执行拒绝策略。在这种情况下，线程数量的上限与有界任务队列的状态有直接关系，如果有界队列初始容量较大或者没有达到超负荷的状态，线程数将一直维持在corePoolSize
     *                      以下，反之当任务队列已满时，则会以maximumPoolSize为最大线程数上限。
     *      LinkedBlockingQueue - 使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，也就是说在这种情况下maximumPoolSize
     *                      这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize
     *                      后，就不会再增加了；若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
     *      PriorityBlockingQueue - 其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize
     *                      的数量，只不过其他队列一般是按照先进先出的规则处理任务，而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
     *  threadFactory:线程工厂，用于创建线程，一般用默认即可
     *  handler:拒绝策略；当任务太多来不及处理时，如何拒绝任务
     *      1、AbortPolicy策略(会丢弃任务)：该策略会直接抛出异常，阻止系统正常工作
     *      2、CallerRunsPolicy策略(不会丢弃任务)：如果线程池的线程数量达到上限，该策略会把任务队列中的任务放在调用者线程当中运行
     *      3、DiscardOledestPolicy策略(会丢弃任务)：该策略会丢弃任务队列中最老的一个任务，也就是当前任务队列中最先被添加进去的，马上要被执行的那个任务，并尝试再次提交
     *      4、DiscardPolicy策略(会丢弃任务)：该策略会默默丢弃无法处理的任务，不予任何处理。当然使用此策略，业务场景中需允许任务的丢失
     */
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            20,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(200),
            new ThreadFactoryBuilder().setNamePrefix("juc-threadPoolExecutor").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 通过 CAS 和 AQS 实现的可重复锁
     */
    ReentrantLock reentrantLock;

    // ThreadPoolExecutor 通过worker队列+getTask()阻塞循环获取任务的方式来实现线程的复用
    // protected Runnable getTask() {
    //     boolean timedOut = false; // 标记上一次从队列获取是否超时
    //
    //     for (;;) { // 无限循环以保证线程可以反复获取任务
    //         int c = ctl.get(); // 获取线程池的当前控制状态
    //         int rs = runStateOf(c); // 提取当前的运行状态
    //
    //         // 如果线程池关闭且队列为空，或处于 STOP 状态，则不再接受新任务
    //         if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
    //             decrementWorkerCount(); // 线程即将退出，减少工作线程计数
    //             return null; // 返回 null 让工作者线程退出循环
    //         }
    //
    //         int wc = workerCountOf(c); // 获取线程池的工作者线程数量
    //         // 根据核心线程超时策略和当前线程数量来决定是否设置超时
    //         boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
    //
    //         // 如果工作者线程数超过最大值或上次获取超时，并且存在多于一个工作者或队列为空，尝试减少线程
    //         if ((wc > maximumPoolSize || (timed && timedOut)) && (wc > 1 || workQueue.isEmpty())) {
    //             if (compareAndDecrementWorkerCount(c)) return null; // 如果成功减少计数则让线程退出
    //             continue; // 如果未能减少，重新尝试
    //         }
    //
    //         try {
    //             // 根据超时设置从工作队列中获取任务，或等待直到有任务可获取
    //             // 而避免CPU空转浪费资源就在于 workQueue.take(), 没有任务会阻塞获取
    //             Runnable r = timed ? workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) : workQueue.take();
    //             if (r != null) {
    //                 return r; // 成功获取到任务，返回该任务以供执行
    //             }
    //             timedOut = true; // 如果poll超时且没有获取到任务，则设置超时标志为真
    //         } catch (InterruptedException retry) {
    //             timedOut = false; // 如果在获取任务时被中断，重置超时标志
    //         }
    //     }
    // }

    @SneakyThrows
    private static void futureTaskAndCallable() {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(3000));
            return (System.currentTimeMillis() % 2) == 1 ? "当前毫秒数是单数" : "当前毫秒数是双数";
        });
        new Thread(futureTask).start();

        // futureTask.get() 为阻塞执行, 实现的原理如下: 巧妙的地方在于,原理基于AQS,但是他没有在AQS的基础上实现,或许他认为AQS太重了
        // 1. 内部通过 CAS 去改变stateOffset的值,来标记任务的完成状态
        // 2. 各个线程通过futureTask.get()去获得值的时候,如果在任务没有完成的状态下,会把包装WaitNode(单链表节点)对象,通过CAS去排队塞入futureTask的waiters变量中
        // 3. 排队成功的线程会通过 LockSupport.park(this) 阻塞自己, 后续当 futureTask完成任务后会通过waiters循环 LockSupport.unpark() 唤醒之前阻塞的线程
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println(futureTask.get());
            }
        }).start();
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println(futureTask.get());
            }
        }).start();
    }

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

    @SneakyThrows
    public static void countDownLatch() {
        /*
         * CountDownLatch - 指定数量的线程被执行后, 调用了 CountDownLatch.await() 方法的线程会被唤醒
         * countDown() - 线程调用该方法后, CountDownLatch 内部的计数器会递减, 递减后如果计数器的值为 0, 则唤醒所有因调用 await() 方法被阻塞的线程
         * await() - 线程调用该方法后会被阻塞, 直到 CountDownLatch 内部的计数器值为 0 或者其他线程调用了当先线程的 interrupt() 方法中断了该线程
         *
         * 原理: 内部有一个AQS变量,通过这个变量来控制并发
         * 1. 初始化会赋值给定的State值,这个State值代表需要等待的计数器值
         * 2. 调用countDown()的线程会cas自旋进行state值的递减,知道state=0,那么会唤醒所有在等待的线程
         * 3. 调用await()的线程会尝试是否state=0,如果不等于0,加入AQS等待队列
         */
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(countDownLatch::countDown).start();
        countDownLatch.await();
    }

    @SneakyThrows
    public static void cyclicBarrier() {
        /*
         * CyclicBarrier - 回环屏障
         * parties 用来记录总的线程个数, count 用来记录当前有多少个线程调用了 await
         * await() - 调用该方法的线程会被阻塞, 直到指定数量的线程都调用了 await() 方法, 也就是线程都到了屏障点
         * 所有线程都被唤醒后, CyclicBarrier 的 count 会被重新赋值, 以达到复用的目的
         *
         * 原理:内部持有一个ReentrantLock
         * 1. 每个线程await()的时候先lock上锁, 尝试判断 count-1 是否为0, 如果不为0, 上锁后中途解锁(解锁并不直接unlock(),通过直接操作AQS的state完成),并且加入队列中, 并阻塞当前线程
         * 2. 知道有一个线程 count-1为0, 那么会依次唤醒所有之前阻塞的线程, 这个唤醒操作也有点不同,每个被唤醒的线程先去试图改变state的值(换言之先加锁),完事之后直接 unlock,然后每个线程释放锁之后就继续执行业务代码
         *
         * 相比于CountDownLatch,两者的区别像是 CountDownLatch倾向于一个人等待多个人的结果 而 CyclicBarrier像是一辆公交车等待满员直接发车
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        new Thread(() -> {
            try {
                System.out.println("调用 cyclicBarrier.await() ");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                System.out.println("调用 cyclicBarrier.await() ");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    @SneakyThrows
    public static void semaphoreTest() {
        /*
         * Semaphore - 信号量
         * acquire()/acquire(int permits) - 调用该线程的方法会被阻塞, 直到 Semaphore 的信号量值达到 1/permits, 内部采用具体的公平策略在 AQS 中选择线程进行唤醒
         * release() - 调用该方法的线程会使 Semaphore 的信号量递增
         *
         * 原理: 内部使用AQS作为基础实现,和线程池有些类似,多个线程抢占permits,没有抢到的进行阻塞,相当于就是线程池的任务都抢占线程执行任务
         * 1. 每次执行acquire()的时候对permits进行削减,如果为0,那么直接阻塞
         * 2. 直到有线程主动释放资源,也就是执行release()方法,然后被唤醒的线程继续执行acquire()的逻辑,反复的尝试去获取permits
         */
        Semaphore semaphore = new Semaphore(1);
        new Thread(() -> {
            try {
                log.info("调用 semaphore.acquire() ");
                semaphore.acquire();
                LockSupport.parkNanos(1000 * 1000 * 1000 * 3L);
                log.info("finish semaphore.acquire() ");
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                log.info("调用 semaphore.acquire() ");
                semaphore.acquire();
                LockSupport.parkNanos(1000 * 1000 * 1000 * 3L);
                log.info("finish semaphore.acquire() ");
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        semaphore.acquire();
        log.info("finish semaphore.acquire() ");
        semaphore.release();
    }

    public static void threadPoolExecutorTest() {

        /*
        通过 execute 提交任务 -> 提交任务的同时来决定是任务入列还是新建线程 -> 线程池创建的worker线程run方法只有一个目的,那就是从阻塞队列获取Runnable任务然后执行
        注意: 异常是不会显式抛出的,所以线程池的线程是不会终止的
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                2,
                20,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("threadPoolExecutorTest-").build(),
                new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(() -> {
                log.info("{}", System.currentTimeMillis());
            });
        }
    }

    public static void main(String[] args) {

        // createThread();
        // threadPoolExecutorStatus();
        // futureTaskAndCallable();

        // atomicTest();
        // longAdderTest();

        // cyclicBarrier();
        semaphoreTest();

        // threadPoolExecutorTest();
    }

}

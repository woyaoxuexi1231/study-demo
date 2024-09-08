package org.hulei.jdk.juc;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
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
     * <p>
     * Object.wait() - 调用该方法的线程会释放持有对象的监视器锁
     * wait(long timeout) 方法, 没有在执行的 timeout 时间内被唤醒会因为超时而返回, wait(0) 等价于 wait 方法
     * wait(long timeout, int nanos)函数
     * <p>
     * 调用该方法必须持有对象的监视器锁, 否则会抛出 IllegalMonitorStateException 异常
     * 调用该方法的线程会被阻塞挂起, 直到以下情况, 会尝试重新获取监视器锁, 然后往下执行:
     * <p>
     * 1. 其他线程调用 notify() 或者 notifyAll() 方法
     * 2. 其他线程调用该线程的 interrupt() 方法, 该线程会抛出 InterruptedException 异常而返回
     * <p>
     * 需要注意的是, 可能会存在虚假唤醒 (虚假唤醒出现在 notifyAll() 的时候, 当多个线程同时被唤醒由只有一个能获得监视器锁的时候, 前面获得监视器锁的线程把限制条件给改掉了, 导致后面获得监视器锁的线程在错误的条件下执行)
     * 所以在实际使用中, 会在使用 wait 方法的地方使用 while 循环一直判断条件是否满足, 不满足则继续 wait
     * <p>
     * <p>
     * Object.notify() - 调用该方法的线程会唤醒调用了持有对象的 wait 方法的线程
     * <p>
     * notify() 和 notifyAll() 也都是必须获得对象的监视器锁才能使用, 否则会抛出 IllegalMonitorStateException 异常
     * notifyAll() 只会唤醒在调用这个方法前 调用了 wait 系列函数而被放入共享变量等待集合里面的线程, 后面放入的是不会被唤醒的
     * <p>
     */
    Object object;

    /**
     * Thread 提供的方法
     * <p>
     * join - 阻塞调用线程, 去等待被使用 join 方法的线程执行完毕后恢复 - CountDownLatch 相比 join 粒度更细
     * <p>
     * sleep - 调用线程会让出 cpu 时间片, 并且线程被阻塞挂起, 但是持有的锁是不会被释放的, 指定时间后, 转为就绪状态继而继续争抢 cpu 时间片
     * <p>
     * yield - 暗示线程调度器让出自己的 cpu 时间片, 但是实际上不一定会让出, 不会被阻塞挂起, 而是直接处于就绪状态, 使用较少
     * <p>
     * interrupt - 调用该方法会设置线程的中断状态为 true
     * <p>
     * isInterrupt - 返回当前的线程是否被中断
     * <p>
     * interrupted() - 返回当前线程是否被中断, 并且清楚中断标志
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
     * 对于 Random 这个类来说, 他其实是线程安全的,《Java并发变成之美》一书中说到的, 在多个线程下产生的新种子有可能是一样的, 其实这个概率是比较小的, 因为初始种子会以System.nanoTime()这个返回的值作为生成种子的条件
     * 但是由于 Random 内部使用cas操作,再多个线程使用同一个 Random 生成随机数的时候, 失败的会进行自旋重试, 这导致降低并发性能, 所以产生了 ThreadLocalRandom
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
     * AtomicInter AtomicLong AtomicBoolean
     * <p>
     * 原理类似, 内部都是通过 Unsafe 类来实现原子性的递增或者递减
     * 在没有这些原子操作类的时候我们可以通过 synchronized 来保证线程安全, 但是使用 synchronized 是一种阻塞算法
     * 这些原子操作类都是基于 cas 非阻塞算法实现的, 所以性能会更好
     */
    AtomicLong atomicLong;

    /**
     * JDK 8 新增比这些性能更好的 LongAdder
     * <p>
     * 为了解决高并发下大量线程同时竞争一个原子变量而造成的大量线程不断自旋尝试 cas 操作, 这会浪费 cpu 资源
     * LongAdder 内部通过 base 和 cell 来减少竞争, 线程会对 cell 进行争抢, 最后的结果由 base 计算所有 cell 的值得到
     * cell 结构简单, 由一个被 volatile 修饰的 long 类型变量组成, 修改 cell 的值时采用 cas 保证原子性
     * 具体哪个线程访问哪个 cell 由 ( getProbe() & cell数组元素个数-1 ) 得到, 这个值作为 cell 数组的下标
     * cells 被初始化时初始大小为 2, 后续扩容为之前的 2 倍, 并复制 cell 的元素到扩容之后的数组
     */
    LongAdder longAdder;

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
     * 1. 状态管理：AQS允许同步器定义和管理一个整数状态，用于表示共享资源的状态或可用性。同步器可以通过获取、释放和修改状态来控制对共享资源的访问。
     * 2. 线程排队与阻塞：AQS支持将等待线程以FIFO（先进先出）的顺序排队，这样可以确保公平性。当一个线程尝试获取锁或资源时，如果条件不满足，它会被阻塞并加入到同步队列中等待条件满足。
     * 3. 同步状态的获取与释放：AQS提供了acquire和release等方法，用于获取和释放同步状态。具体来说，通过调用acquire方法可以尝试获取同步状态，如果未成功则会将调用线程阻塞；而调用release方法将释放已持有的同步状态。
     * 4. 实现锁和同步器：AQS是实现各种锁（如ReentrantLock、ReadWriteLock）和同步器（如CountDownLatch、Semaphore）的基础框架。开发者可以通过继承AQS类来自定义同步器，实现特定的同步策略。
     * 5. 可重入性支持：AQS提供了内置的支持来实现可重入锁，允许同一线程重复获取同步状态而不会造成死锁。
     * 6. 条件变量的支持：AQS提供了Condition条件变量的支持，用于实现更复杂的线程协作机制。
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


    /**
     * ThreadPoolExecutor (无脑使用ArrayBlockingQueue+CallerRunsPolicy策略这个不会丢掉任何一个任务)
     * <p>
     * <p>
     * * corePoolSize: 指定了线程池中的线程数量, 它的数量决定了添加的任务是开辟新的线程去执行, 还是放到workQueue任务队列中去
     * <p>
     * <p>
     * * maximumPoolSize: 指定了线程池中的最大线程数量, 这个参数会根据你使用的workQueue任务队列的类型, 决定线程池会开辟的最大线程数量
     * <p>
     * <p>
     * * keepAliveTime: 当线程池中空闲线程数量超过corePoolSize时, 多余的线程会在多长时间内被销毁
     * <p>
     * <p>
     * * unit: keepAliveTime的单位
     * <p>
     * <p>
     * * workQueue: 任务队列, 被添加到线程池中, 但尚未被执行的任务; 它一般分为直接提交队列(SynchronousQueue)、有界任务队列(ArrayBlockingQueue)、
     * 无界任务队列(LinkedBlockingQueue)、优先任务队列（PriorityBlockingQueue）几种；
     * <p>
     * SynchronousQueue - 提交的任务不会被保存，总是会马上提交执行。如果用于执行任务的线程数量小于maximumPoolSize
     * ，则尝试创建新的进程，如果达到maximumPoolSize设置的最大值，则根据你设置的handler
     * 执行拒绝策略。因此这种方式你提交的任务不会被缓存起来，而是会被马上执行，在这种情况下，你需要对你程序的并发量有个准确的评估，才能设置合适的maximumPoolSize数量，否则很容易就会执行拒绝策略；
     * <p>
     * ArrayBlockingQueue - 若有新的任务需要执行时，线程池会创建新的线程，直到创建的线程数量达到corePoolSize
     * 时，则会将新的任务加入到等待队列中。若等待队列已满，即超过ArrayBlockingQueue初始化的容量，则继续创建线程，直到线程数量达到maximumPoolSize
     * 设置的最大线程数量，若大于maximumPoolSize
     * ，则执行拒绝策略。在这种情况下，线程数量的上限与有界任务队列的状态有直接关系，如果有界队列初始容量较大或者没有达到超负荷的状态，线程数将一直维持在corePoolSize
     * 以下，反之当任务队列已满时，则会以maximumPoolSize为最大线程数上限。
     * <p>
     * LinkedBlockingQueue - 使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，也就是说在这种情况下maximumPoolSize
     * 这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize
     * 后，就不会再增加了；若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
     * <p>
     * PriorityBlockingQueue - 其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize
     * 的数量，只不过其他队列一般是按照先进先出的规则处理任务，而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
     * <p>
     * <p>
     * * threadFactory:线程工厂，用于创建线程，一般用默认即可
     * <p>
     * <p>
     * * handler:拒绝策略；当任务太多来不及处理时，如何拒绝任务
     * <p>
     * 1、AbortPolicy策略(会丢弃任务)：该策略会直接抛出异常，阻止系统正常工作
     * <p>
     * 2、CallerRunsPolicy策略(不会丢弃任务)：如果线程池的线程数量达到上限，该策略会把任务队列中的任务放在调用者线程当中运行
     * <p>
     * 3、DiscardOledestPolicy策略(会丢弃任务)：该策略会丢弃任务队列中最老的一个任务，也就是当前任务队列中最先被添加进去的，马上要被执行的那个任务，并尝试再次提交
     * <p>
     * 4、DiscardPolicy策略(会丢弃任务)：该策略会默默丢弃无法处理的任务，不予任何处理。当然使用此策略，业务场景中需允许任务的丢失
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
     * CountDownLatch - 指定数量的线程被执行后, 调用了 CountDownLatch.await() 方法的线程会被唤醒
     * <p>
     * countDown() - 线程调用该方法后, CountDownLatch 内部的计数器会递减, 递减后如果计数器的值为 0, 则唤醒所有因调用 await() 方法被阻塞的线程
     * await() - 线程调用该方法后会被阻塞, 直到 CountDownLatch 内部的计数器值为 0 或者其他线程调用了当先线程的 interrupt() 方法中断了该线程
     */
    CountDownLatch countDownLatch;

    /**
     * CyclicBarrier - 回环屏障
     * <p>
     * parties 用来记录总的线程个数, count 用来记录当前有多少个线程调用了 await
     * <p>
     * await() - 调用该方法的线程会被阻塞, 直到指定数量的线程都调用了 await() 方法, 也就是线程都到了屏障点
     * <p>
     * 所有线程都被唤醒后, CyclicBarrier 的 count 会被重新赋值, 以达到复用的目的
     */
    CyclicBarrier cyclicBarrier;

    /**
     * Semaphore - 信号量
     * <p>
     * acquire()/acquire(int permits) - 调用该线程的方法会被阻塞, 直到 Semaphore 的信号量值达到 1/permits, 内部采用具体的公平策略在 AQS 中选择线程进行唤醒
     * <p>
     * release() - 调用该方法的线程会使 Semaphore 的信号量递增
     */
    Semaphore semaphore;

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
    private static void createThread() {
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

    private static void threadPoolExecutorStatus() {
        // ThreadPoolExecutor 定义为当前线程的状态
        int COUNT_BITS = Integer.SIZE - 3;
        int RUNNING = -1 << COUNT_BITS;
        int SHUTDOWN = 0 << COUNT_BITS;
        int STOP = 1 << COUNT_BITS;
        int TIDYING = 2 << COUNT_BITS;
        int TERMINATED = 3 << COUNT_BITS;
        System.out.println(String.format("%32s", Integer.toBinaryString(COUNT_BITS)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString(RUNNING)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString(SHUTDOWN)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString(STOP)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString(TIDYING)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString(TERMINATED)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString(RUNNING | 0)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString((1 << COUNT_BITS) - 1)).replace(' ', '0'));
        System.out.println(String.format("%32s", Integer.toBinaryString((RUNNING | 0) & (1 << COUNT_BITS) - 1)).replace(' ', '0'));
    }

    public static void main(String[] args) {
        // createThread();
        // threadPoolExecutorStatus();
    }

}

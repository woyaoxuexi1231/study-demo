package org.hulei.basic.jdk.juc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

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


    @SneakyThrows
    public static void main(String[] args) {

        // createThread();
        // threadPoolExecutorStatus();
        // futureTaskAndCallable();

        // atomicTest();
        // longAdderTest();

        // cyclicBarrier();
        // semaphoreTest();

        // threadPoolExecutorTest();


        /*
        Thread 提供的方法
            join() - 阻塞调用线程, 当前线程去等待被使用 join 方法的线程执行完毕后恢复 - CountDownLatch 相比 join 粒度更细
            sleep() - 调用线程会让出 cpu 时间片, 并且线程被阻塞挂起, 但是持有的锁是不会被释放的, 指定时间后, 转为就绪状态继而继续争抢 cpu 时间片
            yield() - 暗示线程调度器让出自己的 cpu 时间片, 但是实际上不一定会让出, 不会被阻塞挂起, 而是直接处于就绪状态, 使用较少
            interrupt() - 调用该方法会设置线程的中断状态为 true
            isInterrupt() - 返回当前线程是否被中断
            interrupted() - 返回当前线程是否被中断, 并且清楚中断标志
        线程的切换会伴随着线程上下文的切换
         */
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("当前时间：{}", LocalDateTime.now());
                try {
                    /*
                    这里 sleep() 和 yield() 方法被设计为静态方法：1.考虑到这两个方法都是针对当前线程 2.全局行为
                     */
                    Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                } catch (InterruptedException e) {
                    break;
                }
            }
            log.info("当前线程已被中断");
        });
        thread.start();
        // LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
        thread.interrupt();
        System.out.println("all task has finished");

        // 线程获取栈
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            System.out.println(stackTraceElement);
        }


        /*
        使用 volatile 来保证内存可见性
        可以防止变量被读取到线程私有内存上后，后续对变量的改变不会再被线程从主内存中获取
         */
        Task task = new Task();
        new Thread(task).start();
        // 主线程等待一秒后终止任务
        Thread.sleep(1000);
        task.stop();

        /*
        对于 Data 这种对象类型的变量，内部的基础类型也是一样的需要使用 volatile 来修饰的
        否则也会导致内存可见性问题
         */
        Data shared = new Data();
        new Thread(new Task2(shared)).start();
        Thread.sleep(1000);
        shared.value = 1;


        /*
        get 会读取 HashMap 的内部结构，编译器和 CPU 不会把整个 map 的引用直接缓存成一个不可变的快照（因为它不是 final 的），因此 CPU 会重新从主内存读取结构。
        但这并不是一个线程安全的操作，所以一定杜绝这种写法
         */
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        new Thread(() -> {
            while (map.get("1") == 1) {

            }
            System.out.println("Thread stopped.");
        }).start();
        Thread.sleep(1000);
        map.put("1", 2);


        /*
        新启动一个线程，然后在线程中启动一个守护线程
        如果没有用户线程，jvm不会等待守护线程执行完毕就直接退出
         */
        new Thread(() -> {
            Thread child = new Thread(() -> {
                while (true) {
                    System.out.println("当前是守护线程。");
                }
            });
            child.setDaemon(true);
            child.start();
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
            System.out.println("线程将会终止");
        }).start();

    }

}

class Task implements Runnable {

    volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            // do something
        }
        System.out.println("Thread stopped.");
    }

    public void stop() {
        running = false;
    }
}


class Data {
    public volatile int value = 0;
}

class Task2 implements Runnable {
    Data data;

    public Task2(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        while (data.value == 0) {
            // do nothing
        }
        System.out.println("Value changed to: " + data.value);
    }
}


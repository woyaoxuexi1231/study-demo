package org.hulei.basic.jdk.juc;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei
 * @since 2025/6/24 20:22
 */

@Slf4j
public class ExecutorServiceTest {

    public static void main(String[] args) {
        // fibonacci();
        // threadPoolExecutorTest();
        scheduledThreadPoolExecutorTest();

    }

    /*
    Executors 提供了一些比较快捷的建立线程池的方法
    newFixedThreadPool 就可以建立固定线程数量的线程池

    就jdk11而言，jdk本身仅提供了三种线程池 ForkJoinPool ScheduledThreadPoolExecutor ThreadPoolExecutor

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
     */
    static ExecutorService executorService = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setNamePrefix("juc-threadPoolExecutor").build());

    /**
    ForkJoinPool 专门用于高效执行可分解的并行任务（尤其是分治算法场景）
    ForkJoinPool 提供了三个执行任务的方法 invoke submit execute
        invoke 执行仅限于 ForkJoinTask，执行此方法后会阻塞直到任务完成
        submit 异步提交任务
        execute 简单后台任务

    TODO 这个很复杂，使用场景目前没有遇到过
     */
    static ForkJoinPool pool = new ForkJoinPool(
            5, // 决定线程池的并行度,也就是线程池中线程的数量,影响线程池能够同时处理的任务数量
            ForkJoinPool.defaultForkJoinWorkerThreadFactory, // 用于制定 ForkJoinWorkerThread的创建,这里使用默认的
            (t, e) -> log.error("ForkJoinPool 发生了异常, thread: {}, ", t.getName(), e), // 用于处理线程中的异常,如果发生了未捕获的异常会触发这个handler
            true // 如果设置为true,线程池在创建时会异步启动工作线程. 如果为false,线程池会在启动时同步启动工作线程.
    );

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
     *      3、DiscardOldestPolicy策略(会丢弃任务)：该策略会丢弃任务队列中最老的一个任务，也就是当前任务队列中最先被添加进去的，马上要被执行的那个任务，并尝试再次提交
     *      4、DiscardPolicy策略(会丢弃任务)：该策略会默默丢弃无法处理的任务，不予任何处理。当然使用此策略，业务场景中需允许任务的丢失
     *

    ThreadPoolExecutor 作为 ExecutorService 的子类，是最常用的线程池

    通过 execute 提交任务 -> 提交任务的同时来决定是任务入列还是新建线程 -> 线程池创建的worker线程run方法只有一个目的,那就是从阻塞队列获取Runnable任务然后执行
    注意: 异常是不会显式抛出的,所以线程池的线程是不会终止的
     */
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            20,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(200),
            new ThreadFactoryBuilder().setNamePrefix("juc-threadPoolExecutor-").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 用于定时任务调度和周期性任务执行的核心类
     * ScheduledThreadPoolExecutor 是 ThreadPoolExecutor 的子类，任务的执行最终都会委派到 ThreadPoolExecutor
     * 定时机制通过 ScheduledFutureTask 实现，这个类的 run 方法内在任务执行完成后会把当前任务再次放入阻塞队列，至于是rate和delay这两种
     */
    static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
            1,
            new ThreadFactoryBuilder().setNamePrefix("scheduledThreadPoolExecutor-").build(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @SneakyThrows
    public static void threadPoolExecutorTest() {
        /*
        对于 threadPoolExecutor 而言，execute 和 submit 的区别在于，execute直接执行一些简单任务，submit可以得到一个Future
         */
        threadPoolExecutor.execute(() -> System.out.println(Thread.currentThread().getName()));
        Future<String> rsp = threadPoolExecutor.submit(() -> System.out.println(Thread.currentThread().getName()), "good");
        System.out.println(rsp.get());
    }

    public static void scheduledThreadPoolExecutorTest() {
        // 延迟指定时间后执行一次性任务。
        scheduledThreadPoolExecutor.schedule(() -> System.out.println("Task executed"), 3, TimeUnit.SECONDS);
        // 以固定频率执行周期性任务。任务按固定频率触发，两次任务的开始时间间隔固定（无论前一次任务是否完成）。
        // 这里尽管设定了两次任务的执行间隔为1，但是任务实际每次需要执行5秒，最终任务会以每五秒执行一次
        scheduledThreadPoolExecutor.scheduleAtFixedRate(
                () -> {
                    log.info("Fixed rate task");
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                0, // 任务首次执行延迟
                1, // 后续任务按 period 间隔触发，无论前一次任务是否完成。
                TimeUnit.SECONDS
        );
        // 以固定延迟执行周期性任务。任务在上一次执行完成后，等待固定延迟时间再触发下一次任务。
        // scheduledThreadPoolExecutor.scheduleWithFixedDelay(
        //         () -> System.out.println("Fixed delay task"),
        //         0, // 任务首次执行延迟
        //         1, // 前一次任务完成后，等待 delay 时间再触发下一次。
        //         TimeUnit.SECONDS
        // );
    }

    /**
     * 斐波那契数列的计算任务
     */
    public static void fibonacci() {
        // 这里内部会有大量的拆分和join操作,超过了ForkJoinPool的parallelism,这里会导致五个线程同时卡住
        // fibonacci前面的任务会等待后面的任务完全执行完毕的结果来统计合,这导致前面五个无限阻塞
        Fibonacci task = new Fibonacci(50);
        Integer result = pool.invoke(task);
        System.out.println("Fibonacci number: " + result);
    }
}


class Fibonacci extends RecursiveTask<Integer> {

    final int n;

    Fibonacci(int n) {
        this.n = n;
    }

    @Override
    public Integer compute() {
        if (n <= 1) {
            return n;
        }
        Fibonacci f1 = new Fibonacci(n - 1);
        f1.fork(); // 拆分任务
        Fibonacci f2 = new Fibonacci(n - 2);
        return f2.compute() + f1.join(); // 获取子任务结果并合并
    }
}

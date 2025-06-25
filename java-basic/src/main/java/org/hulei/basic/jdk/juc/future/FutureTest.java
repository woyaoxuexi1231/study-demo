package org.hulei.basic.jdk.juc.future;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.juc.ExecutorServiceTest;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.entity.jpa.utils.MemoryDbUtil;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei
 * @since 2025/6/25 10:25
 */

@Slf4j
public class FutureTest {

    @SneakyThrows
    public static void main(String[] args) {

        /*
        Callable - 一个用于返回一个结果的任务类，参照于 Runnable 来说，区别在于这个接口可以返回一个结果

        如果直接使用 Thread 来获取异步结果，jdk 提供了 FutureTask 类来实现这种功能
        FutureTask 实现了 RunnableFuture 接口，相当于拥有 Runnable 和 Future 两种特性
        FutureTask 异步结果，这个类主要用于封装 Callable 甚至是 Runnable，并提供一系列方法来实现获取异步结果的操作
                    当使用 Runnable 接口时，可以通过构造器给如一个指定的结果，这个结果并没有特殊含义，这样做的意义仅仅是为了通过FutureTask来检测 Runnable 的执行情况
         */
        FutureTask<Long> futureTask = new FutureTask<>(() -> {
            // LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(3000));
            return 1L;
        });
        new Thread(futureTask).start();

        /*
        futureTask.get() 为阻塞执行, 实现的原理如下: 巧妙的地方在于,原理基于AQS,但是他没有在AQS的基础上实现,或许他认为AQS太重了
            1. 内部通过 CAS 去改变stateOffset的值,来标记任务的完成状态
            2. 各个线程通过futureTask.get()去获得值的时候,如果在任务没有完成的状态下,会把包装WaitNode(单链表节点)对象,通过CAS去排队塞入futureTask的waiters变量中
            3. 排队成功的线程会通过 LockSupport.park(this) 阻塞自己, 后续当 futureTask完成任务后会通过waiters循环 LockSupport.unpark() 唤醒之前阻塞的线程
         */
        Long id = futureTask.get(); // 阻塞线程
        log.info("获取到的用户ID：{}", id);

        /*
        这里 ThreadPoolExecutor 内部返回的也是 futureTask
         */
        Future<Employee> submitted = ExecutorServiceTest.threadPoolExecutor.submit(() -> {
            // LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(3000));
            return MemoryDbUtil.getEmployeeById(id);
        });

        Employee employee = submitted.get(); // 阻塞线程
        log.info("根据ID获取到的雇员信息为：{}", employee);

        /*
        Future使用到这里会发现一些问题：
            1. get 方法会阻塞调用线程
            2. 很难将多个 Future 结果组合起来，组合起来需要额外编码
            3. 缺乏回调机制，无法在计算完成时自动触发后续动作（回调）。开发者必须主动轮询或阻塞等待结果，才能进行下一步处理。
            4. 异常处理困难，Future 的异常只能通过 get() 抛出 ExecutionException 来捕获，处理方式不够灵活和直观，难以在异步链中传播异常。
        为了解决上述future的问题, Java8推出了CompletableFuture,可以异步等待结果并且执行操作
         */
        CompletableFuture
                // 执行有返回值的异步任务
                .supplyAsync(() -> {
                    LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(3000));
                    log.info("开始异步任务...");
                    return 2L;
                }, ExecutorServiceTest.threadPoolExecutor)
                // 链式依赖 串行执行，后续的结果转换和结果消费以及包括这个方法在内都包含另一个 async 方法，async 方法将完全异步并且可以自定义线程池。thenCompose方法将由supplyAsync产生的线程继续执行
                .thenCompose(userId -> {
                    log.info("获取到的用户ID: {}", userId);
                    return CompletableFuture.supplyAsync(() -> MemoryDbUtil.getEmployeeById(userId));
                })
                // 结果转换 可以不进行结果转换
                // 当你的转换函数返回 CompletableFuture 时，总是使用 thenCompose
                // 当你的转换函数返回普通值时，总是使用 thenApply
                .thenApply(e -> {
                    log.info("开始结果转换...");
                    return new StringBuilder().append(e.getFirstName()).append(e.getLastName()).append(", 邮箱为: ").append(e.getEmail());
                })
                // 结果消费 thenRun作为无参数结果消费
                .thenAccept(s -> {
                    log.info("开始消费结果...");
                    log.info("{}", s);
                })
                .exceptionally(ex -> {
                    System.out.println("Failed: " + ex.getCause());
                    return null;
                });

        log.info("main thread has finished");

    }

}

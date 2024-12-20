package org.hulei.basic.jdk.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei
 * @since 2024/9/18 15:47
 */

@Slf4j
public class ForkJoinPoolTest {

    // ForkJoinPool的使用难点在于这个RecursiveTask/
    static ForkJoinPool pool = new ForkJoinPool(
            5, // 决定线程池的并行度,也就是线程池中线程的数量,影响线程池能够同时处理的任务数量
            ForkJoinPool.defaultForkJoinWorkerThreadFactory, // 用于制定 ForkJoinWorkerThread的创建,这里使用默认的
            (t, e) -> log.error("ForkJoinPool 发生了异常, thread: {}, ", t.getName(), e), // 用于处理线程中的异常,如果发生了未捕获的异常会触发这个handler
            true // 如果设置为true,线程池在创建时会异步启动工作线程. 如果为false,线程池会在启动时同步启动工作线程.
    );

    public static void main(String[] args) {
        recursiveAction();
        customForkJoinTask();
    }

    /**
     * RecursiveTask任务带有返回值
     */
    public static void recursiveTask() {
        RecursiveTask<Integer> recursiveTask = new RecursiveTask<Integer>() {
            @Override
            protected Integer compute() {
                LockSupport.parkNanos(3L * 1000 * 1000 * 1000);
                return 0;
            }
        };
        // invoke会阻塞直到结果完成才会接着往下执行
        pool.invoke(recursiveTask);
        System.out.println(recursiveTask.getRawResult());
    }

    /**
     * recursiveAction不带返回值的任务
     */
    public static void recursiveAction() {
        RecursiveAction recursiveAction = new RecursiveAction() {
            @Override
            protected void compute() {
                LockSupport.parkNanos(3L * 1000 * 1000 * 1000);
                log.info("recursiveAction done!");
            }
        };
        // invoke会阻塞直到结果完成才会接着往下执行
        pool.invoke(recursiveAction);
        System.out.println(recursiveAction.getRawResult());
    }

    /**
     * 自定义的ForkJoinTask
     */
    public static void customForkJoinTask() {

        ForkJoinTask<String> forkJoinTask = new ForkJoinTask<String>() {

            String result;

            @Override
            public String getRawResult() {
                return result;
            }

            @Override
            protected void setRawResult(String value) {
                this.result = value;
            }

            @Override
            protected boolean exec() {
                System.out.println("customForkJoinTask done!");
                return true;
            }
        };

        pool.invoke(forkJoinTask);
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
package com.hundsun.demo.springboot.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: ScheduledController
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 16:40
 */

@Slf4j
@RestController
@RequestMapping("/scheduled")
public class ScheduledController {

    @GetMapping("/threadPool")
    public void threadPool() {
        log.info("threadPool");
        /*
         * 阿里云强制不允许这种创建方式
         * 1. FixedThreadPool 和 SingleThreadPool: 允许的请求队列长度为 Integer.MAX_VALUE, 可能会堆积大量的请求, 从而导致 OOM。
         * 2. CachedThreadPool 和 ScheduledThreadPool: 允许的创建线程数量为Integer.MAX_VALUE, 可能会创建大量的线程, 从而导致OOM。
         */
        // ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("scheduled-thread");
            thread.setDaemon(true);
            return thread;
        });
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.info("定时任务执行.");
            throw new RuntimeException("error");
        }, 1L, 1L, TimeUnit.SECONDS);

        ExecutorService pool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2)) {

        };
    }

    private static volatile Integer integer = 1;

    private final Object object = new Object();

    @SneakyThrows
    @Scheduled(cron = "* * * * * ?")
    public void scheduled() {
        synchronized (object) {
            integer++;
        }
        // if (integer == 3) {
        //     throw new RuntimeException("error");
        // }
        // log.info("{}", integer);
    }

    @SneakyThrows
    @Scheduled(cron = "* * * * * ?")
    public void schedule() {
        // log.info("{}", new Date());
        // 这里会阻塞其他的定时任务执行
        // Thread.sleep(10000);
        // 这里抛出异常, 这个定时任务不会被终止
        // org/springframework/scheduling/support/DelegatingErrorHandlingRunnable#run 在这里异常被捕获了
        /*
        在Spring框架中，使用`@Scheduled`注解的方法在执行过程中如果抛出了异常，不会导致整个调度任务被终止。这是因为Spring框架内部对`@Scheduled`注解的处理机制决定的。具体来说，Spring框架使用`ThreadPoolTaskScheduler`来处理调度任务，当任务抛出异常时，Spring会使用`LoggingErrorHandler`来处理这个异常¹。这意味着异常会被捕获并记录，而不会导致整个调度任务被终止。
        如果你希望自定义异常处理逻辑，你可以在`ThreadPoolTaskScheduler`中设置自己的`ErrorHandler`¹。这样，你就可以控制在任务抛出异常时应该执行的操作。
        需要注意的是，虽然Spring框架允许`@Scheduled`注解的方法在抛出异常后继续运行，但在实际编程中，我们通常会在`@Scheduled`注解的方法内部使用try-catch语句来处理可能出现的异常²。这样可以确保我们能够对异常进行适当的处理，例如记录错误信息，或者在某些情况下重新尝试执行任务。
        希望这个解释能帮助你理解这个问题。😊

        Source: Conversation with Bing, 2023/11/7
        (1) Universal exception handler for @Scheduled tasks in Spring (Boot) with .... https://stackoverflow.com/questions/41041536/universal-exception-handler-for-scheduled-tasks-in-spring-boot-with-java-conf.
        (2) Exception handling for Spring 3.2 "@Scheduled" annotation. https://stackoverflow.com/questions/24031613/exception-handling-for-spring-3-2-scheduled-annotation.
        (3) Spring - Task Scheduling using @Scheduled - LogicBig. https://www.logicbig.com/tutorials/spring-framework/spring-core/scheduled-annotation.html.
        (4) java - ScheduledExecutorService Exception handling - Stack Overflow. https://stackoverflow.com/questions/6894595/scheduledexecutorservice-exception-handling.
         */
        // throw new RuntimeException("error");
    }
}

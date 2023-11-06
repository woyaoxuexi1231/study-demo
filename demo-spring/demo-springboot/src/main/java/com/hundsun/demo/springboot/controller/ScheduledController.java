package com.hundsun.demo.springboot.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
@RestController(value = "/scheduled")
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

    private static Integer integer = 1;

    @SneakyThrows
    @Scheduled(cron = "* * * * * ?")
    public void scheduled() {
        integer++;
        // if (integer == 3) {
        //     throw new RuntimeException("error");
        // }
        log.info("{}", integer);
    }

    @SneakyThrows
    @Scheduled(cron = "* * * * * ?")
    public void schedule() {
        log.info("{}", new Date());
        // 这里会阻塞其他的定时任务执行
        Thread.sleep(10000);
        // 这里抛出异常, 这个定时任务不会被终止
        throw new RuntimeException("error");
    }
}

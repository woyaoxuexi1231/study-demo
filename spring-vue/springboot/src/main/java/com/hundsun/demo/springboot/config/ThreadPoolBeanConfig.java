package com.hundsun.demo.springboot.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: ThreadPoolBeanConfig
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-23 10:09
 */

@Configuration
public class ThreadPoolBeanConfig {

    @Bean
    public ThreadPoolExecutor commonPool() {
        return new ThreadPoolExecutor(
                200,
                200,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("SINGLE-TRANSACTION-POOL-").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Bean
    public ThreadPoolExecutor singlePool() {
        return new ThreadPoolExecutor(
                1,
                1,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new ThreadFactoryBuilder().setNamePrefix("single-pool-").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    public ThreadPoolExecutor singleTransactionPool() {
        return new ThreadPoolExecutor(
                2,
                2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new ThreadFactoryBuilder().setNamePrefix("singleTransactionPool-").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}

package org.hulei.mybatis.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hulei
 * @since 2024/11/17 16:19
 */

@MapperScan(basePackages = {"org.hulei.mybatis.mapper"})
@SpringBootApplication
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                5,
                5,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactory() {

                    final AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("mybatisPlusThread-" + atomicInteger.getAndIncrement());
                        return thread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}

package org.hulei.springboot.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
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
 * @since 2024/9/19 19:05
 */


@MapperScan(basePackages = {"org.hulei.springboot.mybatisplus.mapper"})
@SpringBootApplication
public class MybatisPlusApplication {

    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                5,
                5,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactory() {

                    AtomicInteger atomicInteger = new AtomicInteger(0);

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

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // TODO 这是什么插件?
        // interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    public static void main(String[] args) {
        // cat日志常常配合grep使用, grep -A(after)显示之后的 n行, -B(before)显示之前的几行 -C(context)显示前后个n行
        SpringApplication.run(MybatisPlusApplication.class, args);
    }
}

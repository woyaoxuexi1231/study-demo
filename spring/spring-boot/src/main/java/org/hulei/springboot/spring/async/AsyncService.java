package org.hulei.springboot.spring.async;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.spring.listener.SimpleEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

/**
 * @author hulei
 * @since 2024/12/27 11:49
 */

@Slf4j
@Component
public class AsyncService implements ApplicationListener<SimpleEvent> {

    @Async
    public void simple() {
        log.info("this is async, 当前线程为: {}", Thread.currentThread().getName());
    }

    Semaphore semaphore = new Semaphore(2);

    @Async("commonTaskExecutor")
    @Override
    public void onApplicationEvent(SimpleEvent event) {
        try {
            semaphore.acquire();
            log.info("this is onApplicationEvent, 当前线程为: {}", Thread.currentThread().getName());
            Thread.sleep(10 * 1000);
        } catch (Exception e) {

        } finally {
            semaphore.release();
        }
    }
}

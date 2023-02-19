package com.hundsun.demo.dubbo.consumer.service.impl;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.hundsun.demo.dubbo.consumer.api.service.LocalLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-02 10:25
 */

@Service
@Slf4j
public class LocalLockServiceImpl implements LocalLockService {

    /**
     * ThreadPoolExecutor
     */
    private static final ThreadPoolExecutor CONSUMER_TEST_POOL = new ThreadPoolExecutor(1, 2, 20,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), new ThreadFactoryBuilder()
            .setNamePrefix("consumer-test-thread-").build(), new ThreadPoolExecutor.CallerRunsPolicy());

}

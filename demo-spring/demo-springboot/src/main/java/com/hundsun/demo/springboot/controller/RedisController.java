package com.hundsun.demo.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: RedisController
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-23 09:56
 */

@Slf4j
@RestController
public class RedisController {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    @Qualifier("singlePool")
    ThreadPoolExecutor single;

    @Autowired
    ThreadPoolExecutor commonPool;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/test")
    public void test() {
        single.execute(() -> {
            RLock rLock = redissonClient.getLock("DEMO:TEST");
            if (rLock.tryLock()) {
                try {
                    log.info("lock success! ");
                    Thread.sleep(2 * 1000);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    rLock.unlock();
                }
            } else {
                log.info("no");
            }
        });
    }

    @GetMapping("/send")
    public void send() {
        for (int i = 0; i < 2; i++) {
            commonPool.execute(() -> {
                String url = "http://localhost:9094/test";
                restTemplate.getForEntity(url, null);
            });
        }
    }

}

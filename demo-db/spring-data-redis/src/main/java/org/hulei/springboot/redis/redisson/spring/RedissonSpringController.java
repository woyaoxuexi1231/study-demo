package org.hulei.springboot.redis.redisson.spring;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/1/8 14:29
 */

@RequestMapping("/redisson")
@RestController
public class RedissonSpringController {

    /*
    Redisson 是一个 Java 客户端库（官方推荐）
    它在 Redis 基础之上封装了：
        - 分布式锁（可重入锁、读写锁、公平锁、联锁、红锁）
        - 分布式集合（Map、Set、List）
        - 缓存对象（带自动过期）
        - 限流器、信号量、发布订阅等
    也就是说，Redisson 相当于给 Java 提供了一个更像 Java 本地对象的“分布式工具包”，而不是自己去写原生 SET NX PX。

    Redisson 用于什么场景？
        - 需要分布式锁（秒杀、库存扣减、幂等操作）
        - 需要分布式同步工具（信号量、CountDownLatch）
        - 需要在集群中一致性操作（RedLock）
        - 想用 Redis 来做本地对象缓存或 Map/Queue/Set
     */

    @Autowired
    RedissonClient redisson;

    @RequestMapping("/test")
    public void test() {

    }
}

package org.hulei.springboot.redis.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2025/1/8 14:29
 */

@Slf4j
@RequestMapping("/redisson")
@RestController
public class RedissonLockController {

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

    @Autowired
    ThreadPoolExecutor commonPool;

    @PostMapping("/lock")
    public void lock() {
        /*
        使用 SETNX 实现分布式锁的常见问题及解决方案
        🚨1. 锁未设置过期时间导致的死锁 如果线程 A 获取锁后，因异常或网络问题未主动释放锁，锁会永久存在，其他线程无法获取。
        🚨2. 锁过期时间不合理导致的并发问题 若业务执行时间超过锁的过期时间，锁会被自动释放，其他线程可能获取锁并操作共享资源，导致并发冲突。
        🚨3. 原子性操作缺失 SETNX 和 EXPIRE 分两步执行，若服务器宕机，可能导致锁无过期时间。
        🚨4. 误删锁 线程 A 删除锁时，可能误删线程 B 的锁（如锁过期后被其他线程获取，但 A 仍执行删除）。
        🚨5. 主从同步导致的锁丢失 Redis 主从复制异步，若主库宕机，从库未同步锁数据，可能导致锁丢失。

        Redisson如何解决的呢？
        💡Redisson 所有锁操作（加锁、释放锁、续期）均通过 Lua 脚本在 Redis 服务端原子执行，避免多条命令的竞态条件。
        💡Redisson 内置“看门狗”（Watchdog）机制，默认开启（可通过配置关闭）。解决了手动设置 TTL 不合理的问题，无需开发者干预锁的续期。

        redisson实现分布式锁的原理:
        1. 主线程尝试通过lua脚本创建map类型的数据结构,key是锁的名称,内容是锁的持有者和重入的次数
            - 获得锁的线程将正常执行业务逻辑
            - 没有获得锁的线程将通过redis的subscribe订阅一个频道,频道的名称是 redisson_lock__channel:{lockName}, 这是为了在 unlock 的第一时间就可以进行抢锁 而不必再等待 ttl 了
            - 没有获得锁的线程会一直轮询的去检查锁这个键的剩余时间,然后等待到足够时间后尝试去获取锁
        2. 子线程通过一个Map保存当前线程的信息,然后子线程默认每10秒进行续约
        3. 主线程正常解锁时会删除子线程Map内保存的线程信息,以及发送一条解锁的消息到频道内

        watchdog如何感知到主线程已经挂了呢？
          ① finally在一定执行的情况下，unlock会删除 EXPIRATION_RENEWAL_MAP 中保存的线程id信息，这样watch下次续约的时候就可以知道了
          ② 如果说jvm挂了，那么watchdog线程肯定也没了，这种情况下只能等ttl结束了
         */

        commonPool.execute(() -> {
            RLock lock = redisson.getLock("myLock");
            try {
                // 尝试获取锁 waiting - 等待获取锁的时间 leaseTime - 持有锁的时间 unit - 时间单位
                if (lock.tryLock(1, -1, TimeUnit.SECONDS)) {
                    log.info("已经通过redisson获得锁!");
                    Thread.sleep(50 * 1000); // 模拟业务逻辑执行时间
                } else {
                    log.info("当前已有其他线程在执行任务，跳过");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    log.info("已经成功释放锁");
                }
            }
        });
    }

    @PostMapping("/red-lock")
    public void redLock(){
        /*
        Redisson 的 RedLock（红锁） 是基于 Redis 的 分布式互斥锁 实现，专为解决 分布式系统中的资源竞争问题 设计。
        它通过 多实例冗余 的方式提升了锁的可靠性，避免了传统单 Redis 实例锁的单点故障问题，是 Redisson 分布式锁家族中最核心的组件之一。

        🚨传统的分布式锁（如基于单个 Redis 实例的 RLock）存在一个潜在风险：如果 Redis 主节点宕机且未同步到从节点，锁状态可能丢失，导致多个客户端同时获取同一把锁，引发并发问题。
        💡RedLock 的核心目标是：通过多个独立的 Redis 实例（通常为奇数个，如 5 个），构建一个“多数派”机制，确保即使部分实例故障，锁仍然有效。

        RedLock 获取锁的步骤（以 5 个 Redis 实例为例）：
          1. 生成唯一标识：客户端生成一个 唯一随机值（lockValue），用于标识本次锁请求（避免误释放其他客户端的锁）。
          2. 尝试获取所有实例的锁：客户端依次向 5 个 Redis 实例发起 SET命令（带 NX和 PX参数），设置锁的过期时间（expireTime，通常为业务逻辑执行时间的 2~3 倍）。
          3. 统计成功次数：客户端统计成功获取锁的实例数量（记为 successCount）。
          4. 判断是否获取成功：
              如果 successCount ≥ (总实例数 / 2 + 1)（即多数实例成功），则认为锁获取成功。
              否则，认为锁获取失败，需要向所有已成功获取锁的实例发起 DEL命令释放锁。

        但是目前 在 Redisson 3.17.0+ 版本中，Redlock接口被标记为 @Deprecated，并建议改用 getLock方法。原因包括：
          1. 时钟漂移（Clock Drift）：如果 Redis 节点的时钟不同步，可能导致锁提前过期。
          2. 网络延迟：在极端情况下，客户端可能误判锁状态。
          3. Redlock 要求 多个独立的 Redis 节点（不能是主从复制，必须是 Cluster 或哨兵模式）。部署成本较高，且仍存在理论上的缺陷。
         */
    }
}

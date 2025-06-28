package org.hulei.springboot.redis.redisson.basic;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("CallToPrintStackTrace")
public class RedissonLock {

    public static void main(String[] args) {

        RedissonClient redissonClient = RedissonConnectionFactory.getRedissonClient();

        /*
        redisson实现分布式锁的原理:
        1. 主线程尝试通过lua脚本创建map类型的数据结构,key是锁的名称,内容是锁的持有者和重入的次数
            - 获得锁的线程将正常执行业务逻辑
            - 没有获得锁的线程将通过redis的subscribe订阅一个频道,频道的名称是 redisson_lock__channel:{lockName}, 这是为了在 unlock 的第一时间就可以进行抢锁 而不必再等待 ttl 了
            - 没有获得锁的线程会一直轮询的去检查锁这个键的剩余时间,然后等待到足够时间后尝试去获取锁
        2. 子线程通过一个Map保存当前线程的信息,然后子线程默认每10秒进行续约
        3. 主线程正常解锁时会删除子线程Map内保存的线程信息,以及发送一条解锁的消息到频道内

        watchdog如何感知到主线程已经挂了呢？finally在一定执行的情况下，unlock会删除 EXPIRATION_RENEWAL_MAP 中保存的线程id信息，这样watch下次续约的时候就可以知道了
        如果说jvm挂了，那么watchdog线程肯定也没了，这种情况下只能等ttl结束了
         */

        Runnable task = () -> {
            RLock lock = redissonClient.getLock("myLock");
            try {
                // 尝试获取锁 waiting-等待获取锁的时间 leaseTime-持有锁的时间 unit-时间单位
                if (lock.tryLock(1, -1, TimeUnit.SECONDS)) {
                    log.info("已经通过redisson获得锁!");
                    Thread.sleep(50 * 1000); // 模拟业务逻辑执行时间
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("isLocked : {}", lock.isLocked());
                log.info("isHeldByCurrentThread : {}", lock.isHeldByCurrentThread());
                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    log.info("已经成功释放锁");
                } else {
                    log.info("未获取到锁");
                }
            }
        };

        new Thread(task, "redisson_lock_1").start();
        new Thread(task, "redisson_lock_2").start();
        new Thread(task, "redisson_lock_3").start();
    }
}

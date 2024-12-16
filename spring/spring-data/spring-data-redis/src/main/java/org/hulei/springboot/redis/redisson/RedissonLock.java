package org.hulei.springboot.redis.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@Slf4j
@SuppressWarnings("CallToPrintStackTrace")
public class RedissonLock {

    public static void main(String[] args) {

        // 创建 Redisson 配置对象
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.80.128:6379")
                .setPassword("123456");
        // 创建 Redisson 客户端
        RedissonClient redisson = Redisson.create(config);

        /*
        redisson实现分布式锁的原理:
        1. 主线程尝试通过lua脚本创建map类型的数据结构,key是锁的名称,内容是锁的持有者和重入的次数
            - 获得锁的线程将正常执行业务逻辑
            - TODO 2024年9月19日(这个频道具体有什么作用,还不知道) 没有获得锁的线程将通过redis的subscribe订阅一个频道,频道的名称是 redisson_lock__channel:{lockName}
            - 没有获得锁的线程会一直轮询的去检查锁这个键的剩余时间,然后等待到足够时间后尝试去获取锁
        2. 子线程通过一个Map保存当前线程的信息,然后子线程默认每10秒进行续约
        3. 主线程正常解锁时会删除子线程Map内保存的线程信息,以及发送一条解锁的消息到频道内
         */
        RLock lock = redisson.getLock("myLock");

        Runnable task = () -> {
            try {
                // 尝试获取锁
                lock.lock();
                log.info("已经通过redisson获得锁!");
                Thread.sleep(5 * 1000); // 模拟业务逻辑执行时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                log.info("已经成功释放锁");
            }
        };

        new Thread(task, "redisson_lock_1").start();
        // new Thread(task, "redisson_lock_2").start();
        // new Thread(task, "redisson_lock_3").start();
    }
}

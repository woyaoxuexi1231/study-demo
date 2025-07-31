package org.hulei.springboot.redis.redisson.basic;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

public class RateLimiterExample {
    public static void main(String[] args) throws InterruptedException {

        RedissonClient redisson = RedissonConnectionFactory.getRedissonClient();

        RRateLimiter rateLimiter = redisson.getRateLimiter("myRateLimiter");
        // 每秒最多 5 个操作
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        for (int i = 0; i < 10; i++) {
            if (rateLimiter.tryAcquire()) {
                System.out.println("Allowed: " + i);
            } else {
                System.out.println("Blocked: " + i);
            }
            Thread.sleep(100);
        }

        redisson.shutdown();
    }
}

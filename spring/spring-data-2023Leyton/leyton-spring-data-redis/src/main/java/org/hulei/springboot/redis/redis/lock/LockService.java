package org.hulei.springboot.redis.redis.lock;

import lombok.Data;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Data
@Service
public class LockService {

    private final RedisDistributedLock redisLock;
    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final long DEFAULT_WAIT_TIME = 100;

    public LockService(RedisDistributedLock redisLock) {
        this.redisLock = redisLock;
    }

    /**
     * 获取锁（带重试）
     */
    @Retryable(value = {LockFailedException.class}, 
               maxAttempts = DEFAULT_RETRY_TIMES,
               backoff = @Backoff(delay = DEFAULT_WAIT_TIME))
    public String acquireLockWithRetry(String lockKey, long expireTime) {
        String lockId = redisLock.tryLock(lockKey, expireTime);
        if (lockId == null) {
            throw new LockFailedException("获取锁失败");
        }
        return lockId;
    }

    public static class LockFailedException extends RuntimeException {
        public LockFailedException(String message) {
            super(message);
        }
    }
}
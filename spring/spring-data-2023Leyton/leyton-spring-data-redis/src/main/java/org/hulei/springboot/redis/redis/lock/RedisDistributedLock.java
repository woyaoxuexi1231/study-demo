// package org.hulei.springboot.redis.redis.lock;
//
// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.data.redis.core.script.DefaultRedisScript;
// import org.springframework.stereotype.Component;
//
// import java.util.Collections;
// import java.util.UUID;
// import java.util.concurrent.TimeUnit;
//
// @Component
// public class RedisDistributedLock {
//
//     private final StringRedisTemplate redisTemplate;
//
//     // 解锁Lua脚本
//     private static final String UNLOCK_SCRIPT =
//         "if redis.call('get', KEYS[1]) == ARGV[1] then " +
//         "   return redis.call('del', KEYS[1]) " +
//         "else " +
//         "   return 0 " +
//         "end";
//
//     public RedisDistributedLock(StringRedisTemplate redisTemplate) {
//         this.redisTemplate = redisTemplate;
//     }
//
//     /**
//      * 获取分布式锁
//      * @param lockKey 锁的key
//      * @param expireTime 锁过期时间(毫秒)
//      * @return 锁标识（用于释放锁）
//      */
//     public String tryLock(String lockKey, long expireTime) {
//         String lockId = UUID.randomUUID().toString();
//         /*
//         setIfAbsent 包含 set nx px 三个操作
//         只有当 key 不存在时才设置指定过期时间的 key
//          */
//         Boolean success = redisTemplate.opsForValue()
//             .setIfAbsent(lockKey, lockId, expireTime, TimeUnit.MILLISECONDS);
//         return success != null && success ? lockId : null;
//     }
//
//     /**
//      * 释放分布式锁
//      * @param lockKey 锁的key
//      * @param lockId 锁标识
//      * @return 是否释放成功
//      */
//     public boolean unlock(String lockKey, String lockId) {
//         /*
//         解锁使用 Lua 脚本保证原子性，验证lockId避免误删其他客户端的锁
//         只有当确实删除了数据才返回 1
//          */
//         DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
//         Long result = redisTemplate.execute(
//             redisScript,
//             Collections.singletonList(lockKey),
//             lockId);
//         return result != null && result == 1;
//     }
// }
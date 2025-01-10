package org.hulei.springboot.redis.redisson.basic;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author hulei
 * @since 2025/1/8 14:19
 */

public class RedissonConnectionFactory {

    // 创建 Redisson 配置对象
    private static final Config config;

    static {
        config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.80.128:6379")
                .setPassword("123456");
    }

    public static RedissonClient getRedissonClient() {
        // 创建 Redisson 客户端
        return Redisson.create(config);
    }
}

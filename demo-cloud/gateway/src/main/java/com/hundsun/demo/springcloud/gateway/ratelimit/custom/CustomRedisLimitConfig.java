package com.hundsun.demo.springcloud.gateway.ratelimit.custom;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author hulei
 * @since 2024/10/23 11:06
 */

@EnableConfigurationProperties
@Data
@Configuration
@ConfigurationProperties(prefix = "redis.limit")
public class CustomRedisLimitConfig {

    List<RedisLimit> redisLimits;

    @Data
    public static class RedisLimit {
        /**
         * 间隔时间
         */
        private Integer IntervalSec;
        /**
         * 间隔时间内最多允许触发的次数
         */
        private Integer count;
        /**
         * 接口名
         */
        private String apiName;
        /**
         * 用户名
         */
        private String userName;
    }
}

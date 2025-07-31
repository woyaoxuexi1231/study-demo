package org.hulei.springboot.redis.redis.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/11/14 19:35
 */

@Slf4j
@RestController
@RequestMapping("/redis-limit")
@RequiredArgsConstructor
public class RedisLimitingController {

    private final StringRedisTemplate stringRedisTemplate;

    int count = 10;

    @GetMapping("/add")
    public void add() {

        // 限流 key share:service:rate_limit:code_version:user
        String redisKey = "limiting:add";

        // 清空过期键值
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(redisKey))) {
            stringRedisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, System.currentTimeMillis() - 10000);

            Long size = stringRedisTemplate.opsForZSet().size(redisKey);

            if (Objects.isNull(size) || size < count) {
                stringRedisTemplate.opsForZSet().add(redisKey, UUID.randomUUID().toString(), System.currentTimeMillis());
            } else {
                throw new RuntimeException("限流了");
            }
        } else {
            stringRedisTemplate.opsForZSet().add(redisKey, UUID.randomUUID().toString(), System.currentTimeMillis());
        }

    }
}

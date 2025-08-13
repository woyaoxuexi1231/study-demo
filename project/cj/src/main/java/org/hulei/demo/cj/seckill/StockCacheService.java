package org.hulei.demo.cj.seckill;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StockCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Cache<Long, Integer> localCache;

    public StockCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.localCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(100, TimeUnit.MILLISECONDS) // 短时缓存
            .build();
    }

    public int getStock(long productId) {
        // 先查本地缓存
        Integer stock = localCache.getIfPresent(productId);
        if (stock != null) {
            return stock;
        }

        // 查Redis
        stock = (Integer) redisTemplate.opsForValue().get("seckill:stock:" + productId);
        if (stock == null) {
            return 0;
        }

        // 更新本地缓存
        localCache.put(productId, stock);
        return stock;
    }
}
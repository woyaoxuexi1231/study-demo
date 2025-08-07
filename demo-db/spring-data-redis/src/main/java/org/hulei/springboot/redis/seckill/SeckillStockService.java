package org.hulei.springboot.redis.seckill;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class SeckillStockService {

    private final StringRedisTemplate redisTemplate;
    private static final String STOCK_KEY_PREFIX = "seckill:stock:";
    private static final String USER_LIMIT_KEY_PREFIX = "seckill:user_limit:";
    
    // 初始化商品库存
    public void initStock(long productId, int stock) {
        String key = STOCK_KEY_PREFIX + productId;
        redisTemplate.opsForValue().set(key, String.valueOf(stock));
        
        // 设置过期时间（秒杀结束后自动清理）
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
    }
    
    // 获取剩余库存
    public int getRemainStock(long productId) {
        String stock = redisTemplate.opsForValue().get(STOCK_KEY_PREFIX + productId);
        return stock != null ? Integer.parseInt(stock) : 0;
    }
}
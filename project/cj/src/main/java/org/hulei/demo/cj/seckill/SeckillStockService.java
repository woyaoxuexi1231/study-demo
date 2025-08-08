package org.hulei.demo.cj.seckill;

import lombok.RequiredArgsConstructor;
import org.hulei.entity.mybatisplus.domain.BigDataProducts;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataProductMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class SeckillStockService {

    private final BigDataProductMapper bigDataProductMapper;
    private final StringRedisTemplate redisTemplate;
    private static final String STOCK_KEY_PREFIX = "seckill:stock:";
    private static final String USER_LIMIT_KEY_PREFIX = "seckill:user_limit:";

    // 初始化商品库存
    @Transactional(rollbackFor = Exception.class)
    public void initStock(long productId, int stock) {
        // 查询数据库的商品信息
        BigDataProducts product = bigDataProductMapper.selectById(productId);
        if (Objects.nonNull(product) && product.getQuantity() >= stock) {
            String key = STOCK_KEY_PREFIX + productId;
            // 不存在时设置，设置过期时间（秒杀结束后自动清理）
            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(stock), 2, TimeUnit.HOURS))) {
                // 修改库存
                product.setQuantity(product.getQuantity() - stock);
                bigDataProductMapper.updateById(product);
            }
        }
    }

    // 获取剩余库存
    public int getRemainStock(long productId) {
        String stock = redisTemplate.opsForValue().get(STOCK_KEY_PREFIX + productId);
        return stock != null ? Integer.parseInt(stock) : 0;
    }
}
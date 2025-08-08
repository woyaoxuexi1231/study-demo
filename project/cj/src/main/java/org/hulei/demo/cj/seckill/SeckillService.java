package org.hulei.demo.cj.seckill;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SeckillService {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> seckillScript;

    public SeckillService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.seckillScript = new DefaultRedisScript<>();
        this.seckillScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("seckill/seckill.lua")));
        this.seckillScript.setResultType(Long.class);
    }

    public SeckillResult trySeckill(long productId, long userId, int quantity) {

        String stockKey = "seckill:stock:" + productId;
        String limitKey = "seckill:user_limit:" + productId;

        List<String> keys = Arrays.asList(stockKey, limitKey);

        // 执行Lua脚本（原子操作）
        Long result = redisTemplate.execute(
                seckillScript,
                keys,
                String.valueOf(userId),
                "1", // 最大购买限制
                String.valueOf(quantity)
        );

        if (result == null) {
            return SeckillResult.SYSTEM_ERROR;
        }

        switch (result.intValue()) {
            case 1:
                return SeckillResult.SUCCESS;
            case 0:
                return SeckillResult.STOCK_NOT_ENOUGH;
            case -1:
                return SeckillResult.LIMIT_EXCEEDED;
            default:
                return SeckillResult.SYSTEM_ERROR;
        }
    }

    public enum SeckillResult {
        SUCCESS,             // 秒杀成功
        STOCK_NOT_ENOUGH,     // 库存不足
        LIMIT_EXCEEDED,       // 超过购买限制
        SYSTEM_ERROR          // 系统错误
    }
}
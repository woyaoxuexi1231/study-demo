package org.hulei.demo.cj.seckill;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataOrders;
import org.hulei.entity.mybatisplus.domain.BigDataProducts;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataProductMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeckillService {

    private final SeckillOrderProcessor seckillOrderProcessor;
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> seckillScript = new DefaultRedisScript<>();
    private final BigDataProductMapper bigDataProductMapper;
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        seckillScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("seckill/seckill.lua")));
        seckillScript.setResultType(Long.class);
    }

    @Transactional(rollbackFor = Exception.class)
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

        log.info("执行结果: {}", result);

        if (result == null) {
            return SeckillResult.SYSTEM_ERROR;
        }

        switch (result.intValue()) {
            case 1:
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setUserId(userId);
                seckillOrder.setProductId(productId);
                seckillOrder.setQuantity(quantity);
                rabbitTemplate.convertAndSend(
                        "normal-topic-exchange",
                        "seckill.order.key",
                        JSON.toJSONBytes(seckillOrder),
                        msg -> msg,
                        null
                );
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
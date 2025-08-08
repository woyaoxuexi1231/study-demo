package org.hulei.demo.cj.seckill;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SeckillOrderProcessor {

    final RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = "seckill.order.queue"),
                    exchange = @Exchange(name = "normal-topic-exchange", type = "topic"),
                    key = "seckill.order.key"
            )
    })
    public void processOrder(SeckillOrder order) {
        try {
            // 1. 检查订单有效性（幂等性检查）
            // 2. 生成订单号
            // 3. 写入数据库
            // 4. 发送成功通知
        } catch (Exception e) {
            // 订单创建失败，回补库存
            replenishStock(order.getProductId(), order.getUserId(), order.getQuantity());
        }
    }

    private void replenishStock(long productId, long userId, int quantity) {
        String stockKey = "seckill:stock:" + productId;
        String limitKey = "seckill:user_limit:" + productId;

        redisTemplate.opsForValue().increment(stockKey, quantity);
        redisTemplate.opsForHash().increment(limitKey, String.valueOf(userId), -quantity);
    }
}
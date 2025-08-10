package org.hulei.demo.cj.seckill;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataOrderItems;
import org.hulei.entity.mybatisplus.domain.BigDataOrders;
import org.hulei.entity.mybatisplus.domain.BigDataProducts;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataOrderItemMapper;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataOrderMapper;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataProductMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class SeckillOrderProcessor {

    final RedisTemplate<String, Object> redisTemplate;

    final BigDataOrderMapper bigDataOrderMapper;

    final BigDataOrderItemMapper bigDataOrderItemMapper;

    final BigDataProductMapper bigDataProductMapper;

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = "seckill.order.queue"),
                    exchange = @Exchange(name = "normal-topic-exchange", type = "topic"),
                    key = "seckill.order.key"
            )
    })
    public void processOrder(Message message, Channel channel) throws Exception {

        byte[] body = message.getBody();
        String s = new String(body, StandardCharsets.UTF_8);
        SeckillOrder seckillOrder = JSON.parseObject(s, SeckillOrder.class);

        try {
            // 1. 检查订单有效性（幂等性检查）
            // 2. 生成订单号
            // 3. 写入数据库
            // 4. 发送成功通知
            BigDataProducts products = bigDataProductMapper.selectById(seckillOrder.getProductId());

            BigDataOrders orders = new BigDataOrders();
            orders.setUserId(seckillOrder.getUserId());
            orders.setStatus("completed");
            orders.setTotalAmount(products.getPrice());
            int insert = bigDataOrderMapper.insert(orders);

            BigDataOrderItems orderItems = new BigDataOrderItems();
            orderItems.setOrderId(orders.getId());
            orderItems.setPrice(products.getPrice());
            orderItems.setProductId(seckillOrder.getProductId());
            orderItems.setQuantity(seckillOrder.getQuantity());
            int item = bigDataOrderItemMapper.insert(orderItems);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);


            log.info("订单创建完成！");

        } catch (Exception e) {
            log.info("订单创建失败！", e);
            // 订单创建失败，回补库存
            replenishStock(seckillOrder.getProductId(), seckillOrder.getUserId(), seckillOrder.getQuantity());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    private void replenishStock(long productId, long userId, int quantity) {
        String stockKey = "seckill:stock:" + productId;
        String limitKey = "seckill:user_limit:" + productId;

        redisTemplate.opsForValue().increment(stockKey, quantity);
        redisTemplate.opsForHash().increment(limitKey, String.valueOf(userId), -quantity);
    }
}
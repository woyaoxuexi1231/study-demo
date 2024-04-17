package org.hulei.springboot.rabbitmq;

import com.hundsun.demo.spring.mq.rabbit.config.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.mapper.RabbitmqLogMapper;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.listener
 * @className: RabbiMQListener
 * @description:
 * @author: h1123
 * @createDate: 2023/3/11 0:28
 */

@Component
@Slf4j
public class RabbiMQListener {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RabbitmqLogMapper rabbitmqLogMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_MASTER_QUEUE,
                    durable = "true",
                    autoDelete = "false",
                    exclusive = "false",
                    arguments = {@Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)}
            ),
            exchange = @Exchange(
                    value = MQConfig.TOPIC_EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    autoDelete = "false",
                    durable = "true"),
            key = MQConfig.TOPIC_MASTER_ROUTE_KEY
    ))
    public void receiveMsg(Message msg, Channel channel) {
        log.info("收到消息, msg: {}", msg);
        try {
            // 在确认后出现了这个异常
            // Shutdown Signal: channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - unknown delivery tag 1, class-id=60, method-id=80)
            // 原因是因为没有配置spring.rabbitmq.listener.simple.acknowledge-mode=manual,所以spring默认自动应答了,应答了两次所以报错了
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }
}

package org.hulei.springboot.rabbitmq.spring.exchange;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author h1123
 * @since 2023/3/11 0:28
 */

@Configuration
@Component
@Slf4j
public class TopicExchangeListener {


    @RabbitListener(queues = MQConfig.TOPIC_MASTER_QUEUE)
    public void receiveMasterMsg(Message msg, Channel channel) {
        log.info("交换机：{}，路由键：{}，队列：{}，消息：{}",
                msg.getMessageProperties().getReceivedExchange(),
                msg.getMessageProperties().getReceivedRoutingKey(),
                msg.getMessageProperties().getConsumerQueue(),
                new String(msg.getBody(), StandardCharsets.UTF_8)
        );
        try {
            // Thread.sleep(5000);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

            if (StringUtils.hasLength(msg.getMessageProperties().getReplyTo())) {
                channel.basicPublish(
                        "",
                        msg.getMessageProperties().getReplyTo(),
                        new AMQP.BasicProperties().builder().correlationId(msg.getMessageProperties().getCorrelationId()).build(),
                        ("org.hulei.springboot.rabbitmq.spring.exchange.TopicExchangeListener.receiveMasterMsg 收到消息了").getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

}

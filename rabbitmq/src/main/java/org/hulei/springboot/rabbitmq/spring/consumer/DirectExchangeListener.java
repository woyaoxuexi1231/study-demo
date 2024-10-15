package org.hulei.springboot.rabbitmq.spring.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2024/9/23 16:37
 */

@Slf4j
@Component
public class DirectExchangeListener {

    /**
     * 测试 direct 类型的交换机
     * direct交换机的消息仅仅只能通过完全匹配路由键才能够发送到指定的队列
     *
     * @param msg     msg
     * @param channel channel
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = "direct-test",
                            durable = "true",
                            autoDelete = "false",
                            exclusive = "false"
                    ),
                    exchange = @Exchange(
                            value = MQConfig.DIRECT_EXCHANGE_NAME
                    ),
                    key = MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY
            )
    )
    public void directQueue(Message msg, Channel channel) {
        log.info("direct-test 收到消息: {}", msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }
}

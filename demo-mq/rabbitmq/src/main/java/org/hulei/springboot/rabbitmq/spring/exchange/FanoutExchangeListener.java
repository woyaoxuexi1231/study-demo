package org.hulei.springboot.rabbitmq.spring.exchange;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.hulei.springboot.rabbitmq.spring.utils.LogUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 对于此类型的交换机,设置的routingkey不会生效,绑定到交换机的所有队列都会收到消息
 *
 * @author hulei
 * @since 2024/9/23 16:39
 */

@Slf4j
@Component
public class FanoutExchangeListener {

    @RabbitListener(queues = MQConfig.FANOUT_MASTER_QUEUE)
    public void masterQueue(Message msg, Channel channel) {
        try {
            LogUtil.log(msg);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    @RabbitListener(queues = MQConfig.FANOUT_SLAVE_QUEUE)
    public void salveQueue(Message msg, Channel channel) {
        try {
            LogUtil.log(msg);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

}

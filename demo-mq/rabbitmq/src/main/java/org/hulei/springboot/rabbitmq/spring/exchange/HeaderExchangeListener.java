package org.hulei.springboot.rabbitmq.spring.exchange;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.hulei.springboot.rabbitmq.spring.utils.LogUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2025/8/22 14:42
 */

@Slf4j
@Component
public class HeaderExchangeListener {

    @RabbitListener(queues = MQConfig.HEADER_MASTER_QUEUE)
    public void receive(String string, Message message, Channel channel) {
        try {
            LogUtil.log(message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("{}队列接收消息出现异常!", MQConfig.HEADER_SLAVE_QUEUE, e);
        }
    }
}

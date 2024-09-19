package org.hulei.springboot.rabbitmq.basic;

import org.hulei.springboot.rabbitmq.basic.config.ConnectFactory;
import com.hundsun.demo.commom.core.consts.MQConfig;
import org.hulei.springboot.rabbitmq.basic.work.MsgPushConsumerA;
import org.hulei.springboot.rabbitmq.basic.work.MsgPushConsumerB;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: RabbitMQTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/26 18:57
 */

@Slf4j
public class ConsumerStart {

    @SneakyThrows
    public static void main(String[] args) {
        log.info("创建队列");
        ConnectFactory.consumerInit();
        log.info("准备接收消息... ");
        new Thread(new MsgPushConsumerA(ConnectFactory.getConnect(), MQConfig.TOPIC_MASTER_QUEUE)).start();
        log.info("消费A线程已启动");
        new Thread(new MsgPushConsumerB(ConnectFactory.getConnect(), MQConfig.TOPIC_SLAVE_QUEUE)).start();
        log.info("消费B线程已启动");
    }

}

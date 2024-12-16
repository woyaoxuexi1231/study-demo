package org.hulei.springboot.rabbitmq.basic.consumer;

import org.hulei.springboot.rabbitmq.basic.config.ConnectFactory;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
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
        new Thread(new TopicMasterClient(ConnectFactory.getConnect())).start();
        new Thread(new TopicDeadClient(ConnectFactory.getConnect())).start();
        // new Thread(new PullModeClient(ConnectFactory.getConnect())).start();
    }
}

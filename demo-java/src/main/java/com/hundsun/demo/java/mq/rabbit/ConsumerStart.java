package com.hundsun.demo.java.mq.rabbit;

import com.hundsun.demo.java.mq.rabbit.config.ConnectFactory;
import com.hundsun.demo.java.mq.rabbit.config.MQConfig;
import com.hundsun.demo.java.mq.rabbit.work.MsgPullConsumer;
import com.hundsun.demo.java.mq.rabbit.work.MsgPushConsumerA;
import com.hundsun.demo.java.mq.rabbit.work.MsgPushConsumerB;
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
        log.info("准备接收消息... ");
        new Thread(new MsgPushConsumerA(ConnectFactory.getConnect(), MQConfig.TOPIC_MASTER_QUEUE)).start();
        log.info("消费A线程已启动");
        new Thread(new MsgPushConsumerB(ConnectFactory.getConnect(), MQConfig.TOPIC_SLAVE_QUEUE)).start();
        log.info("消费B线程已启动");
    }

}

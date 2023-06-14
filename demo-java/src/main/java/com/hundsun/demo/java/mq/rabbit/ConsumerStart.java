package com.hundsun.demo.java.mq.rabbit;

import com.hundsun.demo.java.mq.rabbit.config.ConnectFactory;
import com.hundsun.demo.java.mq.rabbit.config.MQConfig;
import com.hundsun.demo.java.mq.rabbit.work.MsgConsumerA;
import com.hundsun.demo.java.mq.rabbit.work.MsgConsumerB;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        log.info("准备接收消息... ");
        new MsgConsumerA(ConnectFactory.getConnect(), MQConfig.TOPIC_MASTER_QUEUE).start();
        new MsgConsumerB(ConnectFactory.getConnect(), MQConfig.TOPIC_SLAVE_QUEUE).start();
    }

}

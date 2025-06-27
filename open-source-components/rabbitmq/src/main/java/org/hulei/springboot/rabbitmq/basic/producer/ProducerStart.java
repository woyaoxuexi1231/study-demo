package org.hulei.springboot.rabbitmq.basic.producer;

import org.hulei.springboot.rabbitmq.basic.config.ConnectFactory;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: ProducerStart
 * @description:
 * @author: h1123
 * @createDate: 2023/3/1 21:07
 */
@Slf4j
public class ProducerStart {

    public static void main(String[] args) {

        // 作为消息生产者,需要先创建好交换机,以及定义好消息的路由规则,队列不用管
        ConnectFactory.initExchange();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            if ("exit".equalsIgnoreCase(msg)) {
                break;
            }
            // 封装了一个专门发消息的工具类来发消息
            MsgProducer.postMsg(MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_MASTER_ROUTE_KEY, msg);
            // MsgProducer.postMsg(MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_FOR_DEAD_ROUTING_KEY, msg);
            // MsgProducer.postMsg(exchange, "direct.key.test", msg);
        }
    }
}

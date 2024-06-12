package com.hundsun.demo.spring.mq.rabbit;

import com.hundsun.demo.commom.core.consts.MQConfig;
import com.hundsun.demo.spring.mq.rabbit.work.MsgProducer;
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
        while (true) {
            Scanner scanner = new Scanner(System.in);
            // System.out.print("交换机类型: ");
            String exchange = MQConfig.getExchangeName("topic");
            // String exchange = "amq.fanout";
            // System.out.print("消息: ");
            String msg = scanner.nextLine();
            // 封装了一个专门发消息的工具类来发消息
            MsgProducer.postMsg(exchange, MQConfig.TOPIC_MASTER_ROUTE_KEY, msg);
            // MsgProducer.postMsg(exchange, MQConfig.TOPIC_SLAVE_ROUTE_KEY, msg);
            // MsgProducer.postMsg(exchange, "direct.key.test", msg);
        }
    }
}

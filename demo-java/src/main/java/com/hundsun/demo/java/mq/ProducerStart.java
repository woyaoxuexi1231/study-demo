package com.hundsun.demo.java.mq;

import com.hundsun.demo.java.mq.config.ConnectFactory;
import com.hundsun.demo.java.mq.config.MQConfig;
import com.hundsun.demo.java.mq.work.MsgProducer;
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
            System.out.print("交换机类型: ");
            String exchange = MQConfig.getExchangeName(scanner.nextLine());
            System.out.print("路由键: ");
            String routingKey = scanner.nextLine();
            System.out.print("消息: ");
            String msg = scanner.nextLine();
            MsgProducer.postMsg(exchange, routingKey, msg);
        }
    }
}

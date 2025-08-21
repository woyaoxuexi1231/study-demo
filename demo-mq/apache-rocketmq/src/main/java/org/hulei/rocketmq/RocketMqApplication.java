package org.hulei.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/8/21 20:17
 */

@RestController
@SpringBootApplication
public class RocketMqApplication {

    public static void main(String[] args) {
        /*
        Rocket MQ 体系架构：
        1. console 管理平台：管理 RocketMQ 生产者组、Topic、消费者组和 RocketMQ 元数据得平台。
        2. Namesrv 集群：一个无状态的元数据管理，namesrv之于RocketMQ等价于zookeeper之于Kafka
        3. Broker 集群：消息中间商和消息代理。主要用于保存消息，处理生产者、消费者的各种请求的代理。包含 master 和 slave 两种角色，与mysql中的主从类似。
        4. 生产者集群：消息发送方，通常由一个或多个生产者实例组成。
        5. 消费者集群：消息接收方，通常由一个或多个消费者实例组成。
         */
        SpringApplication.run(RocketMqApplication.class, args);
    }
}

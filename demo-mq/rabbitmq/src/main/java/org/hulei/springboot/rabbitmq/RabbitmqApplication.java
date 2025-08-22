package org.hulei.springboot.rabbitmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/9/19 19:13
 */

@SpringBootApplication
public class RabbitmqApplication {

    public static void main(String[] args) {
        /*
        RabbitMQ 使用 AMQP 协议（Advanced Message Queuing Protocol，高级消息队列协议）
        AMQP工作过程：
            1.发布者（Publisher）发布消息（Message），经由交换机（Exchange）。
            2.交换机根据路由规则将收到的消息分发给与该交换机绑定的队列（Queue）。
            3.最后 AMQP 代理会将消息投递给订阅了此队列的消费者，或者消费者按照需求自行获取。
        rabbitmq 包含 生产者、消费者、代理(rabbitmq本身)

        组件：ConnectionFactory, Channel, Exchange, Queue, Binding, Routing Key


         */
        SpringApplication.run(RabbitmqApplication.class, args);
    }
}

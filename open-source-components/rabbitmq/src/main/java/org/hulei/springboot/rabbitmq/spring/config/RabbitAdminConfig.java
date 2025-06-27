package org.hulei.springboot.rabbitmq.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * @author hulei
 * @since 2024/9/23 21:06
 */

@Slf4j
@Configuration
public class RabbitAdminConfig {

    /**
     * rabbitAdmin
     * 1. 声明交换机
     * 2. 声明队列
     * 3. 声明绑定关系
     */
    @Autowired
    RabbitAdmin rabbitAdmin;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @PostConstruct
    public void init() {
        exchangeDeclare();
        queueDeclare();
        bindingDeclare();
    }


    private void exchangeDeclare() {
        // 声明交换机
        TopicExchange exchange = new TopicExchange(
                MQConfig.TOPIC_EXCHANGE_NAME,   // topic类型的交换机名字
                true,  // 交换机是否持久化
                false, // 交换机是否删除
                null // 交换机的其他额外参数
        );
        rabbitAdmin.declareExchange(exchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", exchange);

        DirectExchange directExchange = new DirectExchange(
                MQConfig.DIRECT_EXCHANGE_NAME,  // direct 类型的交换机名字
                true, // 交换机是否持久化
                false, // 交换机是否删除
                null // 交换机的其他额外参数
        );
        rabbitAdmin.declareExchange(directExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", directExchange);
    }

    private void queueDeclare() {
        /*
        队列的 arguments 参数
        1. **x-message-ttl**: 设置队列中消息的生存时间（TTL，即time-to-live）。如果一条消息在队列中存在的时间超过了这个时间，它将被删除或者发送到死信队列。
        2. **x-expires**: 设置队列的自动过期时间。如果队列在指定时间内未被使用（没有任何消费者监听，没有调用 `basic.get`，或者没有新消息进入），它将自动被删除。
        3. **x-max-length**: 设置队列可以容纳的最大消息数。一旦达到这个数量，队列会开始丢弃（或死信）旧消息，以便为新消息腾出空间。
        4. **x-max-length-bytes**: 设置队列可以容纳消息的总体积的最大字节数。一旦达到此限制，队列会开始移除（或死信）旧消息。
        5. **x-dead-letter-exchange**: 指定死信交换机，队列中变成死信的消息将被路由到这个交换机。
        6. **x-dead-letter-routing-key**: 声明队列的死信消息被路由到死信交换机时使用的路由键。
        7. **x-max-priority**: 为队列中的消息设置优先级;队列将优先传递具有较高优先级的消息。
        8. **x-queue-mode**: 设置队列的模式，如"lazy"，表示队列将尽量将消息保存到磁盘上，减少内存使用。
        9. **x-queue-master-locator**: 用于指定 HA 队列中的 master 位置策略，例如 "min-masters"。
        10. **x-ha-policy**: 为队列设置高可用性策略。这通常指定队列应当在多个服务器节点间被镜像。
         */
        Queue queue = new Queue(
                "rabbit_admin_queue",
                true, // 是否持久化
                false, // 是否排他，只能允许创建者消费，也意味着如果配置了自动删除，那么在创建者断开连接后队列将自动删除
                false, // 队列是否在程序停止后自动删除（删除时机是当队列最后一个消费者断开连接时，队列会自动删除）
                null // arguments 队列的其他参数
        );
        rabbitAdmin.declareQueue(queue);
        log.info("使用 RabbitAdmin 创建队列 {} 成功", queue);

        Queue directQueue = new Queue(
                "direct-test",
                true,
                false,
                false,
                null
        );
        rabbitAdmin.declareQueue(directQueue);
        log.info("使用 RabbitAdmin 创建队列 {} 成功", directQueue);
    }

    private void bindingDeclare() {
        // 绑定队列和交换机的关系
        Binding binding = new Binding(
                "rabbit_admin_queue",
                Binding.DestinationType.QUEUE,
                MQConfig.TOPIC_EXCHANGE_NAME,
                "rabbit.admin.route.*",
                null
        );
        rabbitAdmin.declareBinding(binding);
        log.info("使用 RabbitAdmin 创建绑定关系 {} 成功", binding);

        // 还可以绑定交换机和交换机的关系
        Binding exchangeBinding = new Binding(
                MQConfig.DIRECT_EXCHANGE_NAME,
                Binding.DestinationType.EXCHANGE,
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY,
                null
        );
        rabbitAdmin.declareBinding(exchangeBinding);
        log.info("使用 RabbitAdmin 创建绑定关系 {} 成功! ", exchangeBinding);

        // 这里引入一个注意点：这个绑定关系的操作必须要建立在队列已经存在的前提下进行，否则会直接报错 404 找不到队列
        Binding directQueue = new Binding(
                "direct-test",
                Binding.DestinationType.QUEUE,
                MQConfig.DIRECT_EXCHANGE_NAME,
                MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY,
                null
        );
        rabbitAdmin.declareBinding(directQueue);
        log.info("使用 RabbitAdmin 创建绑定关系 {} 成功", directQueue);
    }

}

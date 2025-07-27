package org.hulei.springboot.rabbitmq.spring.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author hulei
 * @since 2025/7/27 20:09
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeclareConfig {

    private final RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void init() {
        exchangeDeclare();
        queueDeclare();
        bindingDeclare();

        deadLetterDeclare();

        otherDeclare();
    }

    private void otherDeclare() {

        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 5);
        rabbitAdmin.declareQueue(new Queue(
                "max-5-queue",
                true,
                false,
                false,
                args
        ));
        rabbitAdmin.declareBinding(new Binding(
                "max-5-queue",
                Binding.DestinationType.QUEUE,
                MQConfig.NORMAL_TOPIC_EXCHANGE,
                "max.5.key",
                null
        ));
    }


    private void exchangeDeclare() {
        // 声明交换机
        TopicExchange exchange = new TopicExchange(
                MQConfig.TOPIC_EXCHANGE_NAME,   // topic类型的交换机名字
                true,  // 交换机是否持久化
                false, // 交换机是否删除
                null // 交换机的其他额外参数
        );
        DirectExchange directExchange = new DirectExchange(MQConfig.DIRECT_EXCHANGE_NAME, true, false, null);
        FanoutExchange fanoutExchange = new FanoutExchange(MQConfig.FANOUT_EXCHANGE_NAME, true, false, null);
        HeadersExchange headersExchange = new HeadersExchange(MQConfig.HEADER_EXCHANGE_NAME, true, false, null);
        TopicExchange deadExchange = new TopicExchange(MQConfig.DEAD_EXCHANGE_NAME, true, false, null);

        rabbitAdmin.declareExchange(exchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", exchange);

        rabbitAdmin.declareExchange(directExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", directExchange);

        rabbitAdmin.declareExchange(fanoutExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", directExchange);

        rabbitAdmin.declareExchange(headersExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", directExchange);

        rabbitAdmin.declareExchange(deadExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", deadExchange);
    }

    private void queueDeclare() {

        List<Queue> queues = new ArrayList<>();

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
        queues.add(new Queue(
                MQConfig.TOPIC_MASTER_QUEUE,
                true, // 是否持久化
                false, // 是否排他，只能允许创建者消费，也意味着如果配置了自动删除，那么在创建者断开连接后队列将自动删除
                false, // 队列是否在程序停止后自动删除（删除时机是当队列最后一个消费者断开连接时，队列会自动删除）
                null // arguments 队列的其他参数
        ));

        queues.add(new Queue(MQConfig.DIRECT_MASTER_QUEUE, true, false, false, null));
        queues.add(new Queue(MQConfig.DIRECT_SLAVE_QUEUE, true, false, false, null));

        queues.add(new Queue(MQConfig.FANOUT_MASTER_QUEUE, true, false, false, null));
        queues.add(new Queue(MQConfig.FANOUT_SLAVE_QUEUE, true, false, false, null));

        queues.add(new Queue(MQConfig.HEADER_MASTER_QUEUE, true, false, false, null));
        queues.add(new Queue(MQConfig.HEADER_SLAVE_QUEUE, true, false, false, null));

        queues.add(new Queue(MQConfig.DEAD_QUEUE_NAME, true, false, false, null));

        queues.forEach((queue) -> {
            rabbitAdmin.declareQueue(queue);
            log.info("使用 RabbitAdmin 创建队列 {} 成功", queue);
        });
    }

    private void bindingDeclare() {

        List<Binding> bindings = new ArrayList<>();

        // 绑定队列和交换机的关系
        bindings.add(new Binding(
                MQConfig.DIRECT_MASTER_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.DIRECT_EXCHANGE_NAME,
                MQConfig.DIRECT_MASTER_ROUTE_KEY,
                null
        ));
        bindings.add(new Binding(
                MQConfig.DIRECT_SLAVE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.DIRECT_EXCHANGE_NAME,
                MQConfig.DIRECT_SLAVE_ROUTE_KEY,
                null
        ));


        bindings.add(new Binding(
                MQConfig.FANOUT_MASTER_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.FANOUT_EXCHANGE_NAME,
                MQConfig.FANOUT_MASTER_ROUTE_KEY,
                null
        ));
        bindings.add(new Binding(
                MQConfig.FANOUT_SLAVE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.FANOUT_EXCHANGE_NAME,
                MQConfig.FANOUT_SLAVE_ROUTE_KEY,
                null
        ));


        bindings.add(new Binding(
                MQConfig.TOPIC_MASTER_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_MASTER_ROUTE_KEY,
                null
        ));


        bindings.add(new Binding(
                MQConfig.HEADER_MASTER_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.HEADER_EXCHANGE_NAME,
                MQConfig.HEADER_MASTER_ROUTE_KEY,
                null
        ));

        bindings.add(new Binding(
                MQConfig.HEADER_SLAVE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.HEADER_EXCHANGE_NAME,
                MQConfig.HEADER_SLAVE_ROUTE_KEY,
                null
        ));

        bindings.forEach(binding -> {
            rabbitAdmin.declareBinding(binding);
            log.info("使用 RabbitAdmin 创建绑定关系 {} 成功", binding);
        });
    }

    public void deadLetterDeclare() {
        // 声明一个正常的交换机
        rabbitAdmin.declareExchange(new TopicExchange(MQConfig.NORMAL_TOPIC_EXCHANGE, true, false));
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机, 这里设置的是, 如果这个队列内的消息出现问题, 消息应该往哪个死信队列发
        args.put("x-dead-letter-exchange", MQConfig.DEAD_EXCHANGE_NAME);
        // 设置死信 RoutingKey, 配置这个的作用时, 当消息出现问题时, 发往死信队列时应该以什么路由key去发送
        args.put("x-dead-letter-routing-key", MQConfig.NORMAL_TOPIC_QUEUE_DEAD_LETTER_ROUTING_KEY);
        args.put("x-max-length", 6);
        rabbitAdmin.declareQueue(new Queue(MQConfig.NORMAL_TOPIC_QUEUE, true, false, false, args));
        rabbitAdmin.declareBinding(new Binding(MQConfig.NORMAL_TOPIC_QUEUE, Binding.DestinationType.QUEUE, MQConfig.NORMAL_TOPIC_EXCHANGE, MQConfig.NORMAL_TOPIC_ROUTING_KEY, null));


        // 声明一个死信交换机
        rabbitAdmin.declareExchange(new TopicExchange(MQConfig.DEAD_EXCHANGE_NAME, true, false));
        rabbitAdmin.declareQueue(new Queue(MQConfig.DEAD_QUEUE_NAME, true, false, false, null));
        rabbitAdmin.declareBinding(new Binding(MQConfig.DEAD_QUEUE_NAME, Binding.DestinationType.QUEUE, MQConfig.DEAD_EXCHANGE_NAME, MQConfig.DEAD_QUEUE_ROUTING_KEY, null));
    }
}

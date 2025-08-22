package org.hulei.springboot.rabbitmq.spring.config;

import io.lettuce.core.resource.Delay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
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

        declareDelayQueue();
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


        /*
        基于 routing key 进行精确匹配。只有当消息的 routing key 与队列的 binding key 完全相等时，消息才会被路由到该队列。
        点对点/简单发布订阅： 非常适合将消息精确地路由到一个或多个特定的队列（例如，不同日志级别的处理）。
         */
        DirectExchange directExchange = new DirectExchange(MQConfig.DIRECT_EXCHANGE_NAME, true, false, null);

        /*
        广播模式。将收到的每一条消息无差别地广播给所有绑定到该交换机上的队列。
        💡完全忽略 routing key，routing key 对于此类型交换机完全无用

        - 极致简单和高效： 路由逻辑最简单（直接广播），性能是所有交换机中最高的。
        - 发布/订阅： 完美实现发布/订阅模式，确保所有订阅者（队列）都能收到相同的消息。
         */
        FanoutExchange fanoutExchange = new FanoutExchange(MQConfig.FANOUT_EXCHANGE_NAME, true, false, null);

        /*
        基于 routing key 的模式匹配。
        routing key 通常是由点号 . 分隔的单词组成的字符串（例如 stock.usd.nyse, nyse.quotes）。
        绑定时使用的 binding key 可以包含通配符
            * (星号)：匹配一个单词。
            # (井号)：匹配零个或多个单词。

        - 结合通配符，可以轻松实现基于主题（topic）的订阅，订阅者可以选择性地接收符合特定模式的消息。
        - 模式匹配比精确匹配复杂，性能低于 direct 和 fanout 交换机。
         */
        TopicExchange exchange = new TopicExchange(
                MQConfig.TOPIC_EXCHANGE_NAME,   // topic类型的交换机名字
                true,  // 交换机是否持久化
                false, // 交换机是否删除
                null // 交换机的其他额外参数
        );

        /*
        基于消息的 headers 属性（一个 key-value 字典）进行匹配。
        💡完全忽略 routing key。

        绑定时需要指定一组 header 键值对作为 binding arguments，并设置 x-match 参数：
            x-match=all：消息的 headers 必须包含并匹配所有指定的键值对。这是默认的
            x-match=any：消息的 headers 只需匹配任意一个指定的键值对。
         */
        HeadersExchange headersExchange = new HeadersExchange(MQConfig.HEADER_EXCHANGE_NAME, true, false, null);


        TopicExchange deadExchange = new TopicExchange(MQConfig.DEAD_EXCHANGE_NAME, true, false, null);

        rabbitAdmin.declareExchange(exchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", exchange);

        rabbitAdmin.declareExchange(directExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", directExchange);

        rabbitAdmin.declareExchange(fanoutExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", fanoutExchange);

        rabbitAdmin.declareExchange(headersExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", headersExchange);

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
                /*
                是否持久化，队列的持久化和交换机的持久化是互不影响的，他们都是独立的，即使交换机不是持久化的，队列也可以是持久化的
                如果交换机非持久化，mq重启后交换机将消失，但不影响队列，队列仍可正常使用，但是无法再接收新的消息，绑定的关系也随着交换机消失了
                 */
                true,
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

        /*
        绑定的一些问题

        🚨队列的多重绑定，在一条消息同时匹配同一个队列的多个绑定键时，rabbitmq如何处理？
        💡RabbitMQ 会确保同一条消息在同一个队列中只出现一次，即使它匹配了该队列的多个绑定条件。
         */

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
                "",
                null
        ));
        bindings.add(new Binding(
                MQConfig.FANOUT_SLAVE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.FANOUT_EXCHANGE_NAME,
                "",
                null
        ));


        bindings.add(new Binding(
                MQConfig.TOPIC_MASTER_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_MASTER_ROUTE_KEY,
                null
        ));

        /*
        header 类型的交换机绑定队列
        其实这里的 routingKey 已经失去意义
        header 类型的交换机不再通过 routingKey 传递消息，而仅仅通过配置的 header 参数进行传递消息
         */
        Map<String, Object> headers = new HashMap<>();
        headers.put("color", "red");
        bindings.add(new Binding(
                MQConfig.HEADER_MASTER_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.HEADER_EXCHANGE_NAME,
                "",
                headers
        ));

        bindings.add(new Binding(
                MQConfig.HEADER_SLAVE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQConfig.HEADER_EXCHANGE_NAME,
                "",
                null
        ));

        bindings.forEach(binding -> {
            rabbitAdmin.declareBinding(binding);
            log.info("使用 RabbitAdmin 创建绑定关系 {} 成功", binding);
        });
    }

    public void deadLetterDeclare() {

        /*
        死信队列（Dead Letter Queue，简称 DLQ） 是 RabbitMQ 中用于接收 “无法被正常消费的消息” 的一种特殊队列。
        利用 队列TTL + 死信队列 也是实现延迟队列的方式之一

        以下情况消息会被发送死信交换机：
            1. 息被消费者拒绝（Reject 或 Nack[也是拒绝消息，可以多条]），并且 requeue=false
            2. 消息在队列中超过了存活时间（TTL）
            3. 队列达到最大长度限制（队列满了）
         */

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

        log.info("死信队列相关的配置已经成功初始化");
    }

    public void declareDelayQueue() {
        /*
        x-delayed-message，让消息可以携带一个 延迟时间（delay）
        1. 该插件实际上是基于 Erlang 的 定时器机制 + 内存/ETS 表 实现的延迟调度。
        2. 当带有 x-delay的消息到达 Exchange 时，插件不会立即路由，而是将消息暂存到内部存储（如 ETS 表）中，并记录投递时间。
        3. 插件有一个 后台调度进程，不断检查哪些消息已经到了投递时间，然后将其真正地投递到绑定的队列中。
         */

        // 声明一个延迟队列交换机，启动 rabbitmq_delayed_message_exchange 插件后，rabbitmq 会有一个专门的交换机
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        CustomExchange delayExchange = new CustomExchange("delay_exchange", "x-delayed-message", true, false, args);
        rabbitAdmin.declareExchange(delayExchange);

        // 声明队列和绑定信息
        rabbitAdmin.declareQueue(new Queue("delay_queue"));
        rabbitAdmin.declareBinding(new Binding(
                "delay_queue",
                Binding.DestinationType.QUEUE,
                "delay_exchange",
                "delay.key",
                null));

        log.info("延迟队列相关的配置已经成功初始化");
    }
}

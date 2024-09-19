package org.hulei.springboot.rabbitmq.spring.consumer;

import com.hundsun.demo.commom.core.consts.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author h1123
 * @since 2023/3/11 0:28
 */

@Configuration
@Component
@Slf4j
public class RabbiMQListener {

    /*============================================== 配置 ===========================================*/

    /**
     * queues参数, 如果队列不存在, 启动将会报错
     * author: hulei42031
     * date: 2024-03-12 16:01
     *
     * @param msg     msg
     * @param channel channel
     */
    @SneakyThrows
    // @RabbitListener(queues = "topic-queue-master")
    public void queuesParam(Message msg, Channel channel) {
        log.info("queuesParam: {}", msg);
        // 即使自动提交, ack也会在业务方法执行完之后再ack
        Thread.sleep(5 * 1000);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);  // 设置并发消费者数量
        factory.setMaxConcurrentConsumers(10); // 设置最大并发消费者数量
        return factory;
    }

    /**
     * 如果你没有配置自定义的 SimpleRabbitListenerContainerFactory，Spring AMQP 会使用默认的 RabbitListenerContainerFactory 实现(SimpleRabbitListenerContainerFactory 的一个实例)
     * 默认的工厂配置旨在提供一套合理的默认行为，适用于大多数简单场景。
     * RabbitAnnotationDrivenConfiguration会默认配置一个 SimpleRabbitListenerContainerFactory
     * <p>
     * 指定了containerFactory之后, 会使用指定的containerFactory来注册这个监听器, 不会再使用springboot默认的监听器容器来注册了
     * <p>
     * author: hulei42031
     * date: 2024-03-12 18:09
     *
     * @param message message
     * @param channel channel
     */
    // @RabbitListener(queues = "topic-queue-master", containerFactory = "myContainerFactory")
    public void handleMessage(String message, Channel channel) {
        System.out.println("Received message: " + message);
        // 这里是否ack也会右具体的消息监听容器来决定了
        // channel.basicAck();
    }

    /*
    // 设置消费者每次只拉取一条消息
    @RabbitListener(queues = "myQueue", containerFactory = "myContainerFactory")
    public void handleMessage(String message) {
        // 处理消息逻辑
    }
    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setPrefetchCount(1); // 设置为 1，表示一次只接收一条未确认的消息
        return factory;
    }
    对于队列阻塞, 只是相对于单个消费者而言, 多个消费者之间并不会互相影响
    如果消费者 A 阻塞了，它不会直接导致消费者 B 无法接收到消息。当使用多个消费者共享同一个队列时，每个消费者都有机会获取队列中的消息进行处理。
    具体来说，当有消息到达队列时，RabbitMQ 会按照一定的策略（如轮询或者优先级）将消息发送给可用的消费者。如果消费者 A 阻塞了，即无法及时消费消息，RabbitMQ 会将该消息发送给其他可用的消费者，如消费者 B。
    但是，需要注意的是，如果消费者 A 阻塞的时间过长或者一直处于阻塞状态，队列中的消息可能会积压，导致整体处理速度变慢。如果积压的消息过多，可能会影响到其他消费者的消息处理速度，从而间接地影响到消费者 B 的消息接收。
    因此，为了避免消息积压和消费者之间的影响，需要综合考虑以下几点：
    1. 消费者的处理速度：确保消费者能够及时处理队列中的消息，避免长时间的阻塞。
    2. 队列长度和缓冲策略：设置合适的队列长度，并根据实际情况选择适当的缓冲策略，如溢出策略或丢弃策略，以防止队列中消息过多导致的性能问题。
    3. 并发控制：根据实际需求，合理配置消费者数量和并发性，避免过度消费或阻塞。
    通过上述措施，可以在一定程度上避免因为某个消费者的阻塞而导致其他消费者无法接收到消息的情况。
     */


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = MQConfig.TOPIC_MASTER_QUEUE, // 队列的名称。如果留空，将创建一个具有随机名称的队列，通常用于声明临时队列。
                            durable = "true", // 表示队列是否持久化。持久化的队列会在 RabbitMQ 重启后依然存在，默认值是 false。
                            autoDelete = "false", // 表示队列是否自动删除。当最后一个消费者断开连接之后队列是否自动删除，默认值是 false。
                            exclusive = "false", // 表示队列是否是排他的。排他队列只对首次声明它的连接可见，并在连接关闭时自动删除，默认值是 false。
                            arguments = {
                                    @Argument(
                                            name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME // 指定死信交换机，队列中变成死信的消息将被路由到这个交换机。
                                    )
                            }
                    ),
                    exchange = @Exchange(value = MQConfig.TOPIC_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = MQConfig.TOPIC_MASTER_ROUTE_KEY // 用于定义绑定键（routing key），这是一个字符串，决定了消息如何路由到队列。
            ))
    public void receiveMasterMsg(Message msg, Channel channel) {
        log.info("{} 收到消息: {}", MQConfig.TOPIC_MASTER_QUEUE, msg);
        // System.out.println("receiveMasterMsg#Received string message: " + msg);
        try {
            // Thread.sleep(5000);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_SLAVE_QUEUE,
                    durable = "true",
                    autoDelete = "false",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)
                    }
            ),
            exchange = @Exchange(value = MQConfig.TOPIC_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = {MQConfig.TOPIC_SLAVE_ROUTE_KEY, MQConfig.FANOUT_SLAVE_ROUTE_KEY} // 队列可以绑定多个路由键
    ))
    public void receiveSlaveMsg(Message msg, Channel channel) {
        log.info("{} 收到消息: {}", MQConfig.TOPIC_SLAVE_QUEUE, msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /*============================================== 交换机类型 ===========================================*/

    /**
     * 测试 direct 类型的交换机
     *
     * @param msg     msg
     * @param channel channel
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct-test"), exchange = @Exchange(value = "amq.direct"), key = "direct.key.test"))
    public void directQueue(Message msg, Channel channel) {
        log.info("direct-test 收到消息: {}", msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /**
     * 测试 direct 类型的交换机
     *
     * @param msg     msg
     * @param channel channel
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct-test2"), exchange = @Exchange(value = "amq.direct"), key = "direct.key.test2"))
    public void directQueue2(Message msg, Channel channel) {
        log.info("direct-test2 收到消息: {}", msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /*===============================================fanout(设置的routingkey不会生效,绑定到交换机的所有队列都会收到消息)===============================================*/

    /**
     * 测试 direct 类型的交换机
     *
     * @param msg     msg
     * @param channel channel
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "fanout-test"), exchange = @Exchange(value = "amq.fanout", type = ExchangeTypes.FANOUT), key = "direct.key.test"))
    public void fanoutQueue(Message msg, Channel channel) {
        log.info("fanout-test 收到消息: {}", msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /**
     * 测试 direct 类型的交换机
     *
     * @param msg     msg
     * @param channel channel
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "fanout-test2"), exchange = @Exchange(value = "amq.fanout", type = ExchangeTypes.FANOUT), key = "direct.key.test2"))
    public void fanoutQueue2(Message msg, Channel channel) {
        log.info("fanout-test2 收到消息: {}", msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

}

package org.hulei.keeping.server.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.demo.commom.core.consts.MQConfig;
import com.hundsun.demo.commom.core.model.ConsumerStatus;
import com.hundsun.demo.commom.core.model.MQIdempotency;
import org.hulei.keeping.server.rabbitmq.consumer.mapper.MQIdempotencyMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

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

    /*============================================== 消息重复消费问题 ===========================================*/

    /**
     * 正在消费
     */
    public static final String CONSUMING = "CONSUMING";
    /**
     * 消费完成
     */
    public static final String CONSUMED = "CONSUMED";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    MQIdempotencyMapper mqIdempotencyMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 保证消息不被重复消费(即最多消费一次,且成功消费一次) <br>
     * 1. 采用唯一ID+数据库主键 <br>
     * 2. 利用 redis来实现幂等 <br>
     *
     * @param msg     msg
     * @param channel 信道
     */
    private void repeatConsumer(Message msg, Channel channel) {
        log.info("接收到消息 msg: {}", msg);
        MQIdempotency validation = JSONObject.parseObject(new String(msg.getBody(), StandardCharsets.UTF_8), MQIdempotency.class);
        ConsumerStatus status = msgConsumableByRedis(validation);
        // ConsumerStatus status = msgConsumableByMysql(validation);

        // 记录是否成功应答
        boolean isAck = false;

        if (status.isFinished()) {
            // 如果消息已经被消费完成了, 那么直接应答
            log.info("消息已经被其他线程消费,不再处理");
            isAck = true;
        } else {
            if (status.isEnableConsumer()) {
                // 处理业务逻辑
                // 模拟一个比较长时间的消息处理时间
                log.info("获得消费权,开始消费消息(五秒后执行完成)");
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 消息处理完毕之后, 要更新消息表的消息处理状态
                // finishConsumerByMysql(validation);
                finishConsumerByRedis(validation);
                log.info("执行结束");
                isAck = true;
            }
            // 消息没有被消费完成, 但是当前有其他服务正在消费, 可以拒绝消息
        }

        try {
            if (isAck) {
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
                log.info("ack成功");
            } else {
                // 当前有消费者正在处理消息
                // 是重新入列呢,还是直接抛弃这个消息呢? 入列是否已经没有必要了? 2024年4月14日 看具体的业务场景吧.
                log.info("当前有重复的消息正在被其他线程消费,将延迟5秒进行拒绝消息,以重新入列");
                Thread.sleep(5 * 1000);
                channel.basicReject(msg.getMessageProperties().getDeliveryTag(), true);
            }
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /**
     * 利用唯一 ID + mysql主键来控制重复消费的问题, 优化可以避免重复消费问题
     *
     * @param validation 消息
     */
    private ConsumerStatus msgConsumableByMysql(MQIdempotency validation) {

        // 创建一个消息的过期时间, 如果状态是 正在消费中&时间过期 那代表
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义格式，不显示毫秒
        Timestamp now = new Timestamp(System.currentTimeMillis()); // 获取系统当前时间
        String nowTime = df.format(now);
        Timestamp explore = new Timestamp(System.currentTimeMillis() - (60 * 1000)); // 60秒超时时间(根据业务执行时间来定)
        String exploreTime = df.format(explore);

        try {
            validation.setTime(nowTime);
            validation.setStatus(CONSUMING);
            int i = mqIdempotencyMapper.insertSelective(validation);
        } catch (DuplicateKeyException e) {

            // 主键冲突报错, 进行延迟消费, 假设消息的过期时间是 十分钟
            // 首先去查看这个消息的消费状态
            MQIdempotency mqIdempotency = mqIdempotencyMapper.selectByPrimaryKey(validation);
            // 如果有其他消费者正在消费这个消息
            if (CONSUMING.equals(mqIdempotency.getStatus())) {
                // 1. 判断消息是否过期
                validation.setStatus(CONSUMING);
                validation.setTime(exploreTime);
                /*
                这里是否依旧存在问题? 如果就是另一台机器单纯的慢呢? 正好执行了十分零一秒, 那是否意味着依旧存在重复消费的问题?
                这里要引入过期时间的更新机制，正在消费的线程如果由于处理时间过于长则需要整个过程中不断的调整过期时间
                 */
                // 删除过期时间之外的消息
                int i = mqIdempotencyMapper.deleteByTime(validation);
                if (i > 0) {
                    // 说明消费已经超时了, 可以进行这一次的消费了
                    log.info("上一次消费已超时,准备重新消费消息");
                    // 插入一个新的时间
                    validation.setTime(nowTime);
                    // 重新再插入一个新的
                    mqIdempotencyMapper.insertSelective(validation);
                    return new ConsumerStatus(true, false);
                } else {
                    // 消费没有超时, 仍在消费中, 把这个信息传递出去, 具体怎么处理看情况
                    log.info("目前有其他线程正在消费中(不保证线程已经宕机,消费还没过期)");
                    return new ConsumerStatus(false, false);
                }
            }
            // 如果这个消息已经被成功消费过了, 就不再次消费(相较于)
            if (CONSUMED.equals(mqIdempotency.getStatus())) {
                return new ConsumerStatus(false, true);
            }
        }

        // 插入数据成功意味着, 当前服务拿到消费权就正常消费
        return new ConsumerStatus(true, false);
    }

    /**
     * 结束消费
     *
     * @param validation msg
     */
    private void finishConsumerByMysql(MQIdempotency validation) {
        validation.setStatus(CONSUMED);
        mqIdempotencyMapper.updateByTime(validation);
    }

    /**
     * 利用 redis 原子性来确保重复性消费 - 判断消息是否可以消费
     *
     * @param validation 消息
     */
    private ConsumerStatus msgConsumableByRedis(MQIdempotency validation) {

        // TODO 这里应该设置成锁的结果，而不应该是这个键值对
        // 执行设置键值对操作的结果 (false表示设置不成功[有其他线程在消费], true表示当前线程可以消费)
        boolean flag = Boolean.TRUE.equals(
                stringRedisTemplate.execute( // 执行一个Redis操作的通用方法
                        (RedisCallback<Boolean>) connection -> connection.set( // RedisCallback允许直接访问底层的Redis连接，执行更复杂或者是原生的Redis命令。
                                validation.getUuid().getBytes(), // 获取消息的 UUID (同时也是接下来要设置的 redis的<key,value> 的 value)
                                CONSUMING.getBytes(), // 设置当前 UUID 的这个消息的状态为正在消费中 (同时也是接下来要设置的 redis的<key,value> 的 value)
                                Expiration.from(60, TimeUnit.SECONDS), // 设置当前要设置的键值对的过期时间, 可能某个消费者消费途中宕机了, 那么这个状态需要过期, 以便后续其他线程能够正常消费
                                RedisStringCommands.SetOption.ifAbsent() // 设置条件。这个选项意味着只有当键不存在时，才会设置键值对。
                        )));

        if (flag) {
            log.info("当前可以消费消息");
            return new ConsumerStatus(true, false);
        } else {
            // 查看消息的状态(可能会存在消息生产方重复生产消息了), 所以已经成功消费的消息要记录下来, 然后以备校验
            if (CONSUMED.equals(stringRedisTemplate.opsForValue().get(validation.getUuid()))) {
                log.info("当前消息已被成功消费");
                return new ConsumerStatus(false, true);
            } else {
                // 消息并没有被消费完成, 但是消息正在被其他服务消费
                log.info("当前有其他服务正在消费此消息");
                return new ConsumerStatus(false, false);
            }
        }
    }

    /**
     * 利用 redis 原子性来确保重复性消费 - 结束消费,更新redis的键值
     *
     * @param validation msg
     */
    private void finishConsumerByRedis(MQIdempotency validation) {
        // 没有设置过期时间, 保存消息消费已经被消费的这个状态
        stringRedisTemplate.opsForValue().set(validation.getUuid(), CONSUMED);
    }
}

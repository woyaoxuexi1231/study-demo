package org.hulei.springboot.rabbitmq.spring.consumer;

import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * RabbitListener 和 RabbitHandler 配合使用
 * <p>
 * RabbitListener 修饰类的时候代表整个类作为一个监听类,使用 RabbitHandler 修饰的方法将作为 listener对应的处理方法
 * 1. 不能有多个RabbitHandler方法并且都能处理同一种类型的消息(比如这里的String类型),会因为找不到唯一的处理方法而报错
 * Caused by: org.springframework.amqp.AmqpException: Ambiguous methods for payload type: class java.lang.String: handleMessage and receive2
 * <p>
 * 2. 多个不同入参的方法,会因为发送端的消息类型而确定具体使用哪个handler方法
 * 3. 单独使用RabbitListener没有用,必须要配置至少一个RabbitListener使用
 * 4. RabbitListener(spring-boot-starter-amqp 2.7.16)声明的绑定关系在队列已经存在的情况下好像并不会更新,即使不存在这种绑定关系也不会新增,所以队列声明最好是用 RabbitAdmin
 *
 * @author hulei42031
 * @since 2024-03-28 14:57
 */

@Component
@Slf4j
@RabbitListener(
        // queues = MQConfig.TOPIC_MASTER_QUEUE, // queues,bindings,queuesToDeclare三个只能配置一个, queues参数, 如果队列不存在, 启动将会报错
        containerFactory = "", // 指定监听器容器工厂,可以@RabbitListenerContainerFactory配置自定义容器创建工厂,这里使用默认的
        id = "", // 指定容器的id, 监听容器指的就是当前这个类/方法
        autoStartup = "true", // 指定是否在启动的时候自动启动监听容器
        concurrency = "1", // 指定并发消费者的数量, spring提供的能力
        exclusive = false, // 指定是否为独占模式,即只允许一个消费者监听队列,这是一个spring提供的能力,rabbitmq本身不提供这种能力
        priority = "0", // 指定消费者的优先级 todo
        admin = "", // 指定RabbitAdmin, 用于需要创建队列时自动声明队列 todo
        bindings = { // 指定队列和交换机之间的绑定关系,通过这个参数配置后,启动时会创建这种绑定关系,如果和现存的不一致,会报错
                @QueueBinding(
                        value = @Queue(
                                value = MQConfig.HANDLER_AND_LISTENER_QUEUE,
                                durable = "true", // 是否持久化。代表队列在服务器重启后是否还存在
                                exclusive = "false", // 是否排他性队列。排他性队列只能在声明它的 Connection 中使用（可以在同一个Connection的不同的channel中使用），连接断开时自动删除
                                autoDelete = "false", // 队列是否自动删除, 如果为true，至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，队列会自动删除。
                                arguments = { // 配置参数,多个参数用多个不同的@Argument配置
                                        @Argument( // 配置队列的高级参数
                                                name = "x-dead-letter-exchange", // 队列高级参数的名称
                                                value = MQConfig.DEAD_EXCHANGE_NAME // 对应参数的值
                                        ),
                                        @Argument(
                                                name = "x-dead-letter-routing-key",
                                                value = MQConfig.HANDLER_AND_LISTENER_DEAD_ROUTE_KEY
                                        ),
                                        @Argument(
                                                name = "x-max-length",
                                                value = "6",
                                                type = "java.lang.Integer"
                                        )
                                }
                        ),
                        exchange = @Exchange(
                                value = MQConfig.NORMAL_TOPIC_EXCHANGE,
                                type = ExchangeTypes.TOPIC,
                                durable = "true", // 持久化
                                autoDelete = "false", // 是否自动删除
                                arguments = {}
                        ),
                        key = {MQConfig.HANDLER_AND_LISTENER_ROUTE_KEY}
                )
        },
        returnExceptions = "", // 指定在消息无法被路由到队列时,应该抛出的异常类型
        errorHandler = "", // 指定消息异常时的错误处理类
        group = "" // 设置消费者组, 具有相同组名的消费者将共享处理消息的工作, todo amqp可能没有作用
)
public class RabbitHandlerListener {

    /**
     * 入参以byte[]接收数据
     *
     * @param msgBytes 以bytes的形式接收消息
     * @param msg      Message类,包括 message 的一些配置,属性信息
     * @param channel  信道
     */
    @RabbitHandler
    public void receiveBytes(byte[] msgBytes, Message msg, Channel channel) {
        // 如果发送端发送的是byte[],那么就会触发这个方法,
        // org.hulei.springboot.rabbitmq.basic.producer.MsgProducer 中,rabbitmq提供的api只能发送btye数据类型的数据,所以都会通过这个方法获取数据
        log.info("byte[]入参监听方法收到消息, messageBytes: {}, msgBytes: {}", Arrays.toString(msgBytes), msg);
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /**
     * 入参以字符串接收消息
     *
     * @param msgStr  消息字符串
     * @param msg     Message类,包括 message 的一些配置,属性信息
     * @param channel 信道
     */
    @RabbitHandler
    public void receiveString(String msgStr, Message msg, Channel channel) {
        log.info("string入参监听方法收到消息, messageStr: {}, msgBytes: {}", msgStr, msg);
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    // @RabbitHandler
    public void handleMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("RabbitHandlerListener#handleMessage Received string message: " + message);
        try {
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

}

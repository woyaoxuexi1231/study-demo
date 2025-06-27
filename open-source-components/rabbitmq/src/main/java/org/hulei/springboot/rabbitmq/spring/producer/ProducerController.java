package org.hulei.springboot.rabbitmq.spring.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.Message;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.rabbitmq.producer
 * @className: RabbitmqController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/15 23:57
 */

@Slf4j
@RestController
@RequestMapping(value = "/rabbimq")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @PostMapping(value = "/convertAndSend")
    public void convertAndSend() {

        // 创建一个消息实体
        Message message = new Message();
        long currentTimeMillis = System.currentTimeMillis();
        message.setUuid(String.valueOf(currentTimeMillis)); // 设置消息的uuid
        message.setMsg("hello rabbitmq!"); // 设置消息内容

        /*
        是 Spring AMQP 里用来给消息 标记一个唯一 ID 的，主要用于：
        1️⃣ 消息确认（publisher confirm）
        2️⃣ 消息回调（ack / nack）
        3️⃣ 跟踪消息（看是哪条消息投递成功/失败）

        最常见场景就是 开启发布确认（Publisher Confirms）后 消息是否被成功投递了或者是失败了 可以通过 CorrelationData 来确认具体是哪一条消息
        这个和 correlationId 又不一样了，这个仅仅是作为回调函数中确认消息成功或是失败，而correlationId则主要用于rpc确认响应
         */
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(currentTimeMillis));

        /*
        rabbitTemplate 提供了好几种发送消息的方式

        rabbitTemplate.convertAndSend 这个方法提供了多种变体，包括指定交换机，路由键，消息实体
                                      最常用的方法，先把对象消息自动序列化成 Message（根据配置的 MessageConverter），然后发送。
                                      不需要自己构造 Message 对象，自动转换。
        rabbitTemplate.send convertAndSend 方法底层就是调用的这个方法，send方法需要自己包装 Message

        rabbitTemplate.convertSendAndReceive 和 convertAndSend 类似，但是此方法会阻塞
        rabbitTemplate.sendAndReceive convertSendAndReceive底层调用这个方法，和上面send类似
         */
        rabbitTemplate.convertAndSend(
                MQConfig.DIRECT_EXCHANGE_NAME, // 配置交换机
                MQConfig.DIRECT_MASTER_ROUTE_KEY, // 路由键
                JSON.toJSONString(message), // 这里消息可以发送不同类型，我这里选择json只是为了统一序列化操作
                msg -> msg, // 配置 MessagePostProcessor
                correlationData // correlation data (can be null).
        );
    }

    @PostMapping(value = "/convertSendAndReceive")
    public void convertSendAndReceive() {


        // 创建一个消息实体
        Message message = new Message();
        long currentTimeMillis = System.currentTimeMillis();
        message.setUuid(String.valueOf(currentTimeMillis)); // 设置消息的uuid
        message.setMsg("hello rabbitmq!"); // 设置消息内容

        CorrelationData correlationData = new CorrelationData(String.valueOf(currentTimeMillis));

        Object receive = rabbitTemplate.convertSendAndReceive(
                MQConfig.TOPIC_EXCHANGE_NAME, // 配置交换机
                MQConfig.TOPIC_MASTER_ROUTE_KEY, // 路由键
                JSON.toJSONString(message), // 这里消息可以发送不同类型，我这里选择json只是为了统一序列化操作
                msg -> msg, // 配置 MessagePostProcessor
                correlationData // correlation data (can be null).
        );
        log.info("receive: {}", Objects.isNull(receive) ? "null" : new String((byte[]) receive, StandardCharsets.UTF_8));
    }

    @PostMapping(value = "/invoke")
    public void invoke() {
        // 创建一个消息实体
        Message message = new Message();
        long currentTimeMillis = System.currentTimeMillis();
        message.setUuid(String.valueOf(currentTimeMillis)); // 设置消息的uuid
        message.setMsg("hello rabbitmq!"); // 设置消息内容

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(currentTimeMillis));

        /*
        RabbitTemplate.invoke 是从 Spring AMQP 2.3 开始引入的，它是一个更现代、更“函数式”的 API。

        📌 核心点
           - invoke 接收一个 OperationsCallback<RabbitOperations, T>，把 RabbitTemplate 作为 RabbitOperations 传进去。
           - 你在 invoke 里就可以调用所有操作，比如先发后收、事务、或批量执行等。
           - 常用于组合多个操作，或者做一些需要在同一 Channel 内执行的事情（因为 invoke 会把操作放到同一个 RabbitMQ Channel 中）。
         */
        rabbitTemplate.invoke(operations -> {
            rabbitTemplate.convertAndSend(
                    MQConfig.DIRECT_EXCHANGE_NAME, // the exchange. 如果交换机不存在,reply-code=404, reply-text=NOT_FOUND - no exchange 'exchange-test-topic2' in vhost '/', class-id=60, method-id=40
                    MQConfig.DIRECT_MASTER_ROUTE_KEY, // the routing key.
                    JSON.toJSONString(message), // the data to send.
                    msg -> msg, // a message post processor (can be null).
                    correlationData // correlation data (can be null).
            );
            try {
                // 阻塞式发布确认, 如果配置了 confirm 回调, 依旧会触发 confirm 回调
                // 如果没有开启发布确认，那么这个方法会调用失败
                boolean confirms = rabbitTemplate.waitForConfirms(1000); // 等待1秒
                log.info("等待rabbitMQ确认完成,结果: {}", confirms);
                return confirms;
            } catch (AmqpException e) {
                // 处理异常情况
                log.error("等待rabbitMQ确认出现异常, ", e);
                return false;
            }
        });
    }

    @GetMapping("/sendTopicForDeadMsg")
    public void sendTopicForDeadMsg() {
        // 用于在将消息发送到消息队列之前对消息进行后处理。它允许你在消息发送前对消息的各个属性进行修改或者添加一些额外的处理逻辑。
        MessagePostProcessor messagePostProcessor = message -> {
            // 设置消息的过期时间
            message.getMessageProperties().setExpiration("30000");
            return message;
        };
        // convertAndSend方法本身是同步的,但是他仅仅只是把消息刷入连接的发送缓冲区,后续就直接返回了,所以并不提供发布确认机制
        rabbitTemplate.convertAndSend(
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_FOR_DEAD_QUEUE_ROUTE_KEY,
                "hello world",
                messagePostProcessor
        );
    }

    @GetMapping("/sendMsgToCustomContainerQueue")
    public void sendMsgToCustomContainerQueue() {
        // 每次产生500条消息
        for (int i = 0; i < 500; i++) {
            rabbitTemplate.convertAndSend(
                    MQConfig.TOPIC_EXCHANGE_NAME,
                    MQConfig.KEY_FOR_CUSTOM_CONTAINER,
                    "hello custom container"
            );
        }
    }

    @GetMapping("/topicExchangeToDirectExchange")
    public void topicExchangeToDirectExchange() {
        rabbitTemplate.convertAndSend(
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY,
                "hello direct"
        );
    }


}

package org.hulei.springboot.rabbitmq.basic.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageConsumer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:20
 */

@Slf4j
public class TopicDeadClient implements Runnable {

    Connection connection;

    public TopicDeadClient(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            // 如果在同一个信道上消费多个队列的消息,每个队列都可以拉取prefetchCount数量的消息,但是其中一个阻塞消费,其他的都会被阻塞
            channel.basicQos(1);
            channel.basicConsume(
                    MQConfig.TOPIC_FOR_DEAD_QUEUE,
                    false,
                    "",
                    new DefaultConsumer(channel) {
                        @SneakyThrows
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                            // 这里阻塞消费一下,为了是触发队列消息达到最大值
                            log.info("收到消息: {}, 主题: {}", new String(body, StandardCharsets.UTF_8), envelope.getRoutingKey());
                            /*
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
                            Thread.sleep(1000000000);
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
            );

            Channel deadChannel = connection.createChannel();
            deadChannel.basicQos(1);
            deadChannel.basicConsume(
                    MQConfig.DEAD_QUEUE_NAME,
                    false,
                    "",
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            log.info("死信队列收到消息: {}", new String(body, StandardCharsets.UTF_8));
                            deadChannel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
            );
        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


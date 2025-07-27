package org.hulei.springboot.rabbitmq.basic.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.util.StringUtils;

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

public class TopicMasterClient implements Runnable {

    Connection connection;

    public TopicMasterClient(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {

        try {
            // 创建信道
            Channel channel = connection.createChannel();
            /*
            用于控制消费者的流量控制方法。它用于指定消费者在处理消息时的预取数量（prefetch count）。
            basicQos(int prefetchSize, int prefetchCount, boolean global)
            prefetchSize - 限制消费者一次从 RabbitMQ 服务器拉取的消息的总大小（以字节为单位）。0（表示无限制）。
            prefetchCount - 限制消费者一次从 RabbitMQ 服务器拉取的未确认消息数量。0（表示无限制）
            global - 是否将上面的设置应用于单个消费者还是整个通道（Channel）
             */
            channel.basicQos(1);

            /*
            basicConsume 方法是用来从队列里 异步消费消息, 所以提供了重载了多个不同的方法，提供了好几种不同的回调函数

            Consumer 老式的统一回调接口，包含所有事件（交付、取消、shutdown 等），适合需要处理多种事件时
            DeliverCallback 专门处理收到消息时要做的事，收到一条新消息时调用
            CancelCallback 	当消费者被取消（如队列被删除）时触发
                当 RabbitMQ 取消某个消费者时，会调用 CancelCallback 接口的方法，你可以在该方法中编写自定义逻辑来处理消费者被取消的情况。
                消费者被取消时，指的是消费者从 RabbitMQ 队列中停止接收消息并取消消费的状态。消费者取消可能发生在以下几种情况下：
                1. 手动取消消费者：你可以显式地调用 basicCancel 方法来取消消费者。这意味着你主动告诉 RabbitMQ，你不再对该队列的消息进行消费。这种情况下，CancelCallback 将会被调用。
                2. 自动取消消费者：当消费者连接断开或者消费者所在的频道（channel）关闭时，消费者会自动被取消。例如，如果消费者所在的进程崩溃、网络故障导致消费者与 RabbitMQ 之间的连接断开，RabbitMQ 会检测到连接关闭，并自动将相关消费者标记为取消状态。
                除了上述情况外，还有其他一些因素可能导致消费者被取消，例如：
                1. 队列被删除：如果消费者所订阅的队列被删除，消费者将会被取消。
                2. 队列名变更：如果消费者所订阅的队列名称被修改，消费者将被取消。当重新声明队列时，之前的消费者将无法继续处理消息。
                3. 消费者的启动参数变更：例如，在消费者启动时设置了自动确认（autoAck），如果后续修改为手动确认（manualAck），消费者将会被取消。
                当消费者被取消时，RabbitMQ 会发送一个取消通知，消费者可以在 CancelCallback 中处理这个取消事件，例如执行清理操作、记录日志或发送通知等。
            ConsumerShutdownSignalCallback 这个接口用于当消费者接收到关闭信号时执行相应的操作。
                当消费者接收到关闭信号时，会调用 handleShutdownSignal 方法并传入一个 ShutdownSignalException，你可以在这个方法中执行一些操作，比如记录日志、发送通知或执行清理操作等。

            因此，主要的区别在于范围和功能上。DeliverCallback 更专注于消息交付的处理，而 Consumer 则提供了更全面的消费者处理能力，涵盖了消息交付、取消和关闭等方面。
            在实际应用中，你可以根据需要选择适合的接口来处理消息，如果只需要处理消息的交付逻辑，可以使用 DeliverCallback；如果需要对消费者的整个生命周期进行管理，包括消息交付、取消和关闭时的处理，可以使用 Consumer 接口。
            */
            channel.basicConsume(
                    MQConfig.TOPIC_MASTER_QUEUE,
                    false,
                    "", // 消费者的唯一标识符,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            System.out.println("=======================================================收到消息====================================================");
                            System.out.printf("消费者标识符(这个标识符就是消费者注册在MQ上的一个唯一标识): %s%n", consumerTag);
                            System.out.printf("包含消息的投递元数据(deliveryTag 消息在当前 channel 中的唯一标识（64位长整数）用于消息确认（ack/nack）): %s%n", envelope);
                            System.out.printf("消息本身的配置信息，这个由生产者发送消息时配置的: %s%n", properties);
                            System.out.printf("主题: %s, 消息内容: %s%n", envelope.getRoutingKey(), new String(body, StandardCharsets.UTF_8));
                            System.out.printf("回复队列,收到消息后,可以通过这个回复队列去回复消息,回复队列名称: %s%n", properties.getReplyTo());
                            System.out.printf("correlationId: %s%n", properties.getCorrelationId());
                            System.out.println("=======================================================解析结束====================================================");

                            if (StringUtils.hasLength(properties.getReplyTo())) {
                                // 回复一个消息到指定的队列中去, rpc服务端的实现
                                channel.basicPublish(
                                        "",
                                        properties.getReplyTo(),
                                        new AMQP.BasicProperties().builder().correlationId(properties.getCorrelationId()).build(),
                                        ("消息标识符: " + properties.getCorrelationId() + ", " + envelope.getRoutingKey() + ", " + MQConfig.TOPIC_MASTER_QUEUE + ", " + new String(body, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
                            }
                            // deliveryTag 是用来唯一标识每条投递的消息的整数值。根据官方文档的描述，deliveryTag 是在每个 channel 上保持唯一性的，并且会在 channel 打开时从 1 开始递增。
                            // ack的时候乱写deliveryTag会导致ack失败的，而ack失败又没有进行处理那么就会导致这个消费者接受不到消息了。
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }

                        @Override
                        public void handleCancel(String consumerTag) throws IOException {
                            System.out.println("消费消息被中断!");
                        }
                    });

        } catch (Exception e) {
            System.out.printf("消息接收异常. error: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }
}


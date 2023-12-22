package com.hundsun.demo.java.mq.rabbit.work;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

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
public class MsgPushConsumerA extends MsgConsumer {

    public MsgPushConsumerA(Connection connection, String queueName) {
        super(connection, queueName);
    }

    @Override
    public void run() {

        try {
            // 创建信道
            Channel channel = connection.createChannel();
            // 手动应答
            boolean autoAck = false;
            /*
            用于控制消费者的流量控制方法。它用于指定消费者在处理消息时的预取数量（prefetch count）。
            basicQos(int prefetchSize, int prefetchCount, boolean global)
            prefetchSize - 服务器将提供的最大内容量(以八位字节为单位), 如果无限制, 则为 0, 消息大小限制, 一般设置为 0, 消费端不做限制
            prefetchCount - 服务器将传递的最大邮件数, 如果无限制, 则为 0, 告诉 rabbitmq 不要一次性给消费者推送大于 N 个消息, 即一旦有 N 个消息还没有 ack, 则该 consumer 将 block(阻塞), 直到有消息ack
            global - 是否将上面的设置应用于整个通道

            具体来说，channel.basicQos 可以控制消费者从消息队列中预取的消息数量。通过设置预取数量，可以确保消费者在处理一批消息时不会一次性获取过多的消息，从而提高消息处理的效率和公平性。
            流量控制可以防止消费者在高负载情况下处理过多的消息，避免了资源瓶颈和消息堆积。它还能够提供公平分配消息的机制，确保所有消费者能够平均处理消息队列中的消息。
             */
            channel.basicQos(1);
            /*
            ① String basicConsume(String queue, boolean autoAck, String consumerTag, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException;
            ② String basicConsume(String queue, boolean autoAck, String consumerTag, Consumer callback) throws IOException;

            Consumer和Callback使用推消息模式, 通过持续订阅来获取消息
            * autoAck - 是否自动应答
            * consumerTag - 消费者标签, 用来区分多个消费者, 如果你在订阅消息队列时将 consumerTag 设定为空，那么消息代理（如RabbitMQ）会为消费者生成一个唯一的 consumerTag，并将其返回给消费者应用程序。这个生成的 consumerTag 通常是一个唯一的字符串，用于标识特定的消费者，以便进行后续的消息交付和管理。
            * noLocal 设置为 true, 表示不能将同一个 Connection 中生产者发送的消息传递给这个 Connection 中的消费者
            * exclusive 是否排他
            * arguments 消费者的参数

            * DeliverCallback 当一个消息发送过来后的回调
                DeliverCallback 是在 RabbitMQ 中用于处理消息传递（delivery）的函数式接口。
                它通常作为参数传递给消费者端的 basicConsume 方法，用于定义当消息被接收时如何处理消息的逻辑。
                当消费者接收到消息时，会调用 DeliverCallback 接口的方法来处理接收到的消息内容。
                在这个回调中，你可以编写处理消息的逻辑，比如解析消息内容、进行业务处理等。
            * CancelCallback 参数用于指定在消费者被取消时要执行的回调操作
                当 RabbitMQ 取消某个消费者时，会调用 CancelCallback 接口的方法，你可以在该方法中编写自定义逻辑来处理消费者被取消的情况。
                消费者被取消时，指的是消费者从 RabbitMQ 队列中停止接收消息并取消消费的状态。消费者取消可能发生在以下几种情况下：
                1. 手动取消消费者：你可以显式地调用 basicCancel 方法来取消消费者。这意味着你主动告诉 RabbitMQ，你不再对该队列的消息进行消费。这种情况下，CancelCallback 将会被调用。
                2. 自动取消消费者：当消费者连接断开或者消费者所在的频道（channel）关闭时，消费者会自动被取消。例如，如果消费者所在的进程崩溃、网络故障导致消费者与 RabbitMQ 之间的连接断开，RabbitMQ 会检测到连接关闭，并自动将相关消费者标记为取消状态。
                除了上述情况外，还有其他一些因素可能导致消费者被取消，例如：
                1. 队列被删除：如果消费者所订阅的队列被删除，消费者将会被取消。
                2. 队列名变更：如果消费者所订阅的队列名称被修改，消费者将被取消。当重新声明队列时，之前的消费者将无法继续处理消息。
                3. 消费者的启动参数变更：例如，在消费者启动时设置了自动确认（autoAck），如果后续修改为手动确认（manualAck），消费者将会被取消。
                当消费者被取消时，RabbitMQ 会发送一个取消通知，消费者可以在 CancelCallback 中处理这个取消事件，例如执行清理操作、记录日志或发送通知等。
            * ConsumerShutdownSignalCallback 这个接口用于当消费者接收到关闭信号时执行相应的操作。
                当消费者接收到关闭信号时，会调用 handleShutdownSignal 方法并传入一个 ShutdownSignalException，你可以在这个方法中执行一些操作，比如记录日志、发送通知或执行清理操作等。


            * Consumer 消费者对象的回调接口
                Consumer 是一个更为全面的接口，它不仅包括了消息交付（DeliverCallback），还包括了消息取消（CancelCallback）和关闭信号（ShutdownSignalCallback）等功能。
                通过实现 Consumer 接口，你可以自定义消费者的行为，包括处理交付的消息、取消消费者、以及处理消费者关闭的情况。

            因此，主要的区别在于范围和功能上。DeliverCallback 更专注于消息交付的处理，而 Consumer 则提供了更全面的消费者处理能力，涵盖了消息交付、取消和关闭等方面。
            在实际应用中，你可以根据需要选择适合的接口来处理消息，如果只需要处理消息的交付逻辑，可以使用 DeliverCallback；如果需要对消费者的整个生命周期进行管理，包括消息交付、取消和关闭时的处理，可以使用 Consumer 接口。
            */
            channel.basicConsume(
                    queueName,
                    autoAck,
                    "",
                    new DefaultConsumer(channel) {
                        /**
                         * 这是一个用于处理消息交付的方法
                         * @param consumerTag 消费者标识
                         * @param envelope packaging data for the message, envelope 参数是一个包含了消息的交付信息的对象
                         * @param properties 消息的附加信息对象
                         * @param body the message body (opaque, client-specific byte array)
                         * @throws IOException handleDelivery 方法中的 IOException 可能会被抛出来，用于表示与 RabbitMQ 服务器之间的消息读取或写入等操作可能会遇到异常情况，比如网络连接中断、读写超时等。
                         */
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            log.info("consumerTag: {}", consumerTag);
                            log.info("envelope: {}", envelope);
                            log.info("properties: {}", properties);
                            log.info("topic: {}, body: {}", envelope.getRoutingKey(), new String(body, StandardCharsets.UTF_8));
                            // deliveryTag 是用来唯一标识每条投递的消息的整数值。根据官方文档的描述，deliveryTag 是在每个 channel 上保持唯一性的，并且会在 channel 打开时从 1 开始递增。
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }

                        @Override
                        public void handleCancel(String consumerTag) throws IOException {
                            log.info("消费消息被中断!");
                        }
                    });
        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


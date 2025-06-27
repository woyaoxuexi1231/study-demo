package org.hulei.springboot.rabbitmq.basic.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hulei
 * @since 2025/6/27 17:02
 */

public class SlaveConsumerWorker implements Runnable {
    Connection connection;

    public SlaveConsumerWorker(Connection connection) {
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
            channel.basicQos(3);

            AtomicInteger count = new AtomicInteger(0);

            // 绑定一个 direct 交换机类型的队列
            channel.basicConsume(
                    MQConfig.DIRECT_MASTER_QUEUE,
                    false,
                    (consumerTag, message) -> {
                        count.incrementAndGet();
                        System.out.printf("队列 %s 收到消息：%s%n", MQConfig.DIRECT_MASTER_QUEUE, new String(message.getBody(), StandardCharsets.UTF_8));
                        if (count.get() == 3) {
                            channel.basicAck(message.getEnvelope().getDeliveryTag(), true);
                            count.set(0);
                        }
                    },
                    consumerTag -> {
                        System.out.printf("消费者取消对队列 %s 的消费%n", MQConfig.DIRECT_MASTER_QUEUE);
                    }
            );

        } catch (Exception e) {
            System.out.printf("消息接收异常. error: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }
}

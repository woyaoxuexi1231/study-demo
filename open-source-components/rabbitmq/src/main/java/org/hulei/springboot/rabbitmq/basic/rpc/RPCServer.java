package org.hulei.springboot.rabbitmq.basic.rpc;// RPCServer.java

import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(MQConfig.RABBITMQ_USERNAME);
        factory.setPassword(MQConfig.RABBITMQ_PASSWORD);
        // 设置 ip 和 port
        factory.setHost(MQConfig.RABBITMQ_HOST);
        factory.setPort(MQConfig.RABBITMQ_PORT);
        // 设置虚拟机路径
        factory.setVirtualHost("/");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // 只是声明队列,但是没有绑定交换机,那么发送端就只能直接发送到指定队列
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();

            // 收到消息的回调
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";

                try {
                    // 解析收到的消息
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    int n = Integer.parseInt(message);
                    System.out.println(" [.] fib(" + message + ")");
                    // 结果
                    response += fib(n);
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e);
                } finally {
                    // 响应消息
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes(StandardCharsets.UTF_8));
                    // ack
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
            }));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }
}

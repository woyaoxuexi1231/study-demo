package com.hundsun.demo.spring.mq.rabbit.rpc;// RPCClient.java

import com.hundsun.demo.commom.core.consts.MQConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RPCClient implements AutoCloseable {
    private final Connection connection;
    private final Channel channel;

    public RPCClient() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(MQConfig.RABBITMQ_USERNAME);
        factory.setPassword(MQConfig.RABBITMQ_PASSWORD);
        // 设置 ip 和 port
        factory.setHost(MQConfig.RABBITMQ_HOST);
        factory.setPort(MQConfig.RABBITMQ_PORT);
        // 设置虚拟机路径
        factory.setVirtualHost("/");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public String call(String message) throws Exception {

        // 拼接消息
        final String corrId = UUID.randomUUID().toString();
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        // 发送消息
        String requestQueueName = "rpc_queue";
        // 交换机在这里设置为空字符串，表示消息直接发送到指定的队列，而不经过交换机。
        channel.basicPublish("", requestQueueName, props, message.getBytes(StandardCharsets.UTF_8));

        // 阻塞队列来等待回调
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        //
        String ctag = channel.basicConsume(
                replyQueueName, // 队列
                true, // true 自动ack
                (consumerTag, delivery) -> { // 一个消费者的回调函数，用于处理接收到的消息
                    if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                        response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    }
                },
                consumerTag -> { //  一个消费者取消的回调函数
                }
        );

        // 阻塞获取消息
        String result = response.take();
        // channel.basicConsume() 方法返回了一个消费者标签 ctag，用于唯一标识当前的消费者。而channel.basicCancel(ctag) 则是根据这个消费者标签取消消费者的订阅，即停止消费消息。
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] argv) {
        try (RPCClient rpcClient = new RPCClient()) {
            String response = rpcClient.call("2");
            System.out.println(" [.] Got '" + response + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

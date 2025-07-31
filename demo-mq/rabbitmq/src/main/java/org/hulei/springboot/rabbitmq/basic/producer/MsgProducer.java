package org.hulei.springboot.rabbitmq.basic.producer;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.ReturnListener;
import lombok.SneakyThrows;
import org.hulei.springboot.rabbitmq.basic.config.ConnectFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageProducer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:19
 */

public class MsgProducer {

    private static final Connection connection;

    private static Channel channel;

    private static String replyQueueName;

    private static DefaultConsumer consumer;

    static {

        // 通过连接工厂获取连接, 连接工厂已经配置好了连接 mq 的配置信息
        connection = ConnectFactory.getConnect();

        // 初始化信道
        try {

            // ============================================ 初始化信道的配置 ==================================================
            channel = connection.createChannel();
            // 开启发布确认, 在消息发送失败的情况下可以用于消息的重新处理, 为了消息可靠性一定要开启
            channel.confirmSelect();

            // ============================================ 确认回调 ==================================================
            // 开启确认回调，确认回调会在不同的时期触发不同的方法来达到回调的效果
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) {
                    // 此方法的触发条件是消息成功到达交换机
                    // multiple会标识当前这一次确认是否为批量确认，如果为批量确认则代表标识符(在channel内是递增的)之前的所有消息都已经被broker确认完成
                    System.out.printf("消息发送成功, deliveryTag(消息唯一标识符): %s, multiple(批量确认): %s%n", deliveryTag, multiple);
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) {
                    System.out.printf("消息发送失败, deliveryTag(消息唯一标识符): %s, multiple(批量确认): %s%n", deliveryTag, multiple);
                }
            });

            // ============================================ 返回回调 ==================================================
            // 开启一个返回回调，当消息无法被路由到目标队列时，该方法会被调用
            channel.addReturnListener(
                        /*
                        用于处理当消息无法被路由到对应的队列时触发的返回（Return）事件。
                        方法参数的含义如下：

                        replyCode: 返回的响应码，用于指示返回事件的类型。一般情况下，非零的值表示消息无法被路由到目标队列。
                        replyText: 返回的响应文本，描述了返回事件的具体信息。
                        exchange: 发送消息时指定的交换机名称。
                        routingKey: 发送消息时指定的路由键。
                        properties: 消息的属性，如消息类型、优先级等。
                        body: 消息的内容，以字节数组形式表示。
                        当消息被发送到交换机时，如果交换机无法将消息路由到任何队列中，就会触发返回事件。这通常是因为交换机没有匹配的绑定规则，或者路由键与绑定规则不匹配。

                        通过实现 handleReturn 方法，并在 Channel 中设置 addReturnListener，可以在发送消息后处理返回事件。
                        当返回事件发生时，RabbitMQ 会调用 handleReturn 方法，并将相关的信息作为参数传递给该方法，以供应用程序进行处理。

                        应用程序可以根据返回事件的信息进行适当的处理，比如记录日志、重新发送消息、或者执行其他特定的逻辑。
                        总之，handleReturn 方法用于处理 RabbitMQ 返回（Return）事件，当消息无法被路由到目标队列时，该方法会被调用，开发人员可以在这个方法中编写逻辑来处理返回事件。
                         */
                    new ReturnListener() {
                        @Override
                        public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String replyString = new String(body, StandardCharsets.UTF_8);
                            System.out.printf("消息未找到指定的队列, exchange: %s, routingKey: %s, replyString: %s%n", exchange, routingKey, replyString);
                        }

                    });


            // ============================================ 注册回复队列监听 ==================================================
            // 声明一个回复队列，使用channel.queueDeclare().getQueue()这个方法生成的队列是一个独占的、非持久的、自动删除的，这非常符合一个回复队列的特征
            replyQueueName = channel.queueDeclare().getQueue();
            // 创建一个消费者来消费这个回复队列的消息，在消息成功发送后，如果消费者端有需要传回来的消息就可以使用这种方法接受回复消息了
            consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String reply = new String(body, StandardCharsets.UTF_8);
                    System.out.printf("收到回复的消息: %s%n", reply);
                }
            };
            // 启动回复队列的监听
            channel.basicConsume(replyQueueName, true, consumer);

        } catch (Exception e) {
            System.out.printf("信道初始化失败. error: %s%n", e.getMessage());
        }
    }

    public static void postMsg(String exchangeName, String routingKey, String msg) {

        try {

            // =============================================== 消息配置信息 ==============================================
            /*
            BasicProperties 主要用于配置消息体的各种参数信息

             contentType - 标明是 application/octet-stream(二进制)，application/json、text/plain、application/xml，方便消费者解析
             contentEncoding - 如果消息经过压缩，比如 gzip
             headers - 传递自定义的键值对，用于路由、过滤、额外上下文
             deliveryMode - 1：非持久化，2：持久化（broker 重启后仍保留消息）
             priority - 如果队列支持优先级（需要声明队列时设置 x-max-priority），高优先级先消费
             correlationId - 关联 ID，RPC 场景：关联请求和响应，如果仅仅是发消息，那么这个属性似乎用户并不大
             replyTo - RPC 场景：告诉消费者把响应发到哪个队列
             expiration - 单条消息 TTL，过期后消息会被丢弃或进入死信队列
             messageId - 方便幂等性、去重
             timestamp - 消息生成时间
             type - 业务自定义标识，比如 order.created
             userId - 用于验证消息发送者是否与连接用户一致（可选）
             appId - 标识哪个应用发的
             clusterId - 集群 ID，几乎不用，保留字段
             */

            for (int i = 0; i < 1; i++) {
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties(
                        "application/octet-stream", // 配置消息格式是 json
                        null,
                        null,
                        2,
                        0,
                        UUID.randomUUID().toString(), // 配置关联 ID
                        replyQueueName, // 配置一个回复队列，提示消费者如果回复消息应该往这个回复队列内发送消息
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);


                // =============================================== 消息发送 ==============================================
                System.out.printf("准备发送消息, 交换机: %s, 路由: %s%n", exchangeName, routingKey);
                channel.basicPublish(
                        exchangeName, // 配置交换机
                        routingKey, // 配置路由键
                        true, // true:交换机无法根据自身的类型和路由键找到一个符合条件的队列,那么会调用Basic.return命令将消息返回给生产者 false:出现上述情况,消息将直接被丢弃
                        basicProperties, // 消息属性（需要单独声明）
                        msg.getBytes(StandardCharsets.UTF_8) // 消息体
                );

                // =============================================== 发布确认 ==============================================
                // 开启同步确认后，每条消息都会同步去确认，异步回调虽然也会起作用，但是批量确认就已经失效了，可以看到异步回调中每条消息都是确认过的
                // 如果把这个地方注释掉，就可以看到异步回调其实有很多是批量确认的
                boolean confirms = channel.waitForConfirms();
                if (confirms) {
                    System.out.println("交换机成功收到了消息！");
                }
            }
        } catch (Exception e) {
            System.out.printf("消息发送异常. error: %s%n", e.getMessage());
        }
    }
}


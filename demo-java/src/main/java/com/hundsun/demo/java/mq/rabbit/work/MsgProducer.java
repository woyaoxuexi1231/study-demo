package com.hundsun.demo.java.mq.rabbit.work;

import com.hundsun.demo.java.mq.rabbit.callback.MsgConfirmFailedCallBack;
import com.hundsun.demo.java.mq.rabbit.callback.MsgConfirmSuccessCallBack;
import com.hundsun.demo.java.mq.rabbit.config.ConnectFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageProducer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:19
 */

@Slf4j
public class MsgProducer {

    private static final Connection connection;

    private static Channel channel;

    static {
        // 通过连接工厂获取连接, 连接工厂已经配置好了连接 mq 的配置信息
        connection = ConnectFactory.getConnect();
        // 创建交换机
        ConnectFactory.initExchange();
    }

    public static void postMsg(String exchangeName, String routingKey, String msg) {

        try {

            if (channel == null) {
                // 发消息之前校验一下是否信道已经开启了, 发消息是需要通过信道发送的, 只有连接是不行的
                channel = connection.createChannel();
                // 开启发布确认
                channel.confirmSelect();
                // 添加一个成功回调和一个失败的回调
                channel.addConfirmListener(new MsgConfirmSuccessCallBack(), new MsgConfirmFailedCallBack());
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
                        (replyCode, replyText, exchange, rk, properties, body) -> {
                            String replyString = new String(body, StandardCharsets.UTF_8);
                            log.info("消息未找到指定的队列, exchange: {}, routingKey: {}, replyString: {}", exchange, rk, replyString);
                        });
            }
            /**
             * BasicProperties属性笔记
             contentType	消息体的MIME类型，如application/json
             contentEncoding	消息的编码类型, 如是否压缩
             headers	键/值对表, 用户自定义任意的键和值
             deliveryMode 消息的持久化类型 - 1为非持久化, 2为持久化, 性能影响巨大
             priority 指定队列中消息的优先级
             correlationId 一般用作关联消息的 message-id, 常用于消息的响应
             replyTo	构建回复消息的私有响应队列
             expiration 消息的过期时刻, 字符串, 但是呈现格式为整型, 精确到秒
             messageId 消息的唯一性标识, 由应用进行设置
             timestamp 消息的创建时刻, 整型, 精确到秒
             type 消息类型名称, 完全由应用决定如何使用该字段
             userId 标识已登录用户, 极少使用
             appId 应用程序的类型和版本号
             clusterId 集群 ID
             */
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties(
                    "application/octet-stream",
                    null,
                    null,
                    2,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            /**
             * 发送消息 basicPublish
             * String exchange, 交换机
             * String routingKey, 路由键
             * BasicProperties props, 消息属性（需要单独声明）
             * mandatory true:交换机无法根据自身的类型和路由键找到一个符合条件的队列,那么会调用Basic.return命令将消息返回给生产者 false:出现上述情况,消息将直接被丢弃
             * immediate 参数将判断队列中是否存在消费者,如果在所有满足条件的队列中都没有消费者,这条消息将被返回给生产者, 3.0之后这个参数被取消支持
             * byte[] body，消息体
             */
            channel.basicPublish(exchangeName, routingKey, true, basicProperties, msg.getBytes());
            // 等待确认
            // boolean isConfirm = channel.waitForConfirms();

        } catch (Exception e) {
            log.error("发送消息异常! ", e);
        }
    }
}


package com.hundsun.demo.java.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageProducer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:19
 */

@Slf4j
public class MessageProducer extends Thread {

    private final ConnectionFactory FACTORY;

    private final String msg;

    public MessageProducer(ConnectionFactory FACTORY, String msg) {
        this.FACTORY = FACTORY;
        this.msg = msg;
    }

    @Override
    public void run() {

        Connection connection = null;
        Channel channel = null;

        try {

            connection = FACTORY.newConnection();
            channel = connection.createChannel();

                /*
                 声明交换机
                 * String exchange, 交换机
                 * String type, 交换机类型（direct，topic，fanout）
                 * boolean durable, 是否持久化。代表交换机在服务器重启后是否还存在
                 * boolean autoDelete, 自动删除
                 * Map<String,Object> arguments，交换机其他属性，如x-message-ttl，x-max-length
                 */
            String exchangeName = "hello-exchange";

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, null);


            // 路由规则
            String routingKey = "testRoutingKey";

                /*
                 发送消息 basicPublish
                 * String exchange, 交换机
                 * String routingKey, 路由键
                 * BasicProperties props, 消息属性（需要单独声明）
                 * byte[] body，消息体
                 */
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties(
                    "application/octet-stream",
                    null,
                    null,
                    1,
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
            channel.basicPublish(exchangeName, routingKey, basicProperties, msg.getBytes());

                /*
                BasicProperties的属性

                contentType	消息体的MIME类型，如application/json
                contentEncoding	消息的编码类型，如是否压缩
                headers	键/值对表，用户自定义任意的键和值
                deliveryMode 消息的持久化类型 ，1为非持久化，2为持久化，性能影响巨大
                priority 指定队列中消息的优先级
                correlationId 一般用作关联消息的message-id，常用于消息的响应
                replyTo	构建回复消息的私有响应队列
                expiration 消息的过期时刻，字符串，但是呈现格式为整型，精确到秒
                messageId 消息的唯一性标识，由应用进行设置
                timestamp 消息的创建时刻，整型，精确到秒
                type 消息类型名称，完全由应用决定如何使用该字段
                userId 标识已登录用户，极少使用
                appId 应用程序的类型和版本号
                clusterId 集群ID
                 */

        } catch (Exception e) {
            log.error("发送消息异常! ", e);
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                log.error("连接关闭异常! ", e);
            }
        }
    }
}


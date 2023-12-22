package com.hundsun.demo.java.mq.rabbit.work;

import com.hundsun.demo.java.mq.rabbit.callback.MsgConfirmFailedCallBack;
import com.hundsun.demo.java.mq.rabbit.callback.MsgConfirmSuccessCallBack;
import com.hundsun.demo.java.mq.rabbit.config.ConnectFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
        try {
            // 创建信道
            channel = connection.createChannel();
            // 开启发布确认
            channel.confirmSelect();
            // 添加一个成功回调和一个失败的回调
            channel.addConfirmListener(new MsgConfirmSuccessCallBack(), new MsgConfirmFailedCallBack());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
             * byte[] body，消息体
             */
            channel.basicPublish(exchangeName, routingKey, basicProperties, msg.getBytes());
            // 等待确认
            // boolean isConfirm = channel.waitForConfirms();

        } catch (Exception e) {
            log.error("发送消息异常! ", e);
        }
    }
}


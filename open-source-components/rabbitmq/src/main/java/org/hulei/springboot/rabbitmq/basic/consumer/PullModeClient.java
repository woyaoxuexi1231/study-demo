package org.hulei.springboot.rabbitmq.basic.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;

import java.util.Calendar;
import java.util.Objects;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.mq.rabbit.work
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-22 18:42
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
public class PullModeClient implements Runnable {

    Connection connection;

    public PullModeClient(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        // 以拉模式去获取消息, 需要消费者手动获取消息, 而非由MQ服务器主动推送过来
        while (true) {
            try {
                long millis = 5000;
                log.info("当前时间: {}, {}毫秒后将获取一条消息 ", Calendar.getInstance().getTime(), millis);
                Thread.sleep(millis);
                Channel channel = connection.createChannel();
                boolean autoAck = false;
                GetResponse getResponse = channel.basicGet(MQConfig.TOPIC_PULL_QUEUE, autoAck);
                log.info("通过拉模式获取到一条消息: {}", getResponse);
                if (!Objects.isNull(getResponse)) {
                    channel.basicAck(getResponse.getEnvelope().getDeliveryTag(), false);
                }
            } catch (Exception e) {
                log.error("获取消息出现异常, ", e);
            }
        }
    }
}

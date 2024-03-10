package org.hulei.springboot.listener;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.demo.commom.core.model.ConsumerStatus;
import com.hundsun.demo.commom.core.model.MQIdempotency;
import com.hundsun.demo.commom.core.model.RabbitmqLogDO;
import com.hundsun.demo.java.mq.rabbit.config.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.mapper.MQIdempotencyMapper;
import org.hulei.springboot.mapper.RabbitmqLogMapper;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.listener
 * @className: RabbiMQListener
 * @description:
 * @author: h1123
 * @createDate: 2023/3/11 0:28
 */

@Component
@Slf4j
public class RabbiMQListener {

    /**
     * 正在消费
     */
    public static final String CONSUME_STATUS_CONSUMING = "CONSUMING";
    /**
     * 消费完成
     */
    public static final String CONSUME_STATUS_CONSUMED = "CONSUMED";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    MQIdempotencyMapper mqIdempotencyMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RabbitmqLogMapper rabbitmqLogMapper;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_MASTER_QUEUE,
                    durable = "true",
                    autoDelete = "false",
                    exclusive = "false",
                    arguments = {@Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)}
            ),
            exchange = @Exchange(
                    value = MQConfig.TOPIC_EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    autoDelete = "false",
                    durable = "true"),
            key = MQConfig.TOPIC_MASTER_ROUTE_KEY
    ))

    public void receiveMsg(Message msg, Channel channel) {

        /*
        保证消息幂等性
        1. 采用唯一ID+数据库主键
        2. 利用 redis来实现幂等
         */
        log.info("收到消息: {}", msg);
        MQIdempotency validation = JSONObject.parseObject(new String(msg.getBody(), StandardCharsets.UTF_8), MQIdempotency.class);
        // handleMsgByMysql(validation);
        // ConsumerStatus status = handleMsgByMysqlOptimize(validation);
        log.info("转换后的消息为: {}", validation);
        ConsumerStatus status = handleMsgByRedis(validation);
        boolean isAck = false;

        if (status.isFinished()) {
            // 如果消息已经被消费完成了, 那么直接应答
            log.info("消息已被消费! ");
            isAck = true;
        } else {
            // 消息没有被消费完成过, 这里拿到消费权
            if (status.isEnableConsumer()) {
                // 处理业务逻辑
                // 模拟一个比较长时间的消息处理时间
                log.info("开始执行业务逻辑...");
                try {

                    RabbitmqLogDO logDO = new RabbitmqLogDO();
                    logDO.setUuid(validation.getUuid());
                    logDO.setMsg(validation.getMsg());
                    rabbitmqLogMapper.insertSelective(logDO);

                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("业务执行完成, 开始更新消息表的消息消费状态...");
                // 消息处理完毕之后, 要更新消息表的消息处理状态
                // finishConsumerByMysql(validation);
                finishConsumerByRedis(validation);
                log.info("消息消费状态已更新! ");
                isAck = true;
            }
            // 消息没有被消费完成, 但是当前有其他服务正在消费, 可以拒绝消息
        }

        try {
            if (isAck) {
                log.info("当前消息消费已经被成功消费! 准备应答... ");
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
                log.info("应答完成!");
            } else {
                // 当前有消费者正在处理消息
                log.info("当前有其他消费者正在消费这个消息! 将于5秒后拒绝消息...");
                Thread.sleep(5 * 1000);
                channel.basicReject(msg.getMessageProperties().getDeliveryTag(), true);
            }
        } catch (Exception e) {
            log.error("应答失败!", e);
        }
    }

    /**
     * 结束消费
     *
     * @param validation
     */
    private void finishConsumerByMysql(MQIdempotency validation) {
        validation.setStatus(CONSUME_STATUS_CONSUMED);
        mqIdempotencyMapper.updateByTime(validation);
    }

    /**
     * 结束消费
     *
     * @param validation
     */
    private void finishConsumerByRedis(MQIdempotency validation) {
        stringRedisTemplate.opsForValue().set(validation.getUuid(), CONSUME_STATUS_CONSUMED);
    }

    /**
     * 利用 redis 原子性来确保重复性消费
     *
     * @param validation 消息
     */
    private ConsumerStatus handleMsgByRedis(MQIdempotency validation) {

        boolean flag = Boolean.TRUE.equals(
                stringRedisTemplate.execute(
                        (RedisCallback<Boolean>) connection -> connection.set(
                                validation.getUuid().getBytes(),
                                CONSUME_STATUS_CONSUMING.getBytes(),
                                Expiration.from(60, TimeUnit.SECONDS),
                                RedisStringCommands.SetOption.ifAbsent())));

        if (flag) {
            log.info("当前可以进行消费! ");
            return new ConsumerStatus(true, false);
        } else {
            // 查看消息的状态
            if (CONSUME_STATUS_CONSUMED.equals(stringRedisTemplate.opsForValue().get(validation.getUuid()))) {
                log.info("当前消息已被成功消费了!");
                return new ConsumerStatus(false, true);
            } else {
                log.info("当前有其他服务正在消费...");
                return new ConsumerStatus(false, false);
            }
        }
    }


    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_SLAVE_QUEUE,
                    durable = "true",
                    autoDelete = "false"
                    // arguments = {@Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)}),
            ),
            exchange = @Exchange(
                    value = MQConfig.TOPIC_EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    autoDelete = "false",
                    durable = "true"),
            key = {MQConfig.TOPIC_SLAVE_ROUTE_KEY, MQConfig.FANOUT_SLAVE_ROUTE_KEY}
    ))
    public void receiveMsg2(Message msg, Channel channel) {
        log.info("im receive " + msg + "too");
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("应答失败!", e);
        }

    }
}

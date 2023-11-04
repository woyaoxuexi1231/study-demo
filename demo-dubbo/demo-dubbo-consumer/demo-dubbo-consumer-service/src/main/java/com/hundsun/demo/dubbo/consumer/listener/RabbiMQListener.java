package com.hundsun.demo.dubbo.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.demo.dubbo.consumer.mapper.MQIdempotencyMapper;
import com.hundsun.demo.dubbo.provider.api.model.pojo.MQIdempotency;
import com.hundsun.demo.java.mq.rabbit.config.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_MASTER_QUEUE,
                    durable = "true",
                    autoDelete = "false"
                    // arguments = {@Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)}),
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
                    Thread.sleep(30 * 1000);
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
     * 利用唯一 ID + mysql主键来控制重复消费的问题, 此方法在某些场景下依旧存在重复消费问题
     *
     * @param validation 消息
     */
    private void handleMsgByMysql(MQIdempotency validation) {
        /*
        todo 这里是否存在问题?
        1. 上游发了两条一模一样的消息, 依旧会导致重复消费
        2. 第一个消费者在消费过程中, 断开了与 mq的连接, 也依旧会导致重复消费
         */
        MQIdempotency idempotency = mqIdempotencyMapper.selectByPrimaryKey(validation);
        if (idempotency == null) {
            // 执行业务逻辑
            log.info("开始执行业务逻辑...收到的消息为: {}", validation);
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 执行完业务逻辑添加唯一ID作为已消费的证据
            mqIdempotencyMapper.insert(validation);
        } else {
            log.info("消息已被消费! ");
        }
    }

    /**
     * 利用唯一 ID + mysql主键来控制重复消费的问题, 优化可以避免重复消费问题
     *
     * @param validation 消息
     */
    private ConsumerStatus handleMsgByMysqlOptimize(MQIdempotency validation) {
        /*
        这种方法, 需要自己处理主键冲突的逻辑
        1. 为消息表设置一个消息的插入时间, 并且在业务上自定义消息的过期时间
         */

        // 创建一个消息的过期时间, 在这个时间点之前的消息都应该是执行超时了
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
        String nowTime = df.format(now);
        Timestamp explore = new Timestamp(System.currentTimeMillis() - (60 * 1000));
        String exploreTime = df.format(explore);

        try {
            validation.setTime(nowTime);
            validation.setStatus(CONSUME_STATUS_CONSUMING);
            int i = mqIdempotencyMapper.insertSelective(validation);
        } catch (DuplicateKeyException e) {

            // 主键冲突报错, 进行延迟消费, 假设消息的过期时间是 十分钟
            // 首先去查看这个消息的消费状态
            MQIdempotency mqIdempotency = mqIdempotencyMapper.selectByPrimaryKey(validation);
            // 如果有其他消费者正在消费这个消息
            if (CONSUME_STATUS_CONSUMING.equals(mqIdempotency.getStatus())) {
                // 1. 判断消息是否过期
                validation.setStatus(CONSUME_STATUS_CONSUMING);
                validation.setTime(exploreTime);
                /*
                todo 这里是否依旧存在问题? 如果就是另一台机器单纯的慢呢? 正好执行了十分零一秒, 那是否意味着依旧存在重复消费的问题?
                感觉就像又回到最初的问题, 感觉没办法避免,
                 */
                int i = mqIdempotencyMapper.deleteByTime(validation);
                if (i > 0) {
                    // 说明消费已经超时了, 可以进行这一次的消费了
                    log.info("上次消费已超时, 准备重新消费消息! ");
                    validation.setTime(nowTime);
                    mqIdempotencyMapper.insertSelective(validation);
                    return new ConsumerStatus(true, false);
                } else {
                    // 消费没有超时, 仍在消费中, 把这个信息传递出去, 具体怎么处理看情况
                    log.info("上次消费仍在进行中...");
                    return new ConsumerStatus(false, false);
                }
            }
            // 如果这个消息已经被成功消费过了, 就不再次消费了
            if (CONSUME_STATUS_CONSUMED.equals(mqIdempotency.getStatus())) {
                return new ConsumerStatus(false, true);
            }
        }

        // 插入数据成功意味着, 当前服务拿到消费权就正常消费
        return new ConsumerStatus(true, false);
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

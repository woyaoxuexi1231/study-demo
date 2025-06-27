package org.hulei.springboot.rabbitmq.spring.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.Message;
import org.hulei.springboot.rabbitmq.model.ConsumerStatus;
import org.hulei.springboot.rabbitmq.spring.consumer.mapper.MQIdempotencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/9/17 23:04
 */

@Slf4j
@RestController
@RequestMapping("/rabbitmq_duplicated_consumer")
public class DuplicatedConsume {

    /*============================================== 消息重复消费问题 ===========================================*/

    /**
     * 正在消费
     */
    public static final String CONSUMING = "CONSUMING";
    /**
     * 消费完成
     */
    public static final String CONSUMED = "CONSUMED";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    MQIdempotencyMapper mqIdempotencyMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 保证消息不被重复消费(即最多消费一次,且成功消费一次) <br>
     * 1. 采用唯一ID+数据库主键 <br>
     * 2. 利用 redis来实现幂等 <br>
     *
     * @param msg     msg
     * @param channel 信道
     */
    private void repeatConsumer(org.springframework.amqp.core.Message msg, Channel channel) {
        log.info("接收到消息 msg: {}", msg);
        Message validation = JSONObject.parseObject(new String(msg.getBody(), StandardCharsets.UTF_8), Message.class);
        ConsumerStatus status = msgConsumableByRedis(validation);
        // ConsumerStatus status = msgConsumableByMysql(validation);

        // 记录是否成功应答
        boolean isAck = false;

        if (status.isFinished()) {
            // 如果消息已经被消费完成了, 那么直接应答
            log.info("消息已经被其他线程消费,不再处理");
            isAck = true;
        } else {
            if (status.isEnableConsumer()) {
                // 处理业务逻辑
                // 模拟一个比较长时间的消息处理时间
                log.info("获得消费权,开始消费消息(五秒后执行完成)");
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 消息处理完毕之后, 要更新消息表的消息处理状态
                // finishConsumerByMysql(validation);
                finishConsumerByRedis(validation);
                log.info("执行结束");
                isAck = true;
            }
            // 消息没有被消费完成, 但是当前有其他服务正在消费, 可以拒绝消息
        }

        try {
            if (isAck) {
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
                log.info("ack成功");
            } else {
                // 当前有消费者正在处理消息
                // 是重新入列呢,还是直接抛弃这个消息呢? 入列是否已经没有必要了? 2024年4月14日 看具体的业务场景吧.
                log.info("当前有重复的消息正在被其他线程消费,将延迟5秒进行拒绝消息,以重新入列");
                Thread.sleep(5 * 1000);
                channel.basicReject(msg.getMessageProperties().getDeliveryTag(), true);
            }
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    /**
     * 利用唯一 ID + mysql主键来控制重复消费的问题, 优化可以避免重复消费问题
     *
     * @param validation 消息
     */
    private ConsumerStatus msgConsumableByMysql(Message validation) {

        // 创建一个消息的过期时间, 如果状态是 正在消费中&时间过期 那代表
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义格式，不显示毫秒
        Timestamp now = new Timestamp(System.currentTimeMillis()); // 获取系统当前时间
        String nowTime = df.format(now);
        Timestamp explore = new Timestamp(System.currentTimeMillis() - (60 * 1000)); // 60秒超时时间(根据业务执行时间来定)
        String exploreTime = df.format(explore);

        try {
            validation.setTime(nowTime);
            validation.setStatus(CONSUMING);
            int i = mqIdempotencyMapper.insert(validation);
        } catch (DuplicateKeyException e) {

            // 主键冲突报错, 进行延迟消费, 假设消息的过期时间是 十分钟
            // 首先去查看这个消息的消费状态
            Message message = mqIdempotencyMapper.selectById(validation.getUuid());
            // 如果有其他消费者正在消费这个消息
            if (CONSUMING.equals(message.getStatus())) {
                // 1. 判断消息是否过期
                validation.setStatus(CONSUMING);
                validation.setTime(exploreTime);
                /*
                这里是否依旧存在问题? 如果就是另一台机器单纯的慢呢? 正好执行了十分零一秒, 那是否意味着依旧存在重复消费的问题?
                这里要引入过期时间的更新机制，正在消费的线程如果由于处理时间过于长则需要整个过程中不断的调整过期时间
                 */
                // 删除过期时间之外的消息
                int i = mqIdempotencyMapper.deleteByTime(validation);
                if (i > 0) {
                    // 说明消费已经超时了, 可以进行这一次的消费了
                    log.info("上一次消费已超时,准备重新消费消息");
                    // 插入一个新的时间
                    validation.setTime(nowTime);
                    // 重新再插入一个新的
                    mqIdempotencyMapper.insert(validation);
                    return new ConsumerStatus(true, false);
                } else {
                    // 消费没有超时, 仍在消费中, 把这个信息传递出去, 具体怎么处理看情况
                    log.info("目前有其他线程正在消费中(不保证线程已经宕机,消费还没过期)");
                    return new ConsumerStatus(false, false);
                }
            }
            // 如果这个消息已经被成功消费过了, 就不再次消费(相较于)
            if (CONSUMED.equals(message.getStatus())) {
                return new ConsumerStatus(false, true);
            }
        }

        // 插入数据成功意味着, 当前服务拿到消费权就正常消费
        return new ConsumerStatus(true, false);
    }

    /**
     * 结束消费
     *
     * @param validation msg
     */
    private void finishConsumerByMysql(Message validation) {
        validation.setStatus(CONSUMED);
        mqIdempotencyMapper.updateByTime(validation);
    }

    /**
     * 利用 redis 原子性来确保重复性消费 - 判断消息是否可以消费
     *
     * @param validation 消息
     */
    private ConsumerStatus msgConsumableByRedis(Message validation) {

        // TODO 这里应该设置成锁的结果，而不应该是这个键值对
        // 执行设置键值对操作的结果 (false表示设置不成功[有其他线程在消费], true表示当前线程可以消费)
        boolean flag = Boolean.TRUE.equals(
                stringRedisTemplate.execute( // 执行一个Redis操作的通用方法
                        (RedisCallback<Boolean>) connection -> connection.set( // RedisCallback允许直接访问底层的Redis连接，执行更复杂或者是原生的Redis命令。
                                validation.getUuid().getBytes(), // 获取消息的 UUID (同时也是接下来要设置的 redis的<key,value> 的 value)
                                CONSUMING.getBytes(), // 设置当前 UUID 的这个消息的状态为正在消费中 (同时也是接下来要设置的 redis的<key,value> 的 value)
                                Expiration.from(60, TimeUnit.SECONDS), // 设置当前要设置的键值对的过期时间, 可能某个消费者消费途中宕机了, 那么这个状态需要过期, 以便后续其他线程能够正常消费
                                RedisStringCommands.SetOption.ifAbsent() // 设置条件。这个选项意味着只有当键不存在时，才会设置键值对。
                        )));

        if (flag) {
            log.info("当前可以消费消息");
            return new ConsumerStatus(true, false);
        } else {
            // 查看消息的状态(可能会存在消息生产方重复生产消息了), 所以已经成功消费的消息要记录下来, 然后以备校验
            if (CONSUMED.equals(stringRedisTemplate.opsForValue().get(validation.getUuid()))) {
                log.info("当前消息已被成功消费");
                return new ConsumerStatus(false, true);
            } else {
                // 消息并没有被消费完成, 但是消息正在被其他服务消费
                log.info("当前有其他服务正在消费此消息");
                return new ConsumerStatus(false, false);
            }
        }
    }

    /**
     * 利用 redis 原子性来确保重复性消费 - 结束消费,更新redis的键值
     *
     * @param validation msg
     */
    private void finishConsumerByRedis(Message validation) {
        // 没有设置过期时间, 保存消息消费已经被消费的这个状态
        stringRedisTemplate.opsForValue().set(validation.getUuid(), CONSUMED);
    }
}

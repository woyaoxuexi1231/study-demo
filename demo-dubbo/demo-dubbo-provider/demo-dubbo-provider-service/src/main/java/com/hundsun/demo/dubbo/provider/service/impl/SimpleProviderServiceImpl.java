package com.hundsun.demo.dubbo.provider.service.impl;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.provider.api.service.SimpleProviderService;
import com.hundsun.demo.java.mq.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:15
 */
@DubboService
@Slf4j
public class SimpleProviderServiceImpl implements SimpleProviderService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ResultDTO<?> RpcSimpleInvoke() {
        return ResultDTOBuild.resultSuccessBuild("hello rpc!");
    }

    /**
     *
     */
    private static final ThreadPoolExecutor RABBITMQ_POOL = new ThreadPoolExecutor(5, 20, 20,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(150), new ThreadFactoryBuilder()
            .setNamePrefix("RedissonServiceImpl-thread-").build(), new ThreadPoolExecutor.CallerRunsPolicy());

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_MASTER_QUEUE,
                    durable = "false",
                    autoDelete = "true"
                    // arguments = {@Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)}),
            ),
            exchange = @Exchange(
                    value = MQConfig.TOPIC_EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    autoDelete = "true",
                    durable = "false"),
            key = MQConfig.TOPIC_MASTER_ROUTE_KEY
    ))
    public void receiveMsg(String msg) {
        log.info("im receive " + msg);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = MQConfig.TOPIC_SLAVE_QUEUE,
                    durable = "false",
                    autoDelete = "true"
                    // arguments = {@Argument(name = "x-dead-letter-exchange", value = MQConfig.DEAD_EXCHANGE_NAME)}),
            ),
            exchange = @Exchange(
                    value = MQConfig.TOPIC_EXCHANGE_NAME,
                    type = ExchangeTypes.TOPIC,
                    autoDelete = "true",
                    durable = "false"),
            key = MQConfig.TOPIC_SLAVE_ROUTE_KEY
    ))
    public void receiveMsg2(String msg) {
        log.info("im receive " + msg + "too");
    }
}

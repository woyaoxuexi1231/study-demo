package com.hundsun.demo.dubbo.consumer.config;

import com.hundsun.demo.java.mq.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-27 14:39
 */

@Configuration
@Slf4j
public class InitConfig implements ApplicationRunner {

    @Autowired
    CuratorFramework curatorFramework;

    @Value("${dubbo.application.name}")
    String applicationName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {

            if (curatorFramework.checkExists().forPath("/demo") == null) {
                curatorFramework.create().forPath("/demo", "项目信息".getBytes(StandardCharsets.UTF_8));
            }

            if (curatorFramework.checkExists().forPath("/demo/" + applicationName) != null) {
                curatorFramework.delete().deletingChildrenIfNeeded().forPath("/demo/" + applicationName);
            }
            curatorFramework.create().forPath("/demo/" + applicationName, ("项目名称 : " + applicationName).getBytes(StandardCharsets.UTF_8));
            log.info("添加项目信息成功! ");

        } catch (Exception e) {
            log.error("添加项目信息失败! ", e);
        }
    }

}

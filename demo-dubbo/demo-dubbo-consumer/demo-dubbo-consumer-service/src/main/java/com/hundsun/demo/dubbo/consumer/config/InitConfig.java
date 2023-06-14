package com.hundsun.demo.dubbo.consumer.config;

import com.hundsun.demo.java.mq.rabbit.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

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

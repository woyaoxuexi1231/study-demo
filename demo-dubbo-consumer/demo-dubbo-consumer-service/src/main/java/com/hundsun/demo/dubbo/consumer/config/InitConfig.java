package com.hundsun.demo.dubbo.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-27 14:39
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Component
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
            curatorFramework.create().forPath("/demo/" + applicationName, ("项目名:" + applicationName).getBytes(StandardCharsets.UTF_8));
            log.info("项目信息录入zk正常");

        } catch (Exception e) {
            log.error("项目信息录入zk异常!", e);
        }
    }
}

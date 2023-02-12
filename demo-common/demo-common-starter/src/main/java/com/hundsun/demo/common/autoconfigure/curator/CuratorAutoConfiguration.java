package com.hundsun.demo.common.autoconfigure.curator;

import com.hundsun.demo.common.curator.config.CuratorConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.duubo.common.curator.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-27 13:39
 */
@Configuration
@EnableConfigurationProperties({CuratorConfiguration.class})
public class CuratorAutoConfiguration {

    /**
     * curator配置服务
     */
    @Autowired
    CuratorConfiguration curatorConfig;

    @Bean(initMethod = "start")
    @ConditionalOnMissingBean
    public CuratorFramework curatorFramework() {

        return CuratorFrameworkFactory.newClient(
                curatorConfig.getConnectString(),
                Integer.parseInt(curatorConfig.getSessionTimeoutMs()),
                Integer.parseInt(curatorConfig.getConnectionTimeoutMs()),
                new ExponentialBackoffRetry(Integer.parseInt(curatorConfig.getElapsedTimeMs()), Integer.parseInt(curatorConfig.getRetryCount())));
    }

}

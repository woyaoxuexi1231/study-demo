package com.hundsun.demo.common.autoconfigure.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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

@Slf4j
@Configuration
@ConditionalOnClass(value = CuratorFramework.class)
@EnableConfigurationProperties({CuratorConfiguration.class})
public class CuratorAutoConfiguration {

    /**
     * curator配置服务
     */
    @Autowired
    CuratorConfiguration curatorConfig;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {

        /*
        connectString – list of servers to connect to
        sessionTimeoutMs – session timeout
        connectionTimeoutMs – connection timeout
        retryPolicy – retry policy to use
            1. ExponentialBackoffRetry
                * 随着重试次数增加重试时间间隔变大, 指数倍增长 baseSleepTimeMs * Math.max(1, random.nextInt(1 << (retryCount + 1)))
                * 有两个构造方法
                * baseSleepTimeMs 初始 sleep 时间
                * maxRetries 最多重试几次
                * maxSleepMs 最大的重试时间
                * 如果在最大重试次数内, 根据公式计算出的睡眠时间超过了 maxSleepMs, 将打印 warn 级别日志, 并使用 maxSleepMs time
                * 如果不指定 maxSleepMs, 那么 maxSleepMs 的默认值为 Integer.MAX_VALUE
            2. BoundedExponentialBackoffRetry
                * 继承与 ExponentialBackoffRetry, BoundedExponentialBackoffRetry 只有一个三个参数构造器, 效果跟 ExponentialBackoffRetry 三个函数构造器是一样的, 只是内部实现不一样
                * baseSleepTimeMs 初始 sleep 时间
                * maxSleepTimeMs 最大 sleep 时间
                * maxRetries 最大重试次数
            3. RetryForever
                * RetryForever 永远重试策略
                * retryIntervalMs 重试时间间隔
            4. RetryOneTime
                * RetryOneTime 只重试一次
                * sleepMsBetweenRetry: 每次重试间隔的时间
            5. RetryUntilElapsed
                * RetryUntilElapsed 一直重试，直到超过指定时间
                * maxElapsedTimeMs 最大重试时间
                * sleepMsBetweenRetries 每次重试间隔时间
         */
        log.info("CuratorFrameworkFactory准备连接, 地址: {}", curatorConfig.getConnectString());

        return CuratorFrameworkFactory.newClient(
                curatorConfig.getConnectString(),
                Integer.parseInt(curatorConfig.getSessionTimeoutMs()),
                Integer.parseInt(curatorConfig.getConnectionTimeoutMs()),
                new ExponentialBackoffRetry(Integer.parseInt(curatorConfig.getElapsedTimeMs()), Integer.parseInt(curatorConfig.getRetryCount())));
    }

}

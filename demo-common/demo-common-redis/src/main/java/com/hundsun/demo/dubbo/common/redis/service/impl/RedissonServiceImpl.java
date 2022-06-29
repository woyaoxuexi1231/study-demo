package com.hundsun.demo.dubbo.common.redis.service.impl;

import com.hundsun.demo.dubbo.common.redis.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-30 16:11
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@Service
public class RedissonServiceImpl implements RedissonService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95
     */
    /**
     * 程序化配置RedissonClient
     *
     * @return
     */
    public RedissonClient getRedissonClient() {

        Config config = new Config();
        config.setTransportMode(TransportMode.EPOLL);
        config.useClusterServers().addNodeAddress("192.168.142.131:6379");

        return Redisson.create(config);
    }

    /**
     * 通过YAML文件配置RedissonClient
     *
     * @return
     */
    public RedissonClient getRedissonClientYAML() {

        try {
            Config config = Config.fromYAML(new File(""));
            return Redisson.create();
        } catch (Exception e) {
            throw new RuntimeException("读取配置文件失败");
        }

    }

    /**
     * 程序化集群配置RedissonClient
     *
     * @return
     */
    public RedissonClient getRedissonClientCluster() {
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000)
                .addNodeAddress("192.168.142.131:6379")
                .addNodeAddress("192.168.142.130:6379")
                .addNodeAddress("192.168.142.132:6379");

        return Redisson.create(config);
    }

    /**
     * 程序化哨兵配置RedissonClient
     *
     * @return
     */
    public RedissonClient getRedissonClientSentinel() {

        Config config = new Config();
        config.useSentinelServers()
                .setMasterName("mymaster")
                .addSentinelAddress("redis://192.168.142.131:26379")
                .addSentinelAddress("redis://192.168.142.130:26379", "redis://192.168.142.132:26379")
                .setPassword("123456");

        return Redisson.create(config);
    }

    /**
     * 主从模式配置RedissonClient
     *
     * @return
     */
    public RedissonClient getRedissonClientMasterSlave() {

        Config config = new Config();
        config.useMasterSlaveServers()
                //可以用"rediss://"来启用SSL连接
                .setMasterAddress("redis://127.0.0.1:6379")
                .addSlaveAddress("redis://127.0.0.1:6389", "redis://127.0.0.1:6332", "redis://127.0.0.1:6419")
                .addSlaveAddress("redis://127.0.0.1:6399");

        return Redisson.create(config);
    }

}

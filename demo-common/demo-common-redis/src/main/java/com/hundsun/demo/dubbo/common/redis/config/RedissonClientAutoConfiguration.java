package com.hundsun.demo.dubbo.common.redis.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-01 15:48
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
@EnableConfigurationProperties({RedisProperties.class, RedissonClientConfiguration.class})
public class RedissonClientAutoConfiguration {

    @Autowired
    RedissonClientConfiguration configuration;

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {

        Config config = new Config();
        String master = configuration.getMaster();
        String nodes = configuration.getNodes();
        String host = configuration.getHost();
        String password = configuration.getPassword();

        if (StringUtils.isNotBlank(master) && StringUtils.isNotBlank(nodes)) {
            config.useSentinelServers().setMasterName(master);

            String[] address = nodes.split(",");
            for (String add : address) {
                config.useSentinelServers().addSentinelAddress("redis://" + add);
            }
            config.useSentinelServers().setPassword(password);
            return Redisson.create(config);
        }
        return null;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateSO(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}

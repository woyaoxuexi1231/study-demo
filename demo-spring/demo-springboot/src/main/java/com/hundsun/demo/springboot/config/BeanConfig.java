package com.hundsun.demo.springboot.config;

import com.hundsun.demo.commom.core.aop.DoneTimeAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.client.RestTemplate;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/12 2:12
 */
@Configuration
public class BeanConfig {

    @Bean
    public DoneTimeAspect doneTimeAspect() {
        return new DoneTimeAspect();
    }

    // @Bean
    // public ServletListenerRegistrationBean servletListenerRegistrationBean(SimpleHttpSessionListener simpleHttpSessionListener) {
    //     ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
    //     servletListenerRegistrationBean.setListener(simpleHttpSessionListener);
    //     return servletListenerRegistrationBean;
    // }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 设置 redisTemplate 的编码
     *
     * @param redisTemplate
     */
    @Autowired
    public void redisTemplateSerializerInit(RedisTemplate<Object, Object> redisTemplate) {
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
    }

    @Autowired
    public void initRedisTemplate(RedisTemplate redisTemplate) {
        // redisTemplate.setKeySerializer(RedisSerializer.json());
        redisTemplate.setKeySerializer(RedisSerializer.string());
    }

}

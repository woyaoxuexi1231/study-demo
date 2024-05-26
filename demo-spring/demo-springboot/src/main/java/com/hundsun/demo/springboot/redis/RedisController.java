package com.hundsun.demo.springboot.redis;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.common.model.req.EmployeeQryReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: RedisController
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-23 09:56
 */

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @GetMapping("redisTemplate")
    public void redisTemplate() {
        // redisTemplate.opsForValue().set("hello", "redis");
        // Boolean map = redisTemplate.opsForHash().putIfAbsent("map", "1", UUID.randomUUID().toString());
        // Boolean map = StringRedisTemplate.opsForHash().putIfAbsent("map", "1", UUID.randomUUID().toString());
        // System.out.println(map);
        stringRedisTemplate.opsForValue().set("hello", "redis");

        stringRedisTemplate.opsForList().leftPush("hello-list", "no1");
    }

    @Autowired
    EmployeeMapper employeeMapper;

    public static final String key = "employees::getEmployees";

    @Cacheable(value = key, cacheManager = "cacheManager")
    @PostMapping(value = "/getEmployees")
    public PageInfo<EmployeeDO> getEmployees() {
        // @Valid @RequestBody EmployeeQryReqDTO req
        // PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return new PageInfo<>(employeeMapper.selectAll());
    }

    @CacheEvict(value = key, allEntries = true)
    @PostMapping("/addOneEmployee")
    public void addOneEmployee() {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(new Date().getTime());
        employeeDO.setLastName(JMockData.mock(String.class));
        employeeDO.setFirstName(JMockData.mock(String.class));
        employeeDO.setExtension(JMockData.mock(String.class));
        employeeDO.setEmail(JMockData.mock(String.class));
        employeeDO.setOfficeCode(JMockData.mock(String.class));
        employeeDO.setReportsTo(JMockData.mock(Integer.class));
        employeeDO.setJobTitle(JMockData.mock(String.class));
        employeeMapper.insertSelective(employeeDO);
    }
}

@Configuration
@EnableCaching
class CacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> {
            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
            cacheConfigurations.put(RedisController.key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
            // Add more cache configurations if needed

            builder.withInitialCacheConfigurations(cacheConfigurations);
        };
    }


    @Bean
    @Override
    public CacheManager cacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory)
                // .withCacheConfiguration(RedisController.key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                // If you have a customizer bean, you don't need this line, just ensure your customizer is called
                .build();
    }

    // 这是一个本地缓存
    // @Bean
    // public CacheManager cacheManager() {
    //     return new ConcurrentMapCacheManager("myCache");
    // }
}

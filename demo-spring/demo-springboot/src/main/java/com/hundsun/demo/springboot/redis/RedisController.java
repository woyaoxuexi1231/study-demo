package com.hundsun.demo.springboot.redis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.pagehelper.PageInfo;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.mybatisplus.mapper.EmployeeMapperPlus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: RedisController
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-23 09:56
 */

@EnableCaching
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

    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    // public static final String key = "employees::getEmployees";
    public static final String key = "employees";

    @Cacheable(value = key, key = "#employeeNumber")
    @GetMapping(value = "/getEmployees")
    public PageInfo<EmployeeDO> getEmployees(Long employeeNumber) {
        // @Valid @RequestBody EmployeeQryReqDTO req
        // PageHelper.startPage(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<EmployeeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeDO::getEmployeeNumber, employeeNumber);
        return new PageInfo<>(employeeMapperPlus.selectList(wrapper));
    }

    // @CacheEvict(value = key, allEntries = true)
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

    @CachePut(value = key, key = "#req.getEmployeeNumber()")
    @PostMapping("/updateEmployee")
    public EmployeeDO updateEmployee(@RequestBody EmployeeDO req) {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(req.getEmployeeNumber());
        employeeDO.setEmployeeNumber(new Date().getTime());
        employeeDO.setLastName(JMockData.mock(String.class));
        employeeDO.setFirstName(JMockData.mock(String.class));
        employeeDO.setExtension(JMockData.mock(String.class));
        employeeDO.setEmail(JMockData.mock(String.class));
        employeeDO.setOfficeCode(JMockData.mock(String.class));
        employeeDO.setReportsTo(JMockData.mock(Integer.class));
        employeeDO.setJobTitle(JMockData.mock(String.class));
        employeeMapper.updateByPrimaryKey(employeeDO);
        return employeeDO;
    }
}
//
// @Configuration
// @EnableCaching
// class CacheConfig extends CachingConfigurerSupport {
//
//     @Autowired
//     private RedisConnectionFactory redisConnectionFactory;
//
//     @Bean
//     @Override
//     public CacheManager cacheManager() {
//         return RedisCacheManager.builder(redisConnectionFactory)
//                 // .withCacheConfiguration(RedisController.key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
//                 // If you have a customizer bean, you don't need this line, just ensure your customizer is called
//                 .build();
//     }
//
//
//     @Bean
//     public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//         return builder -> {
//             Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//             cacheConfigurations.put(RedisController.key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
//             // Add more cache configurations if needed
//
//             builder.withInitialCacheConfigurations(cacheConfigurations);
//         };
//     }
//
//
//     // 这是一个本地缓存
//     // @Bean
//     // public CacheManager cacheManager() {
//     //     return new ConcurrentMapCacheManager("myCache");
//     // }
// }


@Configuration
class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(
                new ConcurrentMapCache(RedisController.key)
                // 其他缓存...
        ));
        return cacheManager;
    }
}


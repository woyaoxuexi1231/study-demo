package com.hundsun.demo.springboot.redis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.jsonzou.jmockdata.JMockData;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.SpringbootApplication;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.mybatisplus.mapper.EmployeeMapperPlus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: RedisController
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-23 09:56
 */

@Configuration
@EnableCaching
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    /*========================================== 配置相关 =======================================*/

    /**
     * 定义一个默认的缓存管理器,spring需要一个作为默认
     *
     * @return defaultManager
     */
    @Primary
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(
                // 本地内存缓存
                new ConcurrentMapCache("default")
        ));
        return cacheManager;
    }

    @Bean
    public CacheManager localCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(
                // 本地内存缓存
                new ConcurrentMapCache(RedisController.local)
        ));
        return cacheManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // 设置缓存过期时间
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(redisCacheWriter)
                .withCacheConfiguration(RedisController.redis, defaultCacheConfig)
                // .cacheDefaults(defaultCacheConfig)
                .build();
    }


    /*========================================== 事务相关 =======================================*/

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Qualifier(value = "strObjRedisTemplate")
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @GetMapping("redisTemplate")
    public void redisTemplate() {
        // redisTemplate.opsForValue().set("hello", "redis");
        // Boolean map = redisTemplate.opsForHash().putIfAbsent("map", "1", UUID.randomUUID().toString());
        // Boolean map = StringRedisTemplate.opsForHash().putIfAbsent("map", "1", UUID.randomUUID().toString());
        // System.out.println(map);
        stringRedisTemplate.opsForValue().set("hello", "redis");
        stringRedisTemplate.opsForList().leftPush("hello-list", "no1");
    }

    /**
     * 使用 RedisTemplate.execute 和 SessionCallback 来进行事务管理
     */
    @GetMapping(value = "redisTransaction")
    public void redisTransaction() {
        /*
        下面这个代码例子是不可行的,执行将会报错,这是因为这几个操作都不是在一个连接完成的。
        org.springframework.dao.InvalidDataAccessApiUsageException: Not in transaction mode. Please invoke multi method

        redisTemplate.multi();
        redisTemplate.opsForValue().set("key1", "value1");
        redisTemplate.opsForValue().set("key2", "value2");
        redisTemplate.exec();
         */

        List<Object> r = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {

            @SuppressWarnings("unchecked")
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().set("hxm", "9999");
                // 此处打印null，因为事务还没真正执行
                System.out.println(operations.opsForValue().get("hxm"));
                return operations.exec();
            }
        });

        System.out.println(r);
    }

    /**
     * 使用 template.setEnableTransactionSupport(true) 和 @Transactional 来完成事务管理
     */
    @Transactional
    @GetMapping(value = "redisTransaction2")
    public void redisTransaction2() {
        redisTemplate.opsForValue().set("hello1", "redis");
        redisTemplate.opsForValue().set("hello2", "redis");
        redisTemplate.opsForValue().set("hello3", "redis");
        throw new RuntimeException("error");
    }


    /*========================================== 缓存相关 =======================================*/

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    // public static final String key = "employees::getEmployees";
    public static final String local = "local";
    public static final String redis = "redis";

    /**
     * Cacheable: value作为缓存的命名空间(即一个前缀), key作为完整实际的键值
     * 举例: 在使用ConcurrentMapCache的时候,value作为ConcurrentMapCache的名字 key作为这个cache内部map的key
     *
     * @param employeeNumber id
     * @return object
     */
    @Cacheable(value = {local}, key = "#employeeNumber", cacheManager = "localCacheManager")
    @GetMapping(value = "/getEmployees")
    public EmployeeDO getEmployees(Long employeeNumber) {
        // @Valid @RequestBody EmployeeQryReqDTO req
        // PageHelper.startPage(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<EmployeeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeDO::getEmployeeNumber, employeeNumber);
        List<EmployeeDO> doList = employeeMapperPlus.selectList(wrapper);
        return CollectionUtils.isEmpty(doList) ? null : doList.get(0);
    }

    // @CacheEvict(value = key, allEntries = true)
    @PostMapping("/addOneEmployee")
    public void addOneEmployee() {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(System.currentTimeMillis());
        employeeDO.setLastName(JMockData.mock(String.class));
        employeeDO.setFirstName(JMockData.mock(String.class));
        employeeDO.setExtension(JMockData.mock(String.class));
        employeeDO.setEmail(JMockData.mock(String.class));
        employeeDO.setOfficeCode(JMockData.mock(String.class));
        employeeDO.setReportsTo(JMockData.mock(Integer.class));
        employeeDO.setJobTitle(JMockData.mock(String.class));
        employeeMapper.insertSelective(employeeDO);
    }

    /**
     * CachePut: value和key与Cacheable的相同,这个会更新 value命名空间下键为key的数据
     *
     * @param req 更新信息
     * @return 修改后的对象
     */
    @CachePut(value = local, key = "#req.getEmployeeNumber()", cacheManager = "localCacheManager")
    @PostMapping("/updateEmployee")
    public EmployeeDO updateEmployee(@RequestBody EmployeeDO req) {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(req.getEmployeeNumber());
        employeeDO.setEmployeeNumber(System.currentTimeMillis());
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

    @SneakyThrows
    @GetMapping("/getCache")
    public void getCache() {
        CacheManager cacheManager = SpringbootApplication.applicationContext.getBean(CacheManager.class);
        for (String cacheName : cacheManager.getCacheNames()) {
            if (RedisController.local.equals(cacheName)) {
                ConcurrentMapCache cache = (ConcurrentMapCache) cacheManager.getCache(cacheName);
                Field store = ConcurrentMapCache.class.getDeclaredField("store");
                store.setAccessible(true);
                ConcurrentMap<?, ?> map = (ConcurrentMap<?, ?>) store.get(cache);
                map.forEach((k, v) -> {
                    log.info("k:{}, v:{}", k, v);
                });
            }
        }
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
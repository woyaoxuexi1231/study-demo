package org.hulei.keeping.server.redis;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.jsonzou.jmockdata.JMockData;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import org.hulei.keeping.server.KeepingApplication;
import org.hulei.keeping.server.common.mapper.EmployeeMapper;
import org.hulei.keeping.server.mybatisplus.mapper.EmployeeMapperPlus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
                new ConcurrentMapCache("default")));
        return cacheManager;
    }

    @Bean
    public CacheManager localCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(
                // 本地内存缓存
                new ConcurrentMapCache(RedisController.local)));
        return cacheManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(60)) // 设置缓存过期时间
                .disableCachingNullValues().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(redisCacheWriter).withCacheConfiguration(RedisController.redis, defaultCacheConfig)
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
    @PostMapping(value = "/getEmployees")
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
        CacheManager cacheManager = KeepingApplication.applicationContext.getBean(CacheManager.class);
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







    /*========================================== redis的基本使用 =======================================*/

    /**
     * 使用redis集合获得集合交集
     *
     * @return
     */
    @RequestMapping("/redisForList")
    public String redisForList() {

        // 对于用户 Aurelia 来说,这算是他的关注列表,他一共关注了四个人
        redisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");

        // 对于用户 Nou 来说,这算是他的关注列表,他也关注了四个人
        redisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");

        // 现在的问题是,要求这两个人的共同关注,这其实redis有提供这相关api可以使用
        Set<Object> intersect = redisTemplate.opsForSet().intersect("Aurelia", "Nou");


        // 扩展一下
        // 1.求并集
        Set<Object> union = redisTemplate.opsForSet().union("Aurelia", "Nou");
        // 2.求差集
        Set<Object> difference = redisTemplate.opsForSet().difference("Aurelia", "Nou");

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("intersect", CollectionUtils.isEmpty(intersect) ? "null" : intersect.toString());
        resultMap.put("union", CollectionUtils.isEmpty(union) ? "null" : union.toString());
        resultMap.put("difference", CollectionUtils.isEmpty(difference) ? "null" : difference.toString());
        return JSONObject.toJSONString(resultMap);
    }


    @GetMapping("/redisForString")
    public void redisForString() {
        // 对于字符串, redis提供自增接口, 编号,浏览量等不记名的数量统计都可以使用这种自增来解决, 并且由于redis的自增原子性,可以作为分布式id的获取途径(一定要避免数据丢失和备份)
        redisTemplate.opsForValue().set("string-inc", 1);
        System.out.println(redisTemplate.opsForValue().increment("string-inc", 1));

        // 位图的使用, 可以作为统计用户签到行为,打卡行为
        redisTemplate.opsForValue().setBit("bit-test", 0, true);
        redisTemplate.opsForValue().setBit("bit-test", 1, false);
        System.out.println(redisTemplate.opsForValue().getBit("bit-test", 0));
        System.out.println(redisTemplate.opsForValue().getBit("bit-test", 1));
        System.out.println(redisTemplate.opsForValue().getBit("bit-test", 2));

        // {@link com.hundsun.demo.springboot.redis.msglist.MessageConsumer.run} list可以作为消息队列使用, blpop和brpop这两个操作可以阻塞的弹出元素

        // set结构,易于构建类似 记名点赞,关注列表这样的类似 一对多的结构,并且提供了友好操作的api,比如求交集(intersect),并集(union),差集(difference),并且获取元素也比较友好
        redisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");
        redisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");
        // 交集(共同朋友)
        System.out.println(redisTemplate.opsForSet().intersect("Aurelia", "Nou"));
        // 1.求并集(所有关系网)
        System.out.println(redisTemplate.opsForSet().union("Aurelia", "Nou"));
        // 2.求差集(可能认识的人)
        System.out.println(redisTemplate.opsForSet().difference("Aurelia", "Nou"));
        // 随机获取两位用户
        System.out.println(redisTemplate.opsForSet().randomMembers("Aurelia", 2));

        // sort set, 有序集合, 可以用于排行榜类似需要排名的场景
        redisTemplate.opsForZSet().add("xuexichengji", "zhangsan", 98);
        redisTemplate.opsForZSet().add("xuexichengji", "lisi", 70);
        redisTemplate.opsForZSet().add("xuexichengji", "wangwe", 99);
        redisTemplate.opsForZSet().add("xuexichengji", "zhaoliu", 86);
        // 统计个数
        System.out.println(redisTemplate.opsForZSet().zCard("xuexichengji"));
        // 计算排名,以及获取分数 第一个索引是0, rank是降序, reverseRank是升序
        System.out.println(redisTemplate.opsForZSet().rank("xuexichengji", "zhangsan"));
        System.out.println(redisTemplate.opsForZSet().reverseRank("xuexichengji", "zhangsan"));
        System.out.println(redisTemplate.opsForZSet().incrementScore("xuexichengji", "zhangsan", 2));

        System.out.println(redisTemplate.opsForZSet().range("xuexichengji", 0, 4));

    }

}

// @Configuration
// @EnableCaching
// class CacheConfig extends CachingConfigurerSupport {
//
//     @Autowired
//     private RedisConnectionFactory redisConnectionFactory;
//
//     // @Bean
//     // @Override
//     // public CacheManager cacheManager() {
//     //     return RedisCacheManager.builder(redisConnectionFactory)
//     //             // .withCacheConfiguration(RedisController.key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
//     //             // If you have a customizer bean, you don't need this line, just ensure your customizer is called
//     //             .build();
//     // }
//     //
//     // @Bean
//     // public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//     //     return builder -> {
//     //         Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//     //         cacheConfigurations.put(RedisController.key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
//     //         // Add more cache configurations if needed
//     //
//     //         builder.withInitialCacheConfigurations(cacheConfigurations);
//     //     };
//     // }
//
//     // 这是一个本地缓存
//     @Bean
//     public CacheManager cacheManager() {
//         return new ConcurrentMapCacheManager("myCache");
//     }
//
// }
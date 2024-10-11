package org.hulei.keeping.server.spring.cache;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.pagehelper.PageHelper;
import org.hulei.common.mapper.entity.pojo.EmployeeDO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.common.mapper.mapper.EmployeeMapperPlus;
import org.hulei.keeping.server.KeepingApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hulei
 * @since 2024/9/11 9:20
 */

@Configuration
@EnableCaching
@Slf4j
@RestController
@RequestMapping("/springCache")
public class SpringCacheController {

    /*========================================== 配置相关 =======================================*/

    /**
     * 定义一个默认的缓存管理器, spring需要一个作为默认
     *
     * @return defaultManager
     */
    @Primary
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(CollectionUtil.newArrayList(
                // 本地内存缓存
                new ConcurrentMapCache("default"),
                new ConcurrentMapCache(SpringCacheController.local),
                new ConcurrentMapCache("employee")));
        return cacheManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // 设置缓存过期时间
                .disableCachingNullValues() // 不允许 null 的缓存值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(redisCacheWriter).withCacheConfiguration(SpringCacheController.redis, defaultCacheConfig).build();
    }


    /*========================================== 缓存相关 =======================================*/

    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    // public static final String key = "employees::getEmployees";
    public static final String local = "local";
    public static final String redis = "redis";
    public List<Object> clearQueue = new ArrayList<>();

    /**
     * Cacheable: value作为缓存的命名空间(即一个前缀), key作为完整实际的键值
     * 举例: 在使用ConcurrentMapCache的时候,value作为ConcurrentMapCache的名字 key作为这个cache内部map的key
     * <p>
     * 在redis的key值为 : redis::getEmployees::EmployeeQryReq(super=PageQryReqDTO(pageNum=1, pageSize=10), employeeNumber=null)
     *
     * @param req req
     * @return object
     */
    // @Cacheable(value = {"employee"}, key = "'getEmployees' + #req", cacheManager = "cacheManager")
    @Cacheable(value = {SpringCacheController.redis}, key = "'getEmployees::' + #req", cacheManager = "redisCacheManager")
    @PostMapping(value = "/getEmployees")
    public List<EmployeeDO> getEmployees(@RequestBody EmployeeQryReq req) {
        LambdaQueryWrapper<EmployeeDO> wrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(req.getEmployeeNumber())) {
            wrapper.eq(EmployeeDO::getEmployeeNumber, req.getEmployeeNumber());
        }
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return employeeMapperPlus.selectList(wrapper);
    }

    @Transactional
    @PostMapping("/addOneEmployee")
    public void addOneEmployee() {
        EmployeeDO employeeDO = buildMockData();
        employeeMapperPlus.insert(employeeDO);
        // 标记需要清理缓存
        clearQueue.add(new Object());
    }

    @Transactional
    @PostMapping("/updateEmployee")
    public EmployeeDO updateEmployee(@RequestBody EmployeeDO req) {
        EmployeeDO employeeDO = buildMockData();
        employeeDO.setEmployeeNumber(req.getEmployeeNumber());
        employeeMapperPlus.updateById(employeeDO);
        // 标记需要清理缓存
        clearQueue.add(new Object());
        return employeeDO;
    }

    /**
     * 每分钟的 0秒和30秒 清理缓存
     */
    @Scheduled(cron = "0,30 * * * * *")
    public void clearCache() {
        if (!CollectionUtils.isEmpty(clearQueue)) {
            Objects.requireNonNull(cacheManager().getCache("employee")).clear();
            clearQueue.clear();
        }
    }

    private EmployeeDO buildMockData() {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(System.currentTimeMillis());
        employeeDO.setLastName(JMockData.mock(String.class));
        employeeDO.setFirstName(JMockData.mock(String.class));
        employeeDO.setExtension(JMockData.mock(String.class));
        employeeDO.setEmail(JMockData.mock(String.class));
        employeeDO.setOfficeCode(JMockData.mock(String.class));
        employeeDO.setReportsTo(JMockData.mock(Integer.class));
        employeeDO.setJobTitle(JMockData.mock(String.class));
        return employeeDO;
    }

    /**
     * 打印所有缓存内容
     */
    @SneakyThrows
    @GetMapping("/getCache")
    public void getCache() {
        CacheManager cacheManager = KeepingApplication.applicationContext.getBean(CacheManager.class);
        for (String cacheName : cacheManager.getCacheNames()) {
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

package org.hulei.springboot.redis.redis.spring;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * @author hulei
 * @since 2024/11/16 0:59
 */

@Configuration
@RestController
public class SpringCacheRedisController {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // 设置缓存过期时间
                .disableCachingNullValues() // 不允许 null 的缓存值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(redisCacheWriter).withCacheConfiguration("redis", defaultCacheConfig).build();
    }

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
    @Cacheable(value = {"redis"}, key = "'getEmployees::' + #req", cacheManager = "redisCacheManager")
    @PostMapping(value = "/getEmployees")
    public List<EmployeeQryReq> getEmployees(@RequestBody EmployeeQryReq req) {
        LambdaQueryWrapper<EmployeeDO> wrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(req.getEmployeeNumber())) {
            wrapper.eq(EmployeeDO::getEmployeeNumber, req.getEmployeeNumber());
        }
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return employeeMapperPlus.selectList(wrapper);
    }

}

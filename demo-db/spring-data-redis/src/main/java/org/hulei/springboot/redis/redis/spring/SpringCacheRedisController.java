package org.hulei.springboot.redis.redis.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.springboot.redis.redis.spring.datatype.RedisStringController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/11/16 0:59
 */

@EnableCaching
@RequiredArgsConstructor
@Configuration
@RestController
@RequestMapping("/spring-redis-cache")
public class SpringCacheRedisController {

    /* =================================================== Spring缓存 ==================================================== */

    private final BigDataUserRepository bigDataUserRepository;

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        /*
        RedisCacheManager 是 Spring Cache 抽象（Spring Cache Abstraction） 中的一个具体实现类，用于基于 Redis 来管理缓存。

        1. 创建和管理 Cache 对象
          每个 Cache 其实就是一个 Redis 中的命名空间（key 前缀），由 RedisCacheManager 创建。
          每个 Cache 可以有不同的 TTL、序列化方式等配置。
        2. 与 Redis 交互
          它会使用底层的 RedisCacheWriter 与 Redis 进行通信。
          包括缓存读写、删除、过期、键名构建等操作。
        3. 管理配置（过期时间、序列化方式等）
          可以为所有缓存设置默认配置（比如：60 分钟过期、禁止缓存 null 等）。
          也可以为不同的缓存空间设置不同的配置。
         */
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig() // 一个默认的 RedisCacheConfiguration 实例，用于定义缓存的配置（如序列化、过期时间等）。
                .entryTtl(Duration.ofMinutes(60)) // 设置缓存条目的 TTL（time-to-live）为 60 分钟，即每条缓存数据存活时间为 1 小时。
                .disableCachingNullValues() // 禁止将 null 值缓存。这样可以避免出现一些缓存击穿的问题。
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())) // 设置缓存 键（key） 的序列化方式，使用字符串序列化器（RedisSerializer.string()）。这样你可以在 Redis 中看到清晰的字符串键。
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new FastJsonRedisSerializer<BigDataUser>(BigDataUser.class))); // 设置缓存 值（value） 的序列化方式，使用 JSON 序列化器（RedisSerializer.json()）。缓存的 Java 对象将被序列化为 JSON 格式存入 Redis，便于调试和跨语言兼容。

        // 最终返回一个构建好的 RedisCacheManager，Spring 会使用它来统一管理缓存行为。
        return RedisCacheManager.builder(redisCacheWriter).withCacheConfiguration("springrediscache", defaultCacheConfig).build();
    }

    /*
    @Cacheable 在方法执行前先检查缓存是否有结果，如果有就直接返回缓存结果，否则执行方法并将结果缓存起来。

    value 或 cacheNames	缓存名称（相当于 Redis 的 key 前缀），必填
    key	缓存的键，可使用 SpEL 表达式（如 #id, #user.name）
    condition	缓存的条件，满足才缓存，支持 SpEL
    cacheManager 指定缓存管理器
     */
    @Cacheable(value = {"springrediscache"}, key = "'big-data-user::' + #id", cacheManager = "redisCacheManager")
    @GetMapping(value = "/big-data-user/{id}")
    public BigDataUser findBigDataUserById(@PathVariable(value = "id") Long id) {
        return bigDataUserRepository.findById(id).isPresent() ? bigDataUserRepository.findById(id).get() : null;
    }


    /* =================================================== 缓存穿透，击穿，雪崩问题 ==================================================== */
    /*
    一、缓存穿透（Cache Penetration）
        查询一个不存在于缓存且不存在于数据库的 key（如恶意攻击或无效参数），导致请求绕过缓存直接打到数据库。
        由于数据库无该数据，每次请求都会穿透到数据库，可能压垮数据库。
          - 恶意请求：攻击者故意请求不存在的 key（如 user:999999）；
          - 业务逻辑漏洞：前端传入无效参数（如用户 ID 为负数）。
        💡解决方案
          1. 缓存空值（Null Cache）
          2. 布隆过滤器（Bloom Filter）

    二、缓存击穿（Cache Breakdown）
        一个热点 key（访问量极高）的缓存过期，大量请求同时打到数据库，导致数据库压力骤增甚至崩溃（“热点爆炸”）。
          - 热点 key 过期时间集中（如同一时间过期）；
          - 高并发场景下，多个请求同时发现缓存失效，同时查询数据库。
        💡解决方案
          1. 互斥锁（分布式锁）
          2. 热点 key 永不过期
    三、缓存雪崩（Cache Avalanche）
        大量缓存 key 同时过期（如批量设置的缓存过期时间集中），导致请求全部打到数据库，可能压垮数据库。
          - 缓存过期时间设置不合理（如所有 key 统一设置为 30 分钟）；
          - 业务场景中大量数据需要周期性刷新（如每日凌晨更新的商品列表）。
        💡解决方案
          1. 随机过期时间
          2. 多级缓存（本地缓存 + Redis）
     */


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BigDataUserRepository userRepository; // 数据库访问层

    Long userId = 0L;

    @PostConstruct
    public void init() {
        // 这里要建立一个缓存，用来说明三种缓存穿透、缓存击穿、缓存雪崩三个问题
        List<BigDataUser> bigDataUsers = bigDataUserRepository.fetchTop100();
        // 开启一个pipeline来执行，减少网络开销，提升性能
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            bigDataUsers.forEach((v) -> {
                String key = "spring-redis-cache:jichuan:" + v.getId();
                String value = JSON.toJSONString(v);
                // 💡热点数据永远不过期来防止缓存击穿的发生
                if (v.getId().equals(43230L)) {
                    connection.set(
                            key.getBytes(StandardCharsets.UTF_8),
                            value.getBytes(StandardCharsets.UTF_8)
                    );
                } else {
                    connection.set(
                            key.getBytes(StandardCharsets.UTF_8),
                            value.getBytes(StandardCharsets.UTF_8),
                            // 💡使用随机过期事件来防止雪崩的发生
                            Expiration.seconds(new Random().nextInt(600) + 1),
                            RedisStringCommands.SetOption.SET_IF_ABSENT
                    );
                }
            });
            return null;
        });
    }

    @GetMapping("/get-user-by-id")
    public BigDataUser getUserById(@RequestParam("id") Long id) {

        String key = RedisStringController.cachePrefix + id;

        // 1. 先查缓存
        // 🚨缓存击穿在这里发生，热点 key 过期瞬间被大量请求打到数据库
        // 🚨缓存雪崩也会在这里发生，大量 key 同时过期 → key 随机过期时间、双层缓存。
        String redis = (String) redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(redis)) {
            return JSON.parseObject(redis, BigDataUser.class);
        }

        // 2. 缓存不存在，查数据库
        BigDataUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            // 🚨缓存穿透在这里发生，如果不缓存空值，那么查询不存在的数据时，请求将全部打到db，造成数据库的压力
            // 💡通过缓存空值来达到防止缓存穿透的发生
            redisTemplate.opsForValue().set(key, "null", 5, TimeUnit.MINUTES); // 5分钟过期
            return new BigDataUser();
        }

        // 3. 数据库有数据，缓存到 Redis
        redisTemplate.opsForValue().set(key, user, 30, TimeUnit.MINUTES); // 30分钟过期
        return user;
    }


}

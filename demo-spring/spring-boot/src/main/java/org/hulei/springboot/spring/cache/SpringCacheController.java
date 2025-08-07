package org.hulei.springboot.spring.cache;

import cn.hutool.core.collection.CollectionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.springboot.SpringbootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
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
                new ConcurrentMapCache("local"),
                new ConcurrentMapCache("employee")));
        return cacheManager;
    }


    /*========================================== 缓存相关 =======================================*/
    public List<Object> clearQueue = new ArrayList<>();
    public List<BigDataUser> users = CollectionUtil.newArrayList(
            BigDataUser.gen(),
            BigDataUser.gen()
    );

    /*
    Cacheable: value作为缓存的命名空间(即一个前缀), key作为完整实际的键值
    举例: 在使用ConcurrentMapCache的时候,value作为ConcurrentMapCache的名字 key作为这个cache内部map的key

    value 指定缓存名称（可多个），实际对应缓存存储的 逻辑分区（如 Redis 中的 employee 命名空间）。
    key
      #root.method.getName()：获取当前方法名（如 getEmployeeById）。
      #root.args[0]：获取方法的第一个参数（如 id）。
     */
    @Cacheable(value = {"employee"}, key = "#root.method.getName() +':'+ #root.args[0]", cacheManager = "cacheManager")
    @PostMapping(value = "/getEmployees/{index}")
    public BigDataUser getEmployees(@PathVariable("index") int index) {
        return users.get(index);
    }

    @PostMapping("/addOneEmployee")
    public void addOneEmployee() {
        BigDataUser gen = BigDataUser.gen();
        users.add(gen);
        // 标记需要清理缓存
        clearQueue.add(new Object());
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

    /**
     * 打印所有缓存内容
     */
    @SneakyThrows
    @GetMapping("/getCache")
    public void getCache() {
        CacheManager cacheManager = SpringbootApplication.applicationContext.getBean(CacheManager.class);
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

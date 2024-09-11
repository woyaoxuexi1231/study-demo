package org.hulei.keeping.server.redis;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.jsonzou.jmockdata.JMockData;
import com.hundsun.demo.commom.core.annotation.DoneTime;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.req.PageQryReqDTO;
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

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

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

    /*========================================== 事务相关 =======================================*/

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Qualifier(value = "strObjRedisTemplate")
    @Autowired
    RedisTemplate<String, Object> strObjRedisTemplate;

    /**
     * multi exec 实现事务
     * <p>
     * spring redisTemplate并不是直接使用 multi 和 exec 来实现事务, 必须要结合 execute 和 SessionCallback回调函数来实现事务
     * 这样才能保证所有的操作在一个连接内运行
     */
    @GetMapping(value = "redisMultiAndExec")
    public void redisMultiAndExec() {
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
    @GetMapping(value = "redisTransactional")
    public void redisTransactional() {
        /*
        在redis.cli中操作, 在multi和exec中间执行的语句出现  1.命令错误(set写成了sett),这导致事务无法执行,也不会执行语句 2.运行时错误(本该sadd的命令写成了zadd),这导致前面执行的语句无法回滚
        redis本身的事务是不支持回滚的,所以这里如果出现运行时错误也是无法回滚的,但是如果出现运行时错误的其他错误,是可以不执行操作的
         */
        strObjRedisTemplate.opsForValue().set("transactional", "redis");
        // strObjRedisTemplate.opsForList().set("transactional", 0, "1");
        throw new RuntimeException("error");
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
        strObjRedisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");

        // 对于用户 Nou 来说,这算是他的关注列表,他也关注了四个人
        strObjRedisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");

        // 现在的问题是,要求这两个人的共同关注,这其实redis有提供这相关api可以使用
        Set<Object> intersect = strObjRedisTemplate.opsForSet().intersect("Aurelia", "Nou");


        // 扩展一下
        // 1.求并集
        Set<Object> union = strObjRedisTemplate.opsForSet().union("Aurelia", "Nou");
        // 2.求差集
        Set<Object> difference = strObjRedisTemplate.opsForSet().difference("Aurelia", "Nou");

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("intersect", CollectionUtils.isEmpty(intersect) ? "null" : intersect.toString());
        resultMap.put("union", CollectionUtils.isEmpty(union) ? "null" : union.toString());
        resultMap.put("difference", CollectionUtils.isEmpty(difference) ? "null" : difference.toString());
        return JSONObject.toJSONString(resultMap);
    }


    @GetMapping("/redisForString")
    public void redisForString() {

        // redis字符串类型的 key和 value 的大小限制默认都是 512MB, 最多容纳 2的32次方个key
        // redis的底层存储也是 hash表+链表 结构其实和 hashmap类似,但是没有红黑树

        // 对于字符串, redis提供自增接口, 编号,浏览量等不记名的数量统计都可以使用这种自增来解决, 并且由于redis的自增原子性,可以作为分布式id的获取途径(一定要避免数据丢失和备份)
        strObjRedisTemplate.opsForValue().set("string-inc", 1);
        System.out.println(strObjRedisTemplate.opsForValue().increment("string-inc", 1));

        // 位图的使用, 可以作为统计用户签到行为,打卡行为
        strObjRedisTemplate.opsForValue().setBit("bit-test", 0, true);
        strObjRedisTemplate.opsForValue().setBit("bit-test", 1, false);
        System.out.println(strObjRedisTemplate.opsForValue().getBit("bit-test", 0));
        System.out.println(strObjRedisTemplate.opsForValue().getBit("bit-test", 1));
        System.out.println(strObjRedisTemplate.opsForValue().getBit("bit-test", 2));

        // {@link com.hundsun.demo.springboot.redis.msglist.MessageConsumer.run} list可以作为消息队列使用, blpop和brpop这两个操作可以阻塞的弹出元素

        // set结构,易于构建类似 记名点赞,关注列表这样的类似 一对多的结构,并且提供了友好操作的api,比如求交集(intersect),并集(union),差集(difference),并且获取元素也比较友好
        strObjRedisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");
        strObjRedisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");
        // 交集(共同朋友)
        System.out.println(strObjRedisTemplate.opsForSet().intersect("Aurelia", "Nou"));
        // 1.求并集(所有关系网)
        System.out.println(strObjRedisTemplate.opsForSet().union("Aurelia", "Nou"));
        // 2.求差集(可能认识的人)
        System.out.println(strObjRedisTemplate.opsForSet().difference("Aurelia", "Nou"));
        // 随机获取两位用户
        System.out.println(strObjRedisTemplate.opsForSet().randomMembers("Aurelia", 2));

        // sort set, 有序集合, 可以用于排行榜类似需要排名的场景
        strObjRedisTemplate.opsForZSet().add("xuexichengji", "zhangsan", 98);
        strObjRedisTemplate.opsForZSet().add("xuexichengji", "lisi", 70);
        strObjRedisTemplate.opsForZSet().add("xuexichengji", "wangwe", 99);
        strObjRedisTemplate.opsForZSet().add("xuexichengji", "zhaoliu", 86);
        // 统计个数
        System.out.println(strObjRedisTemplate.opsForZSet().zCard("xuexichengji"));
        // 计算排名,以及获取分数 第一个索引是0, rank是降序, reverseRank是升序
        System.out.println(strObjRedisTemplate.opsForZSet().rank("xuexichengji", "zhangsan"));
        System.out.println(strObjRedisTemplate.opsForZSet().reverseRank("xuexichengji", "zhangsan"));
        System.out.println(strObjRedisTemplate.opsForZSet().incrementScore("xuexichengji", "zhangsan", 2));

        System.out.println(strObjRedisTemplate.opsForZSet().range("xuexichengji", 0, 4));

    }

    /**
     * 一个不停止插入数据的线程任务
     */
    @PostConstruct
    public void insertData() {
        commonPool.execute(() -> {
            while (true) {
                while (isInsert.get()) {
                    strObjRedisTemplate.opsForValue().set(JMockData.mock(String.class), JMockData.mock(String.class));
                }
            }
        });
    }

    /**
     * 是否终止插入数据
     */
    private final AtomicBoolean isInsert = new AtomicBoolean(false);

    @Autowired
    ThreadPoolExecutor commonPool;

    /**
     * redis最大内存测试,以及内存淘汰策略的使用
     */
    @GetMapping("/maxMemoryTest")
    public void maxMemoryTest() {
        /*
        设置redis的最大内存为 10 MB, maxmemory 10MB, redis默认的内存淘汰策略是 noeviction,所以内存超了之后就不再允许插入数据了所以内存溢出后会报错:
                Caused by: org.redisson.client.RedisOutOfMemoryException: command not allowed when used memory > 'maxmemory'..

        LRU - Least Recently Used,最近最少使用
        volatile-lru: 根据lru算法删除设置了超时时间的键,直到腾出足够空间,如果没有可以删除的键了,那么回退到 noeviction
        allkeys-lru: 根据lru算法删除所有的键,直到腾出足够空间
        allkeys-random: 随机删除所有键,直到腾出足够空间
        volatile-random: 随机删除过期键,直到腾出足够空间
        volatile-ttl: 根据对象的过期时间,删除最近要过期的数据,如果没有,那么回退到 noeviction 策略

        总结就是: 过期键一共三种-最近要过期,最近最少使用,随机; 所有键一共两种-最近最少使用,随机; 还有直接不允许插入了
         */
        synchronized (this) {
            isInsert.set(!isInsert.get());
        }
    }

    /**
     * 执行备份命令
     */
    @GetMapping("/save")
    public void save(){

    }

    @DoneTime
    @PostMapping("/scan")
    public void scan(PageQryReqDTO reqDTO) {
        System.out.println(strObjRedisTemplate.keys("*a*"));
    }
}
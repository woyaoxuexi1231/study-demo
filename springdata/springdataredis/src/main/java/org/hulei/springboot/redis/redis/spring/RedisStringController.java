package org.hulei.springboot.redis.redis.spring;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hulei.common.mapper.entity.pojo.EmployeeDO;
import org.hulei.common.mapper.mapper.EmployeeMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/9/20 13:33
 */

@RequestMapping("/redisString")
@RestController
public class RedisStringController {

    /*
    字符串: 操作的时间复杂度基本都是O(1)
    set key value [EX seconds|PX milliseconds|EXAT timestamp|PXAT milliseconds-timestamp|KEEPTTL] [NX|XX] [GET]
        - ex 设置过期的倒数时间,单位秒
        - px 设置过期的倒数时间,单位毫秒
        - exat 设置过期的真实时间,UNIX 时间戳(秒)过期
        - pxat 设置过期的真实时间,UNIX 时间戳(毫秒)过期
        - KEEPTTL 如果键已经有过期时间,那么保持他的过期时间,不覆盖过期时间
        - nx 只有在键不存在的时候才设置键
        - xx 只有在键存在的时候才设置键
        - get 获取旧值并获取新值
        除此之外,redis还提供了 setnx 和 setxx 两个命令,提供原子性的 检查并插入 这个操作
    get key 获取值
    mset key value [key value ...] 批量设置值
    mget key [key ...] 批量获取值
    incr key 用于自增,但是value一定要是整形,否则直接返回错误. 如果键不存在那么默认键的值为0,然后对这个键进行+1操作
        同时还提供了 decr, 自定义增量的 incrby key increment, decrby key increment
    append key value 向字符串尾部追加值
    strlen key 输出值的长度
    getset key value 设置并原来得值,如果之前是空,那么返回nil
    setrange key offset value 设置指定位置的值为新值
    getrange key start end 返回指定位置的值,包括start和end
     */

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Qualifier(value = "strObjRedisTemplate")
    @Autowired
    RedisTemplate<String, Object> strObjRedisTemplate;


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

    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    String employeeCacheStrPrefix = "employeeCache:";

    String employeeCacheUseCount = "employeeCache:useCount";

    /**
     * redis的字符串作为一个缓存的简单使用
     *
     * @param id 用户id
     * @return 结果
     */
    @GetMapping("/getEmployeeById")
    public EmployeeDO getEmployeeById(Long id) {
        // 先查询缓存中存在不存在
        EmployeeDO redisRsp = JSON.parseObject(stringRedisTemplate.opsForValue().get(employeeCacheStrPrefix + id), EmployeeDO.class);

        Optional<EmployeeDO> optional = Optional.ofNullable(redisRsp);
        // 如果值存在,那么就命中缓存一次,计数一下,这里也算是redis的计数功能的简单使用
        optional.ifPresent((r) -> strObjRedisTemplate.opsForValue().increment(employeeCacheUseCount));
        EmployeeDO rsp = optional.orElseGet(() -> {
            LambdaQueryWrapper<EmployeeDO> queryWrapper = Wrappers.lambdaQuery(EmployeeDO.class);
            queryWrapper.eq(EmployeeDO::getEmployeeNumber, id);
            return employeeMapperPlus.selectOne(queryWrapper);
        });

        if (Objects.isNull(rsp)) {
            // 缓存和数据库都没有,这里先不做过滤器,直接返回
            return null;
        } else {
            // 设置缓存,60秒作为过期时间
            stringRedisTemplate.opsForValue().set(employeeCacheStrPrefix + id, JSON.toJSONString(rsp), 60, TimeUnit.SECONDS);
            return rsp;
        }
    }

    @GetMapping("/getCacheHitCount")
    public Integer getCacheHitCount() {
        return (Integer) Optional.ofNullable(strObjRedisTemplate.opsForValue().get(employeeCacheUseCount)).orElse(0);
    }

}

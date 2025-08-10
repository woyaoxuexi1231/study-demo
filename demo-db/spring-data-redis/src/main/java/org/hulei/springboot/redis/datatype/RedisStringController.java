package org.hulei.springboot.redis.datatype;

import com.alibaba.fastjson.JSON;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.jpa.starter.dao.BigDataUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
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
        除此之外,redis还提供了 setnx 和 setxx 两个命令,提供原子性的 检查并插入 这个操作(set xxx ex nx 也是原子性的)
    mset key value [key value ...] 批量设置值
    incr key 用于自增,但是value一定要是整形,否则直接返回错误. 如果键不存在那么默认键的值为0,然后对这个键进行+1操作
        同时还提供了 decr, 自定义增量的 incrby key increment, decrby key increment
    append key value 向字符串尾部追加值, 键不存在会直接新增这个key
    getset key value 设置并原来得值,如果之前是空,那么返回nil
    setrange key offset value 设置指定位置的值为新值


    获取键:
    get key 获取值
    mget key [key ...] 批量获取值
    strlen key 输出值的长度
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


        strObjRedisTemplate.expire("string-inc", 0, TimeUnit.SECONDS);

        // // 位图的使用, 可以作为统计用户签到行为,打卡行为
        // strObjRedisTemplate.opsForValue().setBit("bit-test", 0, true);
        // strObjRedisTemplate.opsForValue().setBit("bit-test", 1, false);
        // System.out.println(strObjRedisTemplate.opsForValue().getBit("bit-test", 0));
        // System.out.println(strObjRedisTemplate.opsForValue().getBit("bit-test", 1));
        // System.out.println(strObjRedisTemplate.opsForValue().getBit("bit-test", 2));
        //
        // // {@link com.hundsun.demo.springboot.redis.msglist.MessageConsumer.run} list可以作为消息队列使用, blpop和brpop这两个操作可以阻塞的弹出元素
        //
        // // set结构,易于构建类似 记名点赞,关注列表这样的类似 一对多的结构,并且提供了友好操作的api,比如求交集(intersect),并集(union),差集(difference),并且获取元素也比较友好
        // strObjRedisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");
        // strObjRedisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");
        // // 交集(共同朋友)
        // System.out.println(strObjRedisTemplate.opsForSet().intersect("Aurelia", "Nou"));
        // // 1.求并集(所有关系网)
        // System.out.println(strObjRedisTemplate.opsForSet().union("Aurelia", "Nou"));
        // // 2.求差集(可能认识的人)
        // System.out.println(strObjRedisTemplate.opsForSet().difference("Aurelia", "Nou"));
        // // 随机获取两位用户
        // System.out.println(strObjRedisTemplate.opsForSet().randomMembers("Aurelia", 2));
        //
        // // sort set, 有序集合, 可以用于排行榜类似需要排名的场景
        // strObjRedisTemplate.opsForZSet().add("xuexichengji", "zhangsan", 98);
        // strObjRedisTemplate.opsForZSet().add("xuexichengji", "lisi", 70);
        // strObjRedisTemplate.opsForZSet().add("xuexichengji", "wangwe", 99);
        // strObjRedisTemplate.opsForZSet().add("xuexichengji", "zhaoliu", 86);
        // // 统计个数
        // System.out.println(strObjRedisTemplate.opsForZSet().zCard("xuexichengji"));
        // // 计算排名,以及获取分数 第一个索引是0, rank是降序, reverseRank是升序
        // System.out.println(strObjRedisTemplate.opsForZSet().rank("xuexichengji", "zhangsan"));
        // System.out.println(strObjRedisTemplate.opsForZSet().reverseRank("xuexichengji", "zhangsan"));
        // System.out.println(strObjRedisTemplate.opsForZSet().incrementScore("xuexichengji", "zhangsan", 2));
        //
        // System.out.println(strObjRedisTemplate.opsForZSet().range("xuexichengji", 0, 4));

    }


    /*
    Redis 的 String 类型是最基础的数据结构（本质是二进制安全的字符串），看似简单却覆盖了80%以上的缓存场景。
    💡 string 类型默认长度最大是 512mb，但是其实使用过程中不用太注意这个，使用不到这么大

    使用场景：
      1. 缓存热点数据（最常用）
         场景：高频访问的数据库查询结果（如用户信息、商品详情），通过 String 缓存减少 DB 压力。
         实现：
           - Key 设计：业务模块:唯一标识（如 user:1001表示用户ID为1001的信息）。
           - Value 存储：序列化后的对象（JSON/Protobuf/MessagePack）。
           - 过期策略：通过 SETEX key seconds value或 EXPIRE设置合理过期时间（如30分钟），避免脏数据长期占用内存。
      2. 计数器/限速器（利用原子性）
         场景：点赞数、评论数、接口调用次数统计，或限制接口频率（如每分钟最多请求10次）。
         实现：
           - 计数器：用 INCR key原子递增（无需加锁），GET key获取当前值。
           - 限速器：结合 EXPIRE实现滑动窗口（如记录每次请求时间戳，用 LLEN限制最近N次的请求数）。

    🚨 String 类型虽灵活，但单个Key过大（如超过1MB）会导致：网络传输耗时增加（单次GET需传输大文件）、主从复制/持久化（RDB/AOF）变慢、集群模式下可能触发「大Key迁移」阻塞。
    🚨 序列化选择：空间与性能平衡
    🚨 过期策略：避免内存泄漏
    🚨 原子性边界：多命令需Lua脚本
     */



    /* =================================================  缓存热点数据  ================================================= */

    @Autowired
    BigDataUserRepository bigDataUserRepository;
    // 业务模块:唯一标识
    public static final String cachePrefix = "springdataredis:string:";

    /*
    初始化预热，可以在程序启动时进行一个缓存预热，把已知的需要大量访问的数据进行预先的插入数据库
     */
    @PostConstruct
    public void initCachePreheating() {
        // 这里假设前一百条就是最热门的数据
        List<BigDataUser> bigDataUsers = bigDataUserRepository.fetchTop100();
        bigDataUsers.forEach(v -> stringRedisTemplate.opsForValue()
                .set(cachePrefix + "cache:user:" + v.getId(),
                        JSON.toJSONString(v),
                        60,
                        TimeUnit.MINUTES
                ));
    }

    /*
    实际使用缓存的场景，在使用过程中缓存在合理的过期策略下会越来越趋近于全是热点数据
    查询数据 -> 查询缓存 -> 缓存没有继续查询数据库 -> 数据插入缓存
     */
    @GetMapping("/get-user-by-id")
    public BigDataUser getUserById(Long id) {

        // 查询缓存数据
        Optional<String> optional = Optional.ofNullable(stringRedisTemplate.opsForValue().get(cachePrefix + "cache:user:" + id));
        // 在根据缓存结果，决定下一步
        if (optional.isPresent()) {
            return JSON.parseObject(optional.get(), BigDataUser.class);
        } else {
            BigDataUser dbData = bigDataUserRepository.findById(id).orElse(null);
            if (Objects.nonNull(dbData)) {
                // 如果数据存在插入缓存
                stringRedisTemplate.opsForValue()
                        .set(
                                cachePrefix + "cache:user:" + id,
                                JSON.toJSONString(dbData),
                                60,
                                TimeUnit.SECONDS
                        );
            }
            return dbData;
        }
    }

    /* =================================================  计数器/限速器（利用原子性）  ================================================= */

    /*
    点击此接口将增加一次接口的点击数量
    其实这种需求可以放到aop中去做
     */
    @GetMapping("/hit")
    public void hit() {
        if (Boolean.FALSE.equals(strObjRedisTemplate.opsForValue().setIfAbsent(cachePrefix + "count:hit", 0))) {
            strObjRedisTemplate.opsForValue().increment(cachePrefix + "count:hit", 1);
        }
    }
}

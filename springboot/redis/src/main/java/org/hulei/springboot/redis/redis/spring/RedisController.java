package org.hulei.springboot.redis.redis.spring;

import com.alibaba.fastjson.JSONObject;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /*
    redis的命令分类:
        Strings（字符串）
        Hashes（哈希）
        Lists（列表）
        Sets（集合）
        Sorted Sets（有序集合）
        HyperLogLog
        Pub/Sub（发布订阅）
        Transactions（事务）
        Scripting（脚本）
        Connection（连接）
        Server（服务器）

    全局命令:
    keys [pattern] 查询全局的所有key,在key非常多的情况下会严重阻塞redis的运行,不建议使用,时间复杂度 O(n), 对于一个拥有一百二十万的key的库执行这个命令,花费了7秒多
    dbsize 查询数据库一共有多少键, dbsize的时间复杂度是 O(1) 他是直接查询redis的内置的键总数变量,可以放心使用
    exists key 时间复杂度O(1)
    del key 删除键
    expire key seconds 设置键的过期时间
    type key 查看键的数据类型

    rename key newkey 重命名键
    randomkey 随机返回一个
    persist 用于移除键的过期时间
    ttl keys 返回键的过期时间
    scan cursor [MATCH pattern] [COUNT count] [TYPE type] 遍历元素 cursor第一次写0,以后每次写上一次返回的游标, 这个命令能有效的解决keys的阻塞问题
    flushdb/fluashall 用于清空数据库或者所有数据库

    acl setuser username 添加用户
    acl setuser username on 开启用户
    acl setuser username >password 设置用户密码
    acl cat 查看当前所有的命令集合
    acl cat string 查看string类型的命令集合
    acl setuser user ~* +@string 开启string命令权限
    acl setuser user +ttl +hset 设置单个命令
     */

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
     * <p>
     * 而exec也不仅仅只提供这种功能,exec最强大的功能体现在下面几个方面:
     * 1. 可以执行任意redis命令,通过传入不同的Callback来执行任意类型的redis命令
     * 2. 事务支持,也就是这个例子中使用的
     * 3. 管道支持,strObjRedisTemplate.executePipelined()来批量执行操作
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
        在redis.cli中操作, 在multi和exec中间执行的语句出现
            1.命令错误(set写成了sett),这导致事务无法执行,也不会执行语句
            2.运行时错误(本该sadd的命令写成了zadd),这导致前面执行的语句无法回滚
        redis本身的事务是不支持回滚的,所以这里如果出现运行时错误也是无法回滚的,但是如果出现运行时错误的其他错误,是可以不执行操作的
         */
        strObjRedisTemplate.opsForValue().set("transactional", "redis");
        // 对于这里抛出的异常,spring会控制不会提交上面的set命令
        throw new RuntimeException("error");
    }

    /*========================================== redis的基本使用 =======================================*/

    /**
     * 是否终止插入数据
     */
    private final AtomicBoolean isInsert = new AtomicBoolean(false);

    private int insertCount = 0;

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


        这里我内存淘汰策略使用的是 allkeys-lru,但是奇怪的是在操作几秒钟后,redis仍然会报错command not allowed when used memory > 'maxmemory'..
        使用 allkeys-ramdon也是同样的问题
        TODO 这里暂时不知道什么原因导致的


        used_memory:3293304 - redis当前正在使用的内存总量 单位字节 3.2mb左右
        used_memory_human:3.14M - 以人类能够方便理解的单位显示used_memory
        used_memory_rss:10719232 - redis在操作系统层面分配的物理内存,通常比used_memory大,因为操作系统在管理内存时会有额外的开销，比如内存碎片或未使用但仍保留的内存页。
        used_memory_rss_human:10.22M
        used_memory_peak:16781376 - Redis曾经使用的最大内存量，即历史峰值内存使用量。
        used_memory_peak_human:16.00M
        used_memory_peak_perc:19.62% - 当前内存使用量相对于历史峰值内存使用量的百分比。当前的内存使用量仅占峰值使用量的19.62%。
        used_memory_overhead:2934734 - Redis的内存开销，主要是指非数据相关的内存消耗，如元数据、字典、哈希表等结构。
        used_memory_startup:812264 - Redis在启动时的内存占用。这个值代表了Redis启动时的初始内存负载，通常用于基础设施的初始化。
        used_memory_dataset:358570 - 实际存储数据所使用的内存量。14.45%表示数据集占用的内存量占总内存的百分比。
        used_memory_dataset_perc:14.45%
        allocator_allocated:3361320 - 这是通过内存分配器实际分配给Redis的数据内存量。
        allocator_active:3944448 - 内存分配器当前为Redis保留的内存。即使Redis没有实际使用全部内存，分配器也可能会为未来使用预留更多内存。
        allocator_resident:7303168 - 操作系统分配给内存分配器的物理内存。比allocator_active大，表示有一部分物理内存可能未实际使用但被分配了。
        total_system_memory:2964652032 - 系统总内存容量。
        total_system_memory_human:2.76G
        used_memory_lua:30720 - Redis用于Lua脚本的内存。
        used_memory_lua_human:30.00K
        used_memory_scripts:0
        used_memory_scripts_human:0B
        number_of_cached_scripts:0
        maxmemory:4194304 - Redis的最大内存使用限制。超出此值时，Redis将根据配置的驱逐策略删除一些数据。
        maxmemory_human:4.00M
        maxmemory_policy:volatile-random - 当内存达到上限时的逐出策略。volatile-random表示只会驱逐设置了过期时间的键，并且随机选择一个键进行驱逐。
        allocator_frag_ratio:1.17 - 内存分配器的碎片化比例。值大于1表示存在碎片化，1.17表示有17%的内存未被充分利用。
        allocator_frag_bytes:583128 - 内存分配器中的碎片量。
        allocator_rss_ratio:1.85 - 分配器的物理内存（RSS）与分配的虚拟内存之间的比例。1.85表示操作系统为Redis分配了比其实际需要多85%的物理内存。
        allocator_rss_bytes:3358720 - 物理内存分配给Redis但未实际使用的字节数。
        rss_overhead_ratio:1.47 - Redis实际使用的内存与操作系统为其保留的内存之间的比例。1.47表示操作系统保留了47%的额外内存。
        rss_overhead_bytes:3416064 - 操作系统层面为Redis保留但Redis未使用的内存量。
        mem_fragmentation_ratio:3.30 - 总的内存碎片化比例，包括所有可能的内存碎片。这一数值较高（3.30），表明存在显著的内存碎片问题。
        mem_fragmentation_bytes:7466952 - 内存碎片的总量，单位是字节。
        mem_not_counted_for_evict:314 - 不计入驱逐机制的内存，通常是系统保留的某些部分。
        mem_replication_backlog:0 -
        mem_clients_slaves:0 - 连接的从节点所占用的内存。
        mem_clients_normal:1557766 - 普通客户端连接所占用的内存。
        mem_aof_buffer:320 - AOF（Append Only File）缓冲区占用的内存。
        mem_allocator:jemalloc-5.1.0 - Redis使用的内存分配器是jemalloc，这是一个高效的内存分配器，专门优化了内存碎片问题。
        active_defrag_running:0 - 表示主动碎片整理是否正在运行。0表示没有在运行。
        lazyfree_pending_objects:0 - 懒惰删除机制中等待删除的对象数量。
        lazyfreed_objects:0 - 懒惰删除机制已经删除的对象数量。
         */
        // commonPool.execute(() -> {
        //     for (int i = 0; i < 500000; i++) {
        //         strObjRedisTemplate.opsForValue().set(JMockData.mock(String.class), JMockData.mock(String.class));
        //         insertCount++;
        //     }
        //     log.info("五十万条字符串类型数据添加完成!");
        // });

        commonPool.execute(() -> {
            for (int i = 0; i < 500000; i++) {
                strObjRedisTemplate.opsForList().leftPush("blocking::list", JMockData.mock(String.class));
                insertCount++;
            }
            log.info("五十万条list类型数据添加完成!");
        });
    }

    @GetMapping("/blocking")
    public void blocking() {
        /*
        讨论一下redis在什么情况下可能导致响应速度变慢
        1. keys 由于会遍历所有的数据值,会造成大量的cpu操作和阻塞,这个操作一定要慎用
        2.
         */
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("blocking-string");
        strObjRedisTemplate.opsForValue().get("blocking::string");
        stopWatch.stop();

        stopWatch.start("blocking-list");
        strObjRedisTemplate.opsForList().range("blocking::list", 0, -1);
        stopWatch.stop();
    }

    @GetMapping("/getKeyByScan")
    public void getKeyByScan(String pattern) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(10).build(); // 可以根据需要设置匹配模式和数量
        try (Cursor<String> scan = strObjRedisTemplate.scan(options)) {
            while (scan.hasNext()) {
                log.info("redis 存在 pattern:{} 的 key:{} ", pattern, scan.next());
            }
        }
        stopWatch.stop();
        String dbsize = stringRedisTemplate.execute((RedisCallback<String>) connection -> String.valueOf(connection.dbSize()));
        log.info("本次扫描结束, 一共扫描{}个键, 耗时分析: {}", dbsize, stopWatch.prettyPrint());
    }
}
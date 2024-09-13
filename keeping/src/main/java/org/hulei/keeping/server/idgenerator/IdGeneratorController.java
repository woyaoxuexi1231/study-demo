package org.hulei.keeping.server.idgenerator;

import com.github.jsonzou.jmockdata.JMockData;
import org.hulei.keeping.server.common.model.req.ItemDO;
import org.hulei.keeping.server.db.dynamicdb.core.DataSourceToggleUtil;
import org.hulei.keeping.server.idgenerator.segmentid.SegmentIdGenerator;
import org.hulei.keeping.server.idgenerator.snowflake.SnowflakeConfig;
import org.hulei.keeping.server.mybatis.mybatisplus.mapper.ItemsMapper;
import org.hulei.keeping.server.utils.StopWatch;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hulei
 * @since 2024/8/30 14:18
 */

@RequestMapping(value = "/idgenerator")
@RestController
public class IdGeneratorController implements ApplicationContextAware {
    /**
     * 上下文
     */
    ApplicationContext applicationContext;
    @Autowired
    private SnowflakeConfig snowflakeConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * id生成器
     */
    SegmentIdGenerator segmentIdGenerator;
    /**
     * 测试线程池
     */
    ThreadPoolExecutor commonPool;

    @Autowired
    SnowflakeConfig SnowflakeConfig;

    @Autowired
    ItemsMapper itemsMapper;

    @Autowired
    public IdGeneratorController(ThreadPoolExecutor commonPool, SegmentIdGenerator segmentIdGenerator) {
        this.commonPool = commonPool;
        this.segmentIdGenerator = segmentIdGenerator;
    }

    /**
     * 数据库分布式ID到底采用哪种方案? 自增,UUID,分段式id,雪花算法?
     * <p>
     * UUID被淘汰 - 随机性虽然很强,但是由于UUID是无序的,这导致innodb的索引在高并发的插入场景将频繁的重构,非常的影响性能
     * 自增 - 单机,主从复制都是很好的解决方案,实现简单,性能不错,但是在分布式架构下,多主的环境中并不能很好的工作,可能会出现冲突的问题,但是也有解决方案,设置不同的自增长步长可以解决问题,但是对于扩容不友好
     * 分段式ID和雪花算法都是很好的分布式ID解决方案
     * <p>
     * 方法对比数据库自增id和分布式生成方案的性能
     * 方法的对比结果就是对于单列插入和批量插入来说使用自增和其他两个对比,好像性能差不了多少
     */
    @SneakyThrows
    @GetMapping("/generateIds")
    public void generateIds() {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("多线程一条一条使用自增id插入数据");
        CountDownLatch count = new CountDownLatch(20000);
        for (int i = 0; i < 20000; i++) {
            commonPool.execute(() -> {
                try {
                    DataSourceToggleUtil.set("first");
                    itemsMapper.insertItemsAutoKey(ItemDO.builder().itemNo(JMockData.mock(String.class)).build());
                } finally {
                    count.countDown();
                }
            });
        }
        count.await();
        stopWatch.stop();

        CountDownLatch count2 = new CountDownLatch(20000);
        stopWatch.start("多线程一条一条使用分布式id算法插入数据");
        for (int i = 0; i < 20000; i++) {
            commonPool.execute(() -> {
                try {
                    DataSourceToggleUtil.set("first");
                    long snowflakeId = segmentIdGenerator.getId("test").getId();
                    // long snowflakeId = snowflakeConfig.getSnowflake().nextId();
                    // log.info("申请的 id 为: {}", snowflakeId);
                    DataSourceToggleUtil.set("third");
                    itemsMapper.insertItemsCustomKey(ItemDO.builder().id(snowflakeId).itemNo(JMockData.mock(String.class)).build());
                } finally {
                    count2.countDown();
                }
            });
        }
        count2.await();
        stopWatch.stop();

        stopWatch.start("多线程使用自增id批量插入数据");
        CountDownLatch count3 = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            commonPool.execute(() -> {
                try {
                    List<ItemDO> itemDOS = new ArrayList<>();
                    for (int j = 0; j < 200; j++) {
                        itemDOS.add(ItemDO.builder().itemNo(JMockData.mock(String.class)).build());
                    }
                    DataSourceToggleUtil.set("first");
                    itemsMapper.insertItemsAutoKeyList(itemDOS);
                } finally {
                    count3.countDown();
                }
            });
        }
        count3.await();
        stopWatch.stop();

        stopWatch.start("多线程使用分布式id批量插入数据");
        CountDownLatch count4 = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            try {
                List<ItemDO> itemDOS = new ArrayList<>();
                DataSourceToggleUtil.set("first");
                for (int j = 0; j < 200; j++) {
                    long snowflakeId = segmentIdGenerator.getId("test").getId();
                    // long snowflakeId = snowflakeConfig.getSnowflake().nextId();
                    itemDOS.add(ItemDO.builder().id(snowflakeId).itemNo(JMockData.mock(String.class)).build());
                }
                DataSourceToggleUtil.set("third");
                itemsMapper.insertItemsCustomKeyList(itemDOS);
            } finally {
                count4.countDown();
            }
        }
        count4.await();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        /*
        对于mysql数据的插入类型分为四种:
        1.insert-like 指所有的插入语句
        2.simple inserts 在插入前就能确定行数的语句
        3.bulk inserts 在插入前并不能确定行数的语句, insert ... select, replace ... select, load data
        4.mixed-mode inserts 插入中有一部分的值是自增长的,有一部分是确定的.

        innodb在5.1.22版本之后进行了性能优化,加入了 innodb_autoinc_lock_mode,提供三个可选值
        在说明三种参数前,来说明一下自增锁的释放时机问题:
            - 自增锁在最初被设计在数据插入完成时释放(不要求事务已经提交),这意味者相同的操作,插入相同的数据,每一次都能产生相同顺序的自增ID
            - 自增锁的释放在获取到自增序列后立马释放,这可能导致相同的操作,相同的数据,每次插入的获取的自增序列不连续 (A获取到11,B获取到12,但是B却先插入,A后插入)
        1. 0 采用特殊的表锁机制, select max(auto_inc_clo) from t for update;获取自增长的计数器值+1赋予自增长列, AUTO-INC Locking
            每次生成自增ID值的时候都都会进行上锁(锁是加在自增长值的获取上),相当于就是说即使是批量插入,也会一条一条的去获取自增id
        2. 1 连续模式,对于simple inserts,采用互斥量去对内存中的计数器进行累加,这个相当于是在第一种方式上进行了批量优化,批量插入时,直接获取一批可以用的id
        3  2 交错模式,对于所有的插入操作,都采用互斥量,这导致这种自增长模式在主从复制的情况下,对于statement-based方式的replication并不能很好的支持,所以一定要使用基于行的复制方式row-base
         */
    }

    public static void main(String[] args) {
        String s = "jdbc:mysql://192.168.80.128:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
        System.out.println("jdbc:mysql://".length());
        System.out.println(s.substring("jdbc:mysql://".length(), s.lastIndexOf("?")));
    }
}

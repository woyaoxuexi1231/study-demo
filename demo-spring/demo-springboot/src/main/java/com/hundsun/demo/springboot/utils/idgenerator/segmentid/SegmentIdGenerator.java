package com.hundsun.demo.springboot.utils.idgenerator.segmentid;

import cn.hutool.core.collection.CollectionUtil;
import com.hundsun.demo.springboot.mapper.SequenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.utils.segmentid
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-10 09:59
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

// @ConditionalOnProperty(value = {"ast.id.generate.strategy"}, havingValue = "segment")
@Component
@Slf4j
public class SegmentIdGenerator implements ApplicationContextAware {

    /**
     * 容器
     */
    private ApplicationContext applicationContext;
    /**
     * mybatis sqlSessionFactory
     */
    private SqlSessionFactory sqlSessionFactory;
    /**
     * SeqIdUtils
     */
    // private final SeqIdUtils seqIdUtils = new SeqIdUtils();

    /**
     * 默认步长, 也是最小步长
     */
    @Value(value = "${ast.id.generate.step:100}")
    private int defaultStep;
    /**
     * 用于异步更新 segment 的线程池
     */
    private ExecutorService service;

    private ScheduledExecutorService scheduledExecutorService;
    /**
     * 当前工具是否成功初始化
     */
    private volatile boolean initOk = false;
    /**
     * segment缓存器
     */
    private Map<String, SegmentBuffer> cache;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        // 初始化 service todo 参数待修改
        this.service = new ThreadPoolExecutor(
                5,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new UpdateThreadFactory());
        // 初始化缓存
        cache = new ConcurrentHashMap<>();
        // 置为未初始化状态
        this.initOk = false;
        // 执行初始化方法
        this.init();
    }

    public boolean init() {
        try {
            // 初始化 sqlSessionFactory
            this.initSqlSessionFactory();
            // 第一次更新缓存
            this.updateCacheFromDb();
            this.initOk = true;
            // this.updateCacheFromDbAtEveryMinute();
        } catch (Exception e) {
            log.error("SegmentIdGenerateUtil-分段式ID生成器初始化失败", e);
        }
        return initOk;
    }

    @PreDestroy
    public void destroy() {
        log.info("销毁 scheduledExecutorService ...");
        this.scheduledExecutorService.shutdown();
    }

    /**
     * 初始化 sqlSessionFactory
     */
    private void initSqlSessionFactory() {
        StopWatch sw = new StopWatch("initSqlSessionFactory");
        sw.start();
        // DataSource是jdk提供的一个标准的用于建立数据库连接的对象, springboot默认使用 HikariCP 作为数据库连接池, 其他还有Apache Commons DBCP,Druid等等
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        // TransactionFactory负责创建 Transaction 对象, 用来管理和维护数据库事务
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        // 这是 MyBatis 中的一个配置容器，用来设置数据库运行环境的名称（在这里是 “development”）
        Environment environment = new Environment("development", transactionFactory, dataSource);
        // 用刚才创建的 Environment 对象来实例化 MyBatis 的 Configuration 类。Configuration 类包含了所有 MyBatis 的配置信息，包括映射文件，结果映射，数据库环境等等。这个对象是 MyBatis 配置信息的集合，后面可以用它来构建 SqlSessionFactory。
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(SequenceMapper.class);
        // 这里得到一个用于操作id生成器的专属的数据库操作类.
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sw.stop();
        log.debug(sw.prettyPrint());
    }

    /**
     * 定时更新缓存, 这个是必要的, 需要定时更新 key
     */
    private void updateCacheFromDbAtEveryMinute() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("check-idCache-thread");
            t.setDaemon(true);
            return t;
        });
        scheduledExecutorService.scheduleWithFixedDelay(SegmentIdGenerator.this::updateCacheFromDb, 60L, 60L, TimeUnit.SECONDS);
    }

    /**
     * 初始化cache, 需要不定时更新, 删除已经不存在的key, 初始化新增的key
     * author: hulei42031
     * date: 2024-02-27 10:59
     */
    private void updateCacheFromDb() {
        StopWatch sw = new StopWatch("updateCacheFromDb");
        sw.start();
        try {
            // 1. 获取所有标签
            List<String> dbTags = this.getAllTags();
            if (CollectionUtil.isEmpty(dbTags)) {
                return;
            }

            List<String> cacheTags = new ArrayList<>(this.cache.keySet());
            Set<String> insertTags = new HashSet<>(dbTags);
            Set<String> removeTags = new HashSet<>(cacheTags);

            // 移除 insertTags 在缓冲中已有的 tag, 避免重复初始化
            cacheTags.forEach(insertTags::remove);

            // 把 insertTags 加入缓存
            for (String insertTag : insertTags) {
                SegmentBuffer buffer = new SegmentBuffer();
                // 设置 tag
                buffer.setKey(insertTag);
                // 获取当前的步长信息
                Segment segment = buffer.getCurrent();
                // 这里简单的初始化一个segment, 在后续会直接使用, 避免segment内部的值不符合预期
                segment.setValue(new AtomicLong(0L));
                segment.setMax(0L);
                segment.setStep(0);

                this.cache.put(insertTag, buffer);
            }
            // 移除 removeTags 在 db查出来的 tag的交际, 剩下的相当于在数据库已经没了, 需要在缓存中移除
            dbTags.forEach(removeTags::remove);
            // 在 cache 中移除已经没有的 tag
            removeTags.forEach(cacheTags::remove);

        } catch (Exception e) {
            log.warn("更新缓存异常", e);
        } finally {
            sw.stop();
            log.debug(sw.prettyPrint());
        }
    }


    public GenResult getId(String key) {
        // 当前 buffer 尚未初始化完成 按理来说, 这里压根不会触发, 初始化未完成的阶段项目还在启动
        if (!this.initOk) {
            return new GenResult(GenResultEnum.INIT_NOT_COMPLETE.getId(), GenResultEnum.INIT_NOT_COMPLETE.getResult());
        }
        // 当前缓存内没有找到相关的表
        if (!this.cache.containsKey(key)) {
            return new GenResult(GenResultEnum.KEY_NOT_FOUND.getId(), GenResultEnum.KEY_NOT_FOUND.getResult());
        }

        /*
         * 其实上面两种情况出现比较少, 一般都不会出现, 那么会直接到这个流程
         * 这是一个初始化的流程, 每一个 buffer都必经的一个过程
         */
        SegmentBuffer buffer = this.cache.get(key);
        // 双重检查锁定
        if (!buffer.isInitOk()) {
            synchronized (buffer) {
                if (!buffer.isInitOk()) {
                    // 正式开始初始化
                    try {
                        // 其实我不太理解为什么这里非要传一个 Segment 进去, 然后在里面又要获取这个 buffer
                        this.updateSegmentFromDb(key, buffer.getCurrent());
                        log.info("buffer初始化完成");
                        // 只会在这里被设置一次, 即意味着 buffer 从此之后便初始化完成
                        buffer.setInitOk(true);
                    } catch (Exception e) {
                        log.warn("buffer初始化异常", e);
                    }
                }
            }
        }

        return this.getIdFromSegmentBuffer(this.cache.get(key));
    }

    private GenResult getIdFromSegmentBuffer(SegmentBuffer buffer) {

        int spin = 0;
        while (true) {

            // 当前buffer加读锁, 也就意味着, 多个线程可以同时获取同一个键的分布式id
            buffer.getLock().readLock().lock();

            long value;
            Segment segment;
            // 1. 一阶段获取 ID
            label:
            {
                try {
                    // 获取当前的segment
                    segment = buffer.getCurrent();
                    // 判断是否需要开始准备下一个segment
                    if (!buffer.isNextReady()
                            && segment.getIdle() < 0.9 * segment.getStep()
                            && buffer.getThreadRunning().compareAndSet(false, true)) {
                        /*
                         * 进入这里需要满足三个条件
                         * 1. 下一个segment依旧不可用(1.初始化后, buffer下一个是不可用的, 因为还没准备好. 2.buffer切换segment的时候下一个是不可用的, 因为被用完的 segment到下一位去了)
                         * 2. 最大值-当前value < 0.9倍步长(这就是一个阈值判断, 如果当前的 segment 用了 10% 了, 就尝试准备下一个 segment)
                         * 3. 当前是否有线程正在计算步长(并发控制, 仅允许一个线程来更新这个 buffer的状态)
                         * 这三个提交意味着 当前的 segment 已经快要不可用了, 急需切换一个 segment
                         */
                        this.readyNext(buffer);
                    }
                    // 此处意味着 已经完成了获取id前的准备操作, 不管下一个segment是不是可用, 都已经准备好了 可以随时切换使用
                    // 直接尝试从当前 segment 获取分布式 ID
                    value = segment.getValue().getAndIncrement();
                    // 能够获取的最大id 不能 >= 数据库的最大 id
                    if (value >= segment.getMax()) {
                        // 当前 segment 能到的 ID 已经不符合要求了, 退出这个流程, 执行二阶段获取 ID
                        break label;
                    }
                    // 成功拿到一个可用的分布式 ID
                    log.info("一阶段成功获得id, {}, 当前是第 {} 个 segment", value, buffer.getCurrentPos());
                    return new GenResult(value, "success");
                } finally {
                    // 释放读锁, 在一阶段获取 ID 的线程互不影响
                    buffer.getLock().readLock().unlock();
                }
            }


            // 二阶段获取 ID - 只有在一阶段获取 ID 失败的线程才会进行二阶段
            // 这里有必要等吗? 在并发量特别大的情况下, 大量的线程在一阶段获取 ID会失败, 都会在这里尝试等待
            // 在这里等待一下其实是有必要的, 为了让一阶段的线程更容易获取锁的使用权
            this.waitAndSleep(buffer);
            /*
             * 更新已经完成, 再次尝试获取分布式 ID
             * 这里的写锁是为什么?
             * 1. 如果一阶段依旧在准备segment, 那么将阻塞等待准备好
             *
             * todo
             * 1. 这里和一阶段获取 ID 互斥, 和 readyNext()互斥(即和一阶段所有操作互斥)
             * 2. 二阶段的所有线程会互相互斥
             */
            buffer.getLock().writeLock().lock();
            try {
                /*
                 * 获取当前可用的 segment
                 * Q:这里有必要吗?如果到了这个阶段, 那这里在当前的 segment必然获取不到 ID了
                 * A:不是的, 高并发下, 这里挤压了很多获取ID的请求, 当第一个进来的会进行切换 segment 的操作, 挤压的请求在这里获取 ID
                 *
                 * 大量积压的请求在这里获取 ID, 但是只有一部分能够成功获取
                 * 并且二阶段的所有的线程会和一阶段的读操作和一阶段的 readyNext()抢锁
                 * 1. 也就是说二阶段和一阶段会同时在当前的 segment 内获取 ID
                 * 2. 当 segment 拿不到 ID的时候, 二阶段的线程尝试切换 segment, 以便于二阶段剩余的线程能够拿到 ID,
                 *      但是如果一阶段的 readyNext()没抢到锁去准备好第二个segment, 那么二阶段的线程会一直报错(两个segment都不可用)
                 *      todo 这里是否可以考虑阻塞一下? 二阶段的报错线程集体阻塞, 那么一阶段的线程更容易准备好下一个 segment
                 */
                segment = buffer.getCurrent();
                value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    log.info("二阶段成功获得id, {} , 当前是第 {} 个 segment", value, buffer.getCurrentPos());
                    return new GenResult(value, "success");
                }
                // value >= 最大值 && buffer.isNextReady() = true 代表当前当前的 segment 不可用, 并且下一个 segment 已经准备好
                if (buffer.isNextReady()) {
                    // * 在这里切换之前, 所有获取
                    buffer.switchPos();
                    /*
                    设置当前的 segment 已经不可以再获取分布式 ID 了
                    这是在当前 segment 生成的 id 已经大于等于最大值的情况下才设置这个值
                    也就是说对于 nextReady 这个值
                    1. 在程序启动后的第一次时间内一直到第一次 segment 的阈值小于0.9这段时间是 false (由于第一个 segment 可以用. 没必要去准备第二个)
                    2. 一旦第二个 segment 被初始化好后一直到第一个被用完这短时间内是 true (当前的 segment 达到阈值0.9的时候就已经开始准备第二个segment了)
                    3. 当第一个被用完的一瞬间, 这个值又开始是 false
                    也就是说这个值是用于标记下一个 segment 是否被准备好
                     */
                    buffer.setNextReady(false);
                    // 这里只做 segment 的切换, 让后续获取 ID 的操作全部回归的 阶段一去获取 ID
                    continue;
                }
                // value >= 最大值 && buffer.isNextReady() = false 代表着不仅当前的 segment 不可用, 并且下一个 segment 还没有准备好
                log.error("用于生成分布式ID的两个 segment 都不可用!继续获取 ID");
                if (spin++ < 3) {
                    continue;
                }
                // 自旋三次不再自旋, 抛出异常
                return new GenResult(GenResultEnum.NOT_READY.getId(), GenResultEnum.NOT_READY.getResult());
            } finally {
                buffer.getLock().writeLock().unlock();
            }
        }

    }

    /**
     * 准备 buffer 的下一个 segment
     *
     * @param buffer buffer
     */
    private void readyNext(SegmentBuffer buffer) {
        this.service.execute(() -> {
            // 加上当前buffer的写锁, 即意味着同一个buffer只能同时进行一个 readyNext 操作
            buffer.getLock().writeLock().lock();
            // 获取下一个 segment, 并进行更新操作
            Segment next = buffer.getSegments()[buffer.nextPos()];
            boolean updateOk = false;
            try {
                // 更新当前的 segment 和 buffer
                SegmentIdGenerator.this.updateSegmentFromDb(buffer.getKey(), next);
                // 更新完成
                updateOk = true;
            } catch (Exception e) {
                log.warn("更新segment出现异常! ", e);
            } finally {
                if (updateOk) {
                    // 这里意味着 buffer 已经设置好了三个参数, 1.更新时间 2.步长 3.最小步长, buffer 已经准备好了下一次获取 id 的操作
                    buffer.setNextReady(true);
                }
                // 当前的更新操作已经结束, 清除当前的线程执行标记
                buffer.getThreadRunning().set(false);
                // 解锁
                buffer.getLock().writeLock().unlock();
            }
        });
    }

    /**
     * 等待 buffer 更新成功
     *
     * @param buffer 需要等待的 buffer
     */
    private void waitAndSleep(SegmentBuffer buffer) {

        // 这是一个循环等待, 但却会消耗cpu资源
        int roll = 0;
        while (buffer.getThreadRunning().get()) {
            ++roll;
            if (roll > 10000) {
                try {
                    log.info("开始等待...");
                    TimeUnit.MICROSECONDS.sleep(100);
                } catch (InterruptedException e) {
                    log.warn("等待 SegmentBuffer 更新的过程中被异常中断!", e);
                }
                break;
            }
        }
    }

    /**
     * 更新 Segment
     * 1. 更新当前 segment 的 value,最大id值,步长
     * 2. 更新当前 segment 指向的 buffer 的步长, 最小步长, 和最近刷新时间
     *
     * @param key     表名
     * @param segment 当前的 segment
     */
    private void updateSegmentFromDb(String key, Segment segment) {
        // 开始计时
        StopWatch sw = new StopWatch("updateSegmentFromDb");
        sw.start();

        // 获取当前 segment 对应的 buffer
        SegmentBuffer buffer = segment.getBuffer();

        // 这只是一个用于与数据库交互的实体
        LeafAlloc leafAlloc = new LeafAlloc();
        leafAlloc.setKey(key);
        leafAlloc.setStep(defaultStep);

        /*
         * 根据不同的情况更新 buffer
         * 1. 初始化更新 buffer
         * 2. 首次更新 buffer
         * 3. 根据时间力度跟新 buffer
         */
        if (!buffer.isInitOk()) {
            // **初始化buffer, 此方法只会执行一次
            // 更新数据库的最大 ID, 然后得到设置的这个最大 ID 值(同一时间只会有一个buffer拿到这个值)
            long maxId = this.updateIdAndGetId(leafAlloc);
            leafAlloc.setMaxId(maxId);
            // 设置步长
            buffer.setStep(leafAlloc.getStep());
            // 这里使用配置的最小步长, 这里其实只用在这里设置, 毕竟步长没变, 没必要设置, 可以少两行代码
            buffer.setMinStep(leafAlloc.getStep());
        } else if (buffer.getUpdateTimeStamp() == 0L) {
            // **初始化后使用第一个buffer一段时间后, 第一次更新第二个buffer会走这个逻辑
            long maxId = this.updateIdAndGetId(leafAlloc);
            leafAlloc.setMaxId(maxId);
            // 目的是知道当前已经完成了第一次的更新
            buffer.setUpdateTimeStamp(System.currentTimeMillis());
            buffer.setStep(leafAlloc.getStep());
            buffer.setMinStep(leafAlloc.getStep());
        } else {
            /*
             * 根据时间来确定下一次的步长
             * 1. 离上一次更新 segment 的时间小于 900秒(15分钟), 那么步长可以 *2
             * 2. 离上一次更新 segment 的时间大于 1800秒(30分钟), 那么步长可以 /2
             */
            long duration = System.currentTimeMillis() - buffer.getUpdateTimeStamp();
            int nextStep = buffer.getStep();
            if (duration < 900000L) {
                if (nextStep * 2 <= 10000) {
                    nextStep *= 2;
                }
            } else if (duration > 1800000L) {
                if (nextStep / 2 >= buffer.getMinStep()) {
                    nextStep /= 2;
                }
            }

            // 根据最新步长更新数据库的最大 ID
            long id = this.updateIdAndGetId(LeafAlloc.builder().key(key).step(nextStep).build());
            leafAlloc.setMaxId(id);
            // 更新 buffer 的时间
            buffer.setUpdateTimeStamp(System.currentTimeMillis());
            // 下一次的步长
            buffer.setStep(nextStep);
            // 最小步长永远都是预先设定的步长
            buffer.setMinStep(leafAlloc.getStep());
        }

        // 设置当前 segment 的起始值(数据库最大-步长)
        segment.getValue().set(leafAlloc.getMaxId() - buffer.getStep());
        // 设置当前这个 segment 这一次能够生成的最大的 id (不能大于或者等于这个id)
        segment.setMax(leafAlloc.getMaxId());
        // 设置计算出来的下一次的新步长
        segment.setStep(buffer.getStep());
        sw.stop();
        log.info(sw.prettyPrint());
    }

    public List<?> getIdForList(String key, List<?> list) throws IllegalAccessException {
        try {
            if (!CollectionUtils.isEmpty(list)) {
                List<Long> ids = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    ids.add(this.getId(key).getId());
                }
                log.info("tabName: {}, 批量申请id成功. beginId: {}", key, ids.get(0));
                // return seqIdUtils.conVertSeqIdList(list, ids);
                return new ArrayList<>();
            } else {
                log.warn("批量申请id, list为空, 不处理.");
                return list;
            }
        } catch (Exception e) {
            log.error("{}表批量申请 id 异常!", key, e);
            throw e;
        }
    }

    /*************************************db方法**********************************/
    private List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            tags = sqlSession.selectList("com.hundsun.demo.springboot.mapper.SequenceMapper.getAll");
        }
        return tags;
    }

    private long updateIdAndGetId(LeafAlloc leafAlloc) {
        long id;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            log.info("更新步长({})", leafAlloc.getStep());
            sqlSession.update("com.hundsun.demo.springboot.mapper.SequenceMapper.update", leafAlloc);
            id = sqlSession.selectOne("com.hundsun.demo.springboot.mapper.SequenceMapper.get", leafAlloc.getKey());
            sqlSession.commit();
        }
        return id;
    }

    public static class UpdateThreadFactory implements ThreadFactory {
        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "segment-update-" + nextThreadNum());
        }
    }
}

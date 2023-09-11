package com.hundsun.demo.springboot.utils.segmentid;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import java.util.concurrent.ArrayBlockingQueue;
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

@ConditionalOnProperty(value = {"ast.id.generate.strategy"}, havingValue = "segment")
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
    private final SeqIdUtils seqIdUtils = new SeqIdUtils();

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
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(TabSeqMapper.class);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sw.stop();
        sw.prettyPrint();
    }

    /**
     * 定时更新缓存, 这个是必要的, 需要定时更新 key
     */
    private void updateCacheFromDbAtEveryMinute() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("check-idCache-thread");
            // t.setDaemon(true);
            return t;
        });
        scheduledExecutorService.scheduleWithFixedDelay(SegmentIdGenerator.this::updateCacheFromDb, 60L, 60L, TimeUnit.SECONDS);
    }

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

                segment.setValue(new AtomicLong(0L));
                segment.setMax(0L);
                segment.setStep(0);

                this.cache.put(insertTag, buffer);
            }
            // 移除 removeTags 在 db查出来的 tag的交际, 剩下的相当于在数据库已经没了, 需要在缓存中移除
            dbTags.forEach(removeTags::remove);
            // 在 cache 中移除已经没有的 tag, todo 这样移除是否存在问题?
            removeTags.forEach(cacheTags::remove);

        } catch (Exception e) {
            log.warn("更新缓存异常", e);
        } finally {
            sw.stop();
        }
    }


    public GenResult getId(String key) {
        if (!this.initOk) {
            return new GenResult(GenResultEnum.INIT_NOT_COMPLETE.getId(), GenResultEnum.INIT_NOT_COMPLETE.getResult());
        }
        if (!this.cache.containsKey(key)) {
            return new GenResult(GenResultEnum.KEY_NOT_FOUND.getId(), GenResultEnum.KEY_NOT_FOUND.getResult());
        }
        SegmentBuffer buffer = this.cache.get(key);
        if (!buffer.isInitOk()) {
            synchronized (buffer) {
                if (!buffer.isInitOk()) {
                    try {
                        // 更新步长之类的
                        this.updateSegmentFromDb(key, buffer.getCurrent());
                        log.info("buffer初始化完成");
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

        while (true) {

            // 加个读锁
            buffer.getLock().readLock().lock();

            long value;
            Segment segment;
            // 1. 一阶段获取 ID
            label:
            {
                try {
                    // 1. 判断当前 segment 的状态, 并判断是否需要对 buffer 中的下一个 segment 进行更新
                    segment = buffer.getCurrent();
                    if (!buffer.isNextReady()
                            && segment.getIdle() < 0.9 * segment.getStep()
                            && buffer.getThreadRunning().compareAndSet(false, true)
                    ) {
                        /*
                         * 进入这里需要满足三个条件
                         * 1. 当前的 buffer 下一次已不可读
                         * 2. 最大值-当前value < 0.9倍步长
                         * 3. 当前是否有线程正在计算步长
                         * 这三个提交意味着 当前的 segment 已经快要不可用了, 急需切换一个 segment
                         */
                        this.readyNext(buffer);
                    }

                    // 到这里意味着第二个 segment 已经准备好 || 当前的 segment 使用空间还很充足 || 当前的 buffer 是否正在更新 segment
                    // 直接尝试从当前 segment 获取分布式 ID
                    value = segment.getValue().getAndIncrement();
                    if (value >= segment.getMax()) {
                        // 但是拿到的 ID 已经超过了最大值, 这里不能拿了, 要在下面的流程去等待
                        break label;
                    }
                    // 成功拿到一个可用的分布式 ID
                    log.info("阶段1成功获得id,{}, 当前是第 {} 个 segment", value, buffer.getCurrentPos());
                    return new GenResult(value, "success");
                } finally {
                    buffer.getLock().readLock().unlock();
                }
            }


            // 二阶段获取 ID - 所有在一阶段获取 ID失败的线程都将进行这个阶段
            // this.waitAndSleep(buffer);
            /*
             * 更新已经完成, 再次尝试获取分布式 ID
             * 这里的写锁是为什么?
             * 1. 会进行 segment 的切换
             * 2. 这里获取 ID 的优先级优于 第一阶段, 避免因为 第一阶段获取 ID 太快, 导致这里获取不到可以用的 ID 而报错
             *
             * todo 这里和一阶段获取ID互斥, 和 readyNext()互斥
             */
            buffer.getLock().writeLock().lock();
            try {
                /*
                 * 获取当前可用的 segment
                 * Q:这里有必要吗?如果到了这个阶段, 那这里在当前的 segment必然获取不到 ID了
                 * A:不是的, 高并发下, 这里挤压了很多获取ID的请求, 当第一个进来的会进行切换 segment 的操作, 挤压的请求在这里获取 ID
                 */
                segment = buffer.getCurrent();
                value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    log.info("阶段2成功获得id,{} , 当前是第 {} 个 segment", value, buffer.getCurrentPos());
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
                // return new GenResult(GenResultEnum.NOT_READY.getId(), GenResultEnum.NOT_READY.getResult());
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
            // 写锁
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
                // 当前的更新操作已经结束, 这里
                buffer.getThreadRunning().set(false);
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
        int roll = 0;
        while (buffer.getThreadRunning().get()) {
            ++roll;
            if (roll > 10000) {
                try {
                    log.info("开始等待...");
                    TimeUnit.SECONDS.sleep(10L);
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
        StopWatch sw = new StopWatch("updateSegmentFromDb");
        sw.start();
        SegmentBuffer buffer = segment.getBuffer();
        LeafAlloc leafAlloc = new LeafAlloc();
        leafAlloc.setKey(key);
        leafAlloc.setStep(defaultStep);
        long duration;


        if (!buffer.isInitOk()) { // 还没有初始化完成
            // 更新数据库的最大 ID, 然后得到设置的这个最大 ID 值
            long maxId = this.updateIdAndGetId(leafAlloc);
            leafAlloc.setMaxId(maxId);
            // 设置步长
            buffer.setStep(leafAlloc.getStep());
            // 这里使用配置的最小步长
            buffer.setMinStep(leafAlloc.getStep());
        } else if (buffer.getUpdateTimeStamp() == 0L) {
            // 这里会从 166行进来 , 同样的操作, 只是设置了一个时间
            long maxId = this.updateIdAndGetId(leafAlloc);
            leafAlloc.setMaxId(maxId);
            // todo 这个时间是啥意思呢 ?, 很明显上面一次不会设置时间, 那么初始化好之后第一次肯定会走这里
            buffer.setUpdateTimeStamp(System.currentTimeMillis());
            buffer.setStep(leafAlloc.getStep());
            buffer.setMinStep(leafAlloc.getStep());
        } else {
            // 距离上一次更新过去了多久
            // duration = System.currentTimeMillis() - buffer.getUpdateTimeStamp();
            // int nextStep = buffer.getStep();
            // if (duration < 900000L) { // 离上次更新时间小于 900秒(15分钟)
            //     if (nextStep * 2 <= 1000000) { // 如果当前步长*2小于一百万, 那么下一次步长为 当前步长*2
            //         nextStep *= 2;
            //     }
            // } else if (duration > 1800000L) { // 离上一次更新已经过去了 30分钟
            //     // 当前步长/2 大于等于最小步长那么就 /2, 否则不变
            //     nextStep = nextStep / 2 >= buffer.getMinStep() ? nextStep / 2 : nextStep;
            // }

            LeafAlloc temp = new LeafAlloc();
            temp.setKey(key);
            // 计算出来的下一次步长
            temp.setStep(leafAlloc.getStep());
            // 根据这个计算出来的步长来更新最大 ID
            long id = this.updateIdAndGetId(temp);
            leafAlloc.setMaxId(id);
            // 更新 buffer 的时间
            buffer.setUpdateTimeStamp(System.currentTimeMillis());
            // 下一次的步长
            buffer.setStep(leafAlloc.getStep());
            // 最小步长永远都是预先设定的步长
            buffer.setMinStep(leafAlloc.getStep());
        }

        // 这里拿到当前 id的起始值, 并设置这个 id
        duration = leafAlloc.getMaxId() - buffer.getStep();
        segment.getValue().set(duration);
        // 设置当前这个 segment 这一次能够生成的最大的 id
        segment.setMax(leafAlloc.getMaxId());
        // 设置计算出来的下一次的新步长
        segment.setStep(buffer.getStep());
        sw.stop();
        sw.prettyPrint();
    }

    public List<?> getIdForList(String key, List<?> list) throws IllegalAccessException {
        try {
            if (!CollectionUtils.isEmpty(list)) {
                List<Long> ids = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    ids.add(this.getId(key).getId());
                }
                log.info("tabName: {}, 批量申请id成功. beginId: {}", key, ids.get(0));
                return seqIdUtils.conVertSeqIdList(list, ids);
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
            tags = sqlSession.selectList("com.hundsun.amust.web.mapper.TabSeqMapper.getAllTags");
        }
        return tags;
    }

    private long updateIdAndGetId(LeafAlloc leafAlloc) {
        long id;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            log.info("更新步长({})", leafAlloc.getStep());
            sqlSession.update("com.hundsun.amust.web.mapper.TabSeqMapper.updateIdByCustomStep", leafAlloc);
            id = sqlSession.selectOne("com.hundsun.amust.web.mapper.TabSeqMapper.getIdByTag", leafAlloc.getKey());
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

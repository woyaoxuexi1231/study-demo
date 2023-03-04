package com.hundsun.demo.springboot.service.serviceimpl;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.springboot.annotation.TargetDataSource;
import com.hundsun.demo.springboot.dynamic.DynamicDataSourceType;
import com.hundsun.demo.springboot.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.model.pojo.EmployeeDO;
import com.hundsun.demo.springboot.service.SimpleService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.service.serviceimpl
 * @className: SimpleServiceImpl
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:32
 */

@Service
@Slf4j
public class SimpleServiceImpl implements SimpleService {

    @Resource
    EmployeeMapper employeeMapper;

    @Autowired
    SimpleService simpleService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    ThreadPoolExecutor SINGLE_TRANSACTION_POOL = new ThreadPoolExecutor(
            10,
            10,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            new ThreadFactoryBuilder().setNamePrefix("SINGLE-TRANSACTION-POOL-").build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    ThreadPoolExecutor singlepool = new ThreadPoolExecutor(
            1,
            1,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            new ThreadFactoryBuilder().setNamePrefix("singlepool-").build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public ResultDTO<?> multiDataSourceSingleTransaction() {

        /*
        1. 双数据源, 一个写事务, 一个读事务, 这种情况, 双线程可以实现事务正常
        2. 双数据源, 都为写事务, 使用本地事务是实现不了的
        */

        /*
        3.13.6 版本的 redisson-spring-boot-starter
        1. pool - 一个核心数和最大数都为 3 的线程池, 日志显示这种写法导致第一个拿到锁的释放失败, 且后面有两个线程同时拿到了锁
            if(lock.tryLock()){
                pool.execute(
                    pool.execute(任务1)
                    pool.execute(任务2)
                    lock.unlock()
                )
            }
        2. singlepool - 单线程池, pool 两个线程的线程池, 日志显示这种写法导致锁的频繁释放失败, 释放的锁的线程已经不持有锁了
            if(lock.tryLock()){
                singlepool.execute(
                    pool.execute(任务1)
                    pool.execute(任务2)
                    lock.unlock()
                )
            }
        这意味着, 解铃还须系铃人, 谁他妈上的锁谁来释放, 这里把锁都拿到其他线程去释放了, 不报错都怪了
        redisson 的 tryLock方法, 如果是同一个线程, tryLock方法会返回 true
        */

        // 1. 确保单个服务同时已有一个线程在执行, 且不阻塞
        singlepool.execute(() -> {

            log.info("尝试获取分布式锁...");
            RLock rLock = redissonClient.getLock("multidatasource::singletransaction::lock");

            // 2. 确保多个服务同时只有一个能获取锁
            if (rLock.tryLock()) {

                // 这里同时只会有一个线程操作, 判断状态
                if (stringRedisTemplate.opsForValue().get("multidatasource::singletransaction::state") != null) {
                    // 直接释放锁
                    log.info("60s内已完成, 请稍后再试! ");
                    rLock.unlock();
                    return;
                }

                // 锁释放的计时器
                CountDownLatch count = new CountDownLatch(2);

                // 需要插入的数据
                List<EmployeeDO> employeeDOS = new ArrayList<>();
                // 控制主数据源流程
                Semaphore masterSemaphore = new Semaphore(0);
                // 控制从数据源流程
                Semaphore slaveSemaphore = new Semaphore(1);
                // 整个流程是否结束
                AtomicBoolean isFinished = new AtomicBoolean(false);
                log.info("获取分布式锁成功, 开始执行流程. ");

                // 从数据源操作
                SINGLE_TRANSACTION_POOL.execute(() -> {
                    simpleService.selectFromSlave(employeeDOS, masterSemaphore, slaveSemaphore, isFinished);
                    count.countDown();
                });

                // 主数据源操作
                SINGLE_TRANSACTION_POOL.execute(() -> {
                    simpleService.copySlaveToMaster(employeeDOS, masterSemaphore, slaveSemaphore, isFinished);
                    count.countDown();
                });

                try {
                    count.await();
                    stringRedisTemplate.opsForValue().set("multidatasource::singletransaction::state", "Initializing", 60, TimeUnit.SECONDS);
                    rLock.unlock();
                    log.info("释放分布式锁完成. ");
                } catch (Exception e) {
                    log.error("释放分布式锁出现异常! ", e);
                }
            }
        });

        return ResultDTOBuild.resultDefaultBuild();
    }

    @Override
    @Transactional
    @TargetDataSource(dataSourceType = DynamicDataSourceType.MASTER)
    public void copySlaveToMaster(List<EmployeeDO> employeeDOS, Semaphore masterSemaphore, Semaphore slaveSemaphore, AtomicBoolean isFinished) {

        employeeMapper.delete(new EmployeeDO());

        while (true) {

            try {

                // 等待被唤醒
                masterSemaphore.acquire();

                // 判断整个流程是否结束
                if (isFinished.get()) {
                    break;
                }

                Thread.sleep(5 * 1000);
                // 正常插入数据
                log.info("开始插入数据");
                if (!employeeDOS.isEmpty()) {
                    employeeMapper.insertList(employeeDOS);
                }
                // 插入完成后清空数组
                employeeDOS.clear();

                // 唤醒从数据源, 让其查询
                slaveSemaphore.release();
                log.info("等待从数据源查询数据...");
            } catch (Exception e) {

                log.error("插入数据异常! ", e);
                // 插入数据出现异常, 终止整个流程
                isFinished.set(true);
                // 唤醒从数据源, 让其终止操作
                log.info("唤醒从数据源");
                slaveSemaphore.release();
                // 抛出异常, 终止循环
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional
    @TargetDataSource(dataSourceType = DynamicDataSourceType.SECOND)
    public void selectFromSlave(List<EmployeeDO> employeeDOS, Semaphore masterSemaphore, Semaphore slaveSemaphore, AtomicBoolean isFinished) {

        // 分页查询
        int pageNum = 1;
        int pageSize = 10;

        while (true) {

            try {

                // 等待被唤醒
                slaveSemaphore.acquire();

                // 流程结束
                if (isFinished.get()) {
                    break;
                }
                log.info("开始查询从数据源数据");
                PageHelper.startPage(pageNum, pageSize);
                employeeDOS.addAll(employeeMapper.selectAll());
                log.info("本次一共查询 {} 条数据", employeeDOS.size());

                // 已经查询所有的数据
                if (employeeDOS.isEmpty()) {
                    log.info("数据查询完成");
                    isFinished.set(true);
                    // 所有数据查询完毕, 唤醒主数据源事务, 准备结束
                    masterSemaphore.release();
                    break;
                }

                // 准备查询下一页数据
                pageNum++;

                // 唤醒主数据源流程, 等待插入完成
                masterSemaphore.release();
                log.info("等待主数据源插入数据...");

            } catch (Exception e) {

                log.error("查询数据异常! ", e);
                // 查询数据异常, 整个流程结束
                isFinished.set(true);
                // 唤醒主数据源, 让其终止操作
                log.info("唤醒主数据源");
                masterSemaphore.release();
                // 抛出异常, 终止循环
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void springRedis() {


    }
}

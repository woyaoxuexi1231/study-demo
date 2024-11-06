package org.hulei.springboot.spring.transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import org.hulei.common.mapper.entity.pojo.EmployeeDO;
import org.hulei.common.core.model.dto.ResultDTO;
import org.hulei.common.core.utils.ResultDTOBuild;
import lombok.extern.slf4j.Slf4j;
import org.hulei.common.mapper.mapper.EmployeeMapperPlus;
import org.hulei.springdata.routingdatasource.annotation.TargetDataSource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: DbController
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 14:47
 */

@Slf4j
@RestController
@RequestMapping("/db")
public class DbController {

    @Resource
    EmployeeMapperPlus employeeMapper;

    @Autowired
    DbController dbController;

    @Autowired
    ThreadPoolExecutor singlePool;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    ThreadPoolExecutor commonPool;

    /**
     * 双数据源不使用分布式事务如何保证事务
     */
    @GetMapping("/multiDataSourceSingleTransaction")
    public ResultDTO<?> multiDataSourceSingleTransaction() {

        /*
        1. 双数据源, 一个写事务, 一个读事务, 这种情况, 双线程可以实现事务正常
        2. 双数据源, 都为写事务, 使用本地事务是实现不了的
        */

        // 1. 确保单个服务同时已有一个线程在执行, 且不阻塞
        singlePool.execute(() -> {

            log.info("尝试获取分布式锁...");
            RLock rLock = redissonClient.getLock("multidatasource::singletransaction::lock");

            // 2. 确保多个服务同时只有一个能获取锁
            if (rLock.tryLock()) {
                try {
                    // 先删除数据,以备插入数据
                    employeeMapper.delete(new LambdaQueryWrapper<>());

                    // 锁释放的计时器,只有当两个任务都完成之后,整个任务才停止
                    CountDownLatch count = new CountDownLatch(2);
                    // 这是两个操作共享的一个数据列表, 操作A查数据插入这个list, 操作B从这个list取数据插入数据库
                    // 这个列表不用考虑并发,同时只会有一个线程操作他
                    List<EmployeeDO> employeeDOS = new ArrayList<>();
                    // 控制主数据源流程的信号量(插入数据), 先不执行, 等待从流程触发这个信号量让主流程获得执行权限
                    Semaphore insertSemaphore = new Semaphore(0);
                    // 控制从数据源流程, 先执行这个流程
                    Semaphore selectSemaphore = new Semaphore(1);
                    // 整个流程是否结束,至于为什么需要这个变量,考虑下面的场景
                    // 流程A查询结束了,等待流程B来激活,但是流程B却因为异常而中断了,那么流程A将永远等待
                    AtomicBoolean isFinished = new AtomicBoolean(false);
                    log.info("获取分布式锁成功, 开始执行流程. ");
                    // 从数据源操作
                    commonPool.execute(() -> {
                        try {
                            dbController.selectFromDB(employeeDOS, insertSemaphore, selectSemaphore, isFinished);
                        } finally {
                            count.countDown();
                        }
                    });
                    // 主数据源操作
                    commonPool.execute(() -> {
                        try {
                            dbController.insertToDB(employeeDOS, insertSemaphore, selectSemaphore, isFinished);
                        } finally {
                            count.countDown();
                        }
                    });
                    count.await();
                    // stringRedisTemplate.opsForValue().set("multidatasource::singletransaction::state", "Initializing", 60, TimeUnit.SECONDS);
                    log.info("操作成功");
                } catch (Exception e) {
                    log.error("操作出现异常", e);
                } finally {
                    rLock.unlock();
                }
            }
        });

        return ResultDTOBuild.resultDefaultBuild();
    }

    @Transactional
    @TargetDataSource(value = "first")
    public void insertToDB(List<EmployeeDO> employeeDOS, Semaphore insertSemaphore, Semaphore selectSemaphore, AtomicBoolean isFinished) {
        while (true) {
            try {
                // 尝试获取写信号
                insertSemaphore.acquire();
                // 判断整个流程是否结束
                if (isFinished.get()) {
                    break;
                }
                // 正常插入数据
                log.info("开始插入数据");
                if (!employeeDOS.isEmpty()) {
                    employeeDOS.forEach(employeeMapper::insert);
                }
                // 插入完成后清空数组
                employeeDOS.clear();
                // 唤醒从数据源, 让其查询
                selectSemaphore.release();
                log.info("等待从数据源查询数据...");
            } catch (Exception e) {
                log.error("插入数据异常! ", e);
                // 插入数据出现异常, 终止整个流程
                isFinished.set(true);
                selectSemaphore.release();
                // 抛出异常, 终止循环
                break;
            }
        }
    }

    @Transactional
    @TargetDataSource(value = "third")
    public void selectFromDB(List<EmployeeDO> employeeDOS, Semaphore insertSemaphore, Semaphore selectSemaphore, AtomicBoolean isFinished) {
        // 分页查询
        int pageNum = 1;
        int pageSize = 10;
        while (true) {
            try {
                // 尝试获取读的信号量,读信号量就绪代表当前读操作是被允许的
                selectSemaphore.acquire();
                // 被唤醒时首先检测流程是否结束了 1.写入操作报错了,没必要再读了
                if (isFinished.get()) {
                    break;
                }
                log.info("开始查询数据");
                PageHelper.startPage(pageNum, pageSize);
                employeeDOS.addAll(employeeMapper.selectList(Wrappers.lambdaQuery()));
                log.info("本次一共查询 {} 条数据", employeeDOS.size());
                // 已经查询所有的数据
                if (employeeDOS.isEmpty()) {
                    log.info("数据查询结束,所有数据都已经查询完毕!");
                    isFinished.set(true);
                    // 所有数据查询完毕, 唤醒主数据源事务, 准备结束
                    insertSemaphore.release();
                    break;
                }
                // 准备查询下一页数据
                pageNum++;
                // 唤醒主数据源流程, 等待插入完成
                insertSemaphore.release();
                log.info("等待主数据源插入数据...");
            } catch (Exception e) {
                log.error("查询数据异常! ", e);
                // 查询数据异常, 整个流程结束
                isFinished.set(true);
                insertSemaphore.release();
                // 抛出异常, 终止循环
                break;
            }
        }
    }
}

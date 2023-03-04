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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @SneakyThrows
    public void mysqlSelect() {

        /*
        MVCC - 多版本并发控制
        快照读 - 普通的 select查询 SQL语句
        当前读 - 执行 insert, update, delete, select for update, select lock in share mode时进行读取数据的方式

        ReadView - 快照读sql 执行时 mvcc提取数据的依据, 就是在执行查询 sql的时候一个事务表, 通过事务表和数据保存的事务信息, 来判断当前的 sql应该获取哪个时刻的数据才是安全的
            m_ids - 当前活跃的事务编号, 还没有被提交的事务
            min_trx_id - 最小活跃事务编号
            max_trx_id - 预分配事务编号, 当前最大的事务编号 + 1
            creator_trx_id - ReadView创建者的事务编号
        可重复读和读提交 - 基于 undo_log 的版本链, 在表内会额外的增加两个字段 trx_id(这数据属于哪个事务编号, 修改操作的事务编号), db_roll_ptr(指向上一个进行版本变化时的数据镜像)
            1. 判断当前事务编号是否等于 creator_trx_id, 成立说明是当前事务更改的, 直接返回 - 也就是说, 第一步判断当前数据库的数据是不是当前事务所修改的, 自己修改的数据自己肯定可以访问的
            2. 判断 trx_id < min_trx_id, 成立说明数据已经提交, 可以返回 - 这个说明我们的 ReadView 最小的事务都要比这个数据的事务小, 对于当前的我们来说, 这个数据百分百安全
            3. 判断 trx_id > max_trx_id, 成立则说明事务是在 ReadView 生成之后才开启的, 不允许访问数据 - 当前数据库里的数据, 是在我们的 ReadView 生成之后才被提交过, 我们不能在过去查看未来的数据, 所以是不可以用这个数据的
            4. 判断 min_trx_id <= trx_id <= max_trx_id, 成立则在 m_ids 对比, 如果不存在数据则代表是已提交的, 可以访问 - 这个数据如果是在最小事务和最大事务之间修改的, 就是说这个数据是在这个 ReadView 被创建的期间被改的, 如果不在活跃事务里, 就说明数据对于当前的 ReadView 来说安全, 当然可以访问
            读提交(RC)每次都会生成 ReadView 来获取数据, 也就意味着, 在同一个事务内, 会出现不可重复读
            可重复读(RR)仅在第一次会生成 ReadView, 之后都会复用这个 ReadView, 所以这意味着避免了不可重复读和幻读
                但是有特例, 在同一个事务内, 两次快照读中间穿插一次当前读会导致幻读, 这种情况, 会在第二次快照读的时候重新创建 ReadView
                案例: 在第一次查询的时候创建 ReadView, 然后事务二新插入了一条数据并提交成功, 事务一更新全表数据的某个字段, 然后执行查询操作这时就产生了幻读
         */

        // for (int i = 0; i < 2; i++) {
        System.out.println("----------------------" + new Date() + "----------------------");
        List<EmployeeDO> employeeDOS = jdbcTemplate.query("select * from employees where lastName like '%M%' for update", new BeanPropertyRowMapper<>(EmployeeDO.class));
        employeeDOS.forEach(System.out::println);
        System.out.println("--------------------------------------------");
        // Thread.sleep(5 * 1000);
        // }
        // jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1003,'M','D','x6789','222@qq.com','1',1002,'sss')");
        jdbcTemplate.execute("delete from employees where employeeNumber = 1003");
        Thread.sleep(5 * 1000);
        System.out.println("----------------------" + new Date() + "----------------------");
        List<EmployeeDO> employeeDOS2 = jdbcTemplate.query("select * from employees where lastName like '%M%' for update", new BeanPropertyRowMapper<>(EmployeeDO.class));
        employeeDOS2.forEach(System.out::println);
        System.out.println("--------------------------------------------");

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @SneakyThrows
    public void mysqlUpdate() {
        // jdbcTemplate.execute("update employees set lastName = 'Murph4' where employeeNumber = 1002");
        // jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1003,'M','D','x6789','222@qq.com','1',1002,'sss')");
        jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1800,'M','D','x6789','222@qq.com','1',1002,'sss')");
        // Thread.sleep(10 * 1000);
        // throw new RuntimeException("提交报错");
    }
}

package com.hundsun.demo.springboot.service.serviceimpl;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.springboot.annotation.TargetDataSource;
import com.hundsun.demo.springboot.dynamic.DynamicDataSourceType;
import com.hundsun.demo.springboot.dynamic.DynamicDataSourceTypeManager;
import com.hundsun.demo.springboot.mapper.AutoKeyTestMapper;
import com.hundsun.demo.springboot.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.model.pojo.EmployeeDO;
import com.hundsun.demo.springboot.service.SimpleService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.util.ArrayList;
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @SneakyThrows
    public void mysqlSelect() {

        /*
        select for update
        如果查询条件用了索引/主键，那么select … for update就会进行行锁。
        如果是普通字段(没有索引/主键)，那么select … for update就会进行锁表。
         */
        // List<EmployeeDO> employeeDOS = jdbcTemplate.query("select * from employees where lastName like '%M%' for update ", new BeanPropertyRowMapper<>(EmployeeDO.class));
        List<EmployeeDO> employeeDOS = jdbcTemplate.query("select * from employees where employeeNumber < 1600 for update", new BeanPropertyRowMapper<>(EmployeeDO.class));
        employeeDOS.forEach(i -> log.info(i.toString()));
        System.out.println("--------------------------------------------");
        // jdbcTemplate.execute("update employees set lastName = 'Murph4' where employeeNumber = 1003");

        Thread.sleep(60 * 1000);
        List<EmployeeDO> employeeDOS2 = jdbcTemplate.query("select * from employees where employeeNumber < 1600", new BeanPropertyRowMapper<>(EmployeeDO.class));
        employeeDOS2.forEach(i -> log.info(i.toString()));
        System.out.println("--------------------------------------------");

    }

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    // @Transactional(isolation = Isolation.READ_COMMITTED)
    @SneakyThrows
    public void mysqlUpdate() {

        // 手动开启事务
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);

        try {
            log.info("开始更新...");
            jdbcTemplate.execute("update employees set lastName = 'Murph3' where employeeNumber = 1003");
            // jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1003,'M','D','x6789','222@qq.com','1',1002,'sss')");
            // jdbcTemplate.execute("insert into employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle)  values (1800,'M','D','x6789','222@qq.com','1',1002,'sss')");
            // List<EmployeeDO> employeeDOS2 = jdbcTemplate.query("select * from employees where employeeNumber < 1600", new BeanPropertyRowMapper<>(EmployeeDO.class));
            // employeeDOS2.forEach(i -> log.info(i.toString()));
            // System.out.println("--------------------------------------------");
            log.info("更新完成! ");
            // Thread.sleep(10 * 1000);
            throw new RuntimeException("提交报错");
        } catch (Exception e) {
            log.error("更新异常! ", e);
            status.setRollbackOnly();
        }

        if (status.isRollbackOnly()) {
            transactionManager.rollback(status);
            log.info("回滚完成! ");
        } else {
            transactionManager.commit(status);
            log.info("提交完成! ");
        }

    }

    @Override
    public void mybatis() {

        /*
        $ 和 # 号的区别
        我们都知道 #号可以防止 SQL 注入
        原理其实就是 #号占位符把位置解析成 ?, 填值的时候对特殊字符进行转移, 外加一些必要的符号, 字符串自动加单引号, 使用 PreparedStatement
        而 $ 符号不做任何处理直接写入, 这使得拥有更大的灵活性, 但是却引入了 SQL 注入
         */
        // insert into ${tableName}
        // values ( ${employeeNumber}, ${lastName}, ${firstName}, ${extension}, ${email}, ${officeCode}, ${reportsTo}, ${jobTitle})
        // values ( #{employeeNumber}, #{lastName}, #{firstName}, #{extension}, #{email}, #{officeCode}, #{reportsTo}, #{jobTitle})
        EmployeeDO employeeDO = new EmployeeDO();
        // employeeDO.setTableName("employees");
        employeeDO.setEmployeeNumber(2001);
        employeeDO.setLastName("'mybatis'");
        employeeDO.setFirstName("'dollar'");
        employeeDO.setExtension("'?'");
        employeeDO.setEmail("'?'");
        employeeDO.setOfficeCode("'?'");
        employeeDO.setReportsTo(1002);
        employeeDO.setJobTitle("'?'");
        employeeMapper.insertWithDollar(employeeDO);
    }

    @Override
    public void testMysqlAutoKey() {
        // 主库插入一千条数据
        // 每次插入100条, 插入10次
        /*
        数据库的 binlog_format=row, 是基于行复制的, 这里并没有出现主从复制不一致的情况

        我这里测试在从库删除一条数据之后, 是否还会进行同步, 如果操作的数据不包含主从不一致的数据对于主从复制是不影响的
        但是如果包含从库没有的数据, 那么会报错, 并停止主从同步
        通过 show slave status \G; 和 select * from performance_schema.replication_applier_status_by_worker\G;
        可以看到报错信息显示
        Coordinator stopped because there were error(s) in the worker(s). The most recent failure being: Worker 1 failed executing transaction 'ANONYMOUS' at master log mysql-bin.000025, end_log_pos 2204. See error log and/or performance_schema.replication_applier_status_by_worker table for more details about this failure or others, if any.
        Worker 1 failed executing transaction 'ANONYMOUS' at master log mysql-bin.000025, end_log_pos 2204; Could not execute Update_rows event on table test.autokeytest; Can't find record in 'autokeytest', Error_code: 1032; handler error HA_ERR_KEY_NOT_FOUND; the event's master log mysql-bin.000025, end_log_pos 2204
        主库要更新的数据在对应的表内从库是没有的
        通过 set global sql_slave_skip_counter=1; 提过这条错误, 但是主库依旧不做任何修改, 然后主从复制可以恢复, 但是如果继续操作那一条被从库删除的数据依旧会让主从复制停止
         */
        for (int i = 1; i <= 10; i++) {

            List<AutoKeyTest> list = new ArrayList<>();

            for (int j = 1; j < 100; j++) {
                AutoKeyTest autoKeyTest = new AutoKeyTest();
                autoKeyTest.setA(i + "," + j);
                autoKeyTest.setB(i + "," + j);
                list.add(autoKeyTest);
            }

            new Thread(() -> {
                DynamicDataSourceTypeManager.set(DynamicDataSourceType.SECOND);
                autoKeyTestMapper.insertList(list);
            }).start();

        }
    }

    @Resource
    AutoKeyTestMapper autoKeyTestMapper;

    @Table(name = "autokeytest")
    @Data
    public static class AutoKeyTest {
        private String a;
        private String b;
    }
}

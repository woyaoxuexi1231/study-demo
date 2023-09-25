package com.hundsun.demo.springboot.controller;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.springboot.dynamic.DynamicDataSourceType;
import com.hundsun.demo.springboot.dynamic.DynamicDataSourceTypeManager;
import com.hundsun.demo.springboot.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.mapper.SequenceMapper;
import com.hundsun.demo.springboot.model.pojo.EmployeeDO;
import com.hundsun.demo.springboot.service.SimpleService;
import com.hundsun.demo.springboot.utils.segmentid.GenResult;
import com.hundsun.demo.springboot.utils.segmentid.GenResultEnum;
import com.hundsun.demo.springboot.utils.segmentid.SegmentIdGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: SimpleController
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:30
 */
@RestController
@Slf4j
public class SimpleController implements ApplicationContextAware {

    @Autowired
    SimpleService simpleService;

    @Resource
    EmployeeMapper employeeMapper;

    /**
     * 双数据源不使用分布式事务如何保证事务
     */
    @GetMapping("/multiDataSourceSingleTransaction")
    public ResultDTO<?> multiDataSourceSingleTransaction() {
        return simpleService.multiDataSourceSingleTransaction();
    }

    @GetMapping("/mysqlSelect")
    public void mysqlSelect() {
        simpleService.mysqlSelect();
    }

    @GetMapping("/mysqlUpdate")
    public void mysqlUpdate() {
        simpleService.mysqlUpdate();
    }

    @GetMapping("/mybatis")
    public void mybatis() {
        // simpleService.mybatis();
        EmployeeDO employeeDO = new EmployeeDO();
        // employeeDO.setEmployeeNumber(System.currentTimeMillis());
        employeeDO.setLastName("n");
        employeeDO.setFirstName("a");
        employeeMapper.insertSelective(employeeDO);
    }

    @GetMapping("/testMysqlAutoKey")
    public void testMysqlAutoKey() {
        simpleService.testMysqlAutoKey();
    }

    @GetMapping("/transactionInvalidation")
    public void transactionInvalidation() {
        DynamicDataSourceTypeManager.set(DynamicDataSourceType.SECOND);
        simpleService.transactionInvalidation();
    }

    @GetMapping("/pageHelper")
    public void pageHelper() {
        simpleService.pageHelper();
        // DynamicDataSourceTypeManager.set(DynamicDataSourceType.SECOND);
        // simpleService.pageHelper();

    }


    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    SqlSessionFactory sqlSessionFactory;

    final Object object = new Object();

    @PostConstruct
    public void Init() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(SequenceMapper.class);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    public void seqSession() {
        String value;
        synchronized (object) {
            /*
             * 假设有一个 value 值初始值是 100 ,更新操作是把这个 value + 100
             * 1. 这里需要使用 init 方法里的步骤自己开启这个 sqlSessionFactory, 如果直接注入 sqlSessionFactory来开启, 事务会被 spring托管, 手动提交将失效
             * 2. 在可重复读的事务隔离界别下, 进程 A执行 update之后(200), 不影响进程B的查询结果(即进程B查到的依旧是快照的数据, 即 100),
             *  当进程B执行了 update语句后再查询结果就是300了
             * 3. 在读提交的事务隔离级别下, 跟上述结果一致
             * ?
             *
             *
             * 那也就是说在这里 只要使用的 innodb 存储引擎, 无论如何都不会有问题
             * 1. 进程A在更新的时候, 进程B会被卡住, 进程B既更新不了也查询不了
             * 2. 对于那些在进程A更新前提交的更新, 进程A在更新时会拿到最新的已提交的版本, 然后紧接着的查询操作也是当前读, 这个当前读的结果也是正确的
             *
             */
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                sqlSession.update("com.hundsun.demo.springboot.mapper.SequenceMapper.update", "t1");
                value = sqlSession.selectOne("com.hundsun.demo.springboot.mapper.SequenceMapper.get", "t1").toString();
                sqlSession.commit();
            }
        }
        log.info(value);
    }

    @Autowired
    ThreadPoolExecutor commonPool;

    @SneakyThrows
    @GetMapping("/testSql")
    public void testSql() {
        Thread.sleep(2000);
        for (int i = 0; i < 5000; i++) {
            commonPool.execute(this::seqSession);
        }
    }

    @Autowired
    SegmentIdGenerator segmentIdGenerator;

    @SneakyThrows
    @GetMapping("/testSql2")
    public void testSql2() {
        StopWatch sw = new StopWatch("1000个线程同时并发获取ID,并插入数据库耗时测试.");
        sw.start();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 3; i++) {
            commonPool.execute(() -> {
                for (int i1 = 0; i1 < 10000; i1++) {
                    GenResult t1 = segmentIdGenerator.getId("t1");
                    if (GenResultEnum.NOT_READY.getId() == t1.getId()) {
                        atomicInteger.incrementAndGet();
                    } else {
                        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                            sqlSession.insert("com.hundsun.demo.springboot.mapper.SequenceMapper.insert", t1.getId());
                            sqlSession.commit();
                        }
                    }
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        sw.stop();
        log.info("获取 ID 失败一共 {} 次", atomicInteger.get());
        log.info(prettyPrintBySecond(sw));
    }

    private String prettyPrintBySecond(StopWatch stopWatch) {
        StopWatch.TaskInfo[] taskInfos = stopWatch.getTaskInfo();
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append("StopWatch '").append(stopWatch.getId()).append("': running time = ").append(stopWatch.getTotalTimeSeconds()).append(" s");
        sb.append('\n');
        if (taskInfos.length < 1) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------\n");
            sb.append("s         %     Task name\n");
            sb.append("---------------------------------------------\n");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumFractionDigits(3);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(2);
            pf.setMinimumFractionDigits(2);
            pf.setGroupingUsed(false);
            for (StopWatch.TaskInfo task : taskInfos) {
                sb.append(nf.format(task.getTimeSeconds())).append("  ");
                sb.append(pf.format((double) task.getTimeNanos() / stopWatch.getTotalTimeNanos())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }

    @GetMapping("/threadPool")
    public void threadPool() {
        log.info("threadPool");
        /*
         * 阿里云强制不允许这种创建方式
         * 1. FixedThreadPool 和 SingleThreadPool: 允许的请求队列长度为 Integer.MAX_VALUE, 可能会堆积大量的请求, 从而导致 OOM。
         * 2. CachedThreadPool 和 ScheduledThreadPool: 允许的创建线程数量为Integer.MAX_VALUE, 可能会创建大量的线程, 从而导致OOM。
         */
        // ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("scheduled-thread");
            thread.setDaemon(true);
            return thread;
        });
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.info("定时任务执行.");
            throw new RuntimeException("error");
        }, 1L, 1L, TimeUnit.SECONDS);

        ExecutorService pool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2)) {

        };
    }
}

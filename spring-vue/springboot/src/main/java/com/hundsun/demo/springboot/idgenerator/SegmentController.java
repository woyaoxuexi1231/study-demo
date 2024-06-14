package com.hundsun.demo.springboot.idgenerator;

import com.hundsun.demo.springboot.idgenerator.segmentid.SegmentIdGenerator;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: SegmentController
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 16:38
 */

@Slf4j
@RestController
@RequestMapping("/segment")
public class SegmentController implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    final Object object = new Object();

    SqlSessionFactory sqlSessionFactory;

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
                sqlSession.update("com.hundsun.demo.springboot.idgenerator.SequenceMapper.update", "t1");
                value = sqlSession.selectOne("com.hundsun.demo.springboot.idgenerator.SequenceMapper.get", "t1").toString();
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
        for (int i = 0; i < 300; i++) {
            commonPool.execute(() -> {
                segmentIdGenerator.getId("test");
            });
        }
    }
}

package com.hundsun.demo.springboot.spring.transactional;

import cn.hutool.core.collection.CollectionUtil;
import com.github.jsonzou.jmockdata.JMockData;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.ProductInfoDO;
import com.hundsun.demo.commom.core.model.User;
import com.hundsun.demo.springboot.common.mapper.ProductInfoMapper;
import com.hundsun.demo.springboot.config.ThreadPoolBeanConfig;
import com.hundsun.demo.springboot.mybatisplus.BatchCommitTest;
import com.hundsun.demo.springboot.mybatisplus.TestServiceImpl;
import com.hundsun.demo.springboot.mybatisplus.mapper.EmployeeMapperPlus;
import com.hundsun.demo.springboot.mybatisplus.mapper.UserMapper;
import com.hundsun.demo.springboot.tkmybatis.mapper.TkUserMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author woaixuexi
 * @since 2024/4/2 20:55
 */

@Slf4j
@Component
public class TransactionTest {

    /**
     * author: hulei42031
     * date: 2023-12-11 19:43
     */
    @Autowired
    ThreadPoolBeanConfig config;
    /**
     * author: hulei42031
     * date: 2023-12-11 19:43
     */
    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    @Autowired
    TransactionTest transactionTest;

    @Autowired
    TestServiceImpl testService;

    /**
     * 测试注解失效的场景
     * <p>
     * `@Transactional` 注解可能会失效的一些场景包括：<p>
     * 1. [不常见]注解被忽略：如果未正确配置 Spring 容器或未启用事务管理，那么 `@Transactional` 注解将会被忽略，事务不会生效。请确保已正确配置 Spring 上下文，并在配置文件中开启事务支持。<p>
     * 2. [常见错误]类不是由 Spring 容器管理：如果使用 `@Transactional` 注解的类没有被 Spring 容器管理，或类的实例是通过 `new` 操作符手动创建的，那么 `@Transactional` 注解也会失效。在使用 `@Transactional` 注解时，确保类被 Spring 容器扫描和实例化，或显式地将类定义为 Spring Bean。<p>
     * 3. 方法不是公共方法：`@Transactional` 注解默认只应用于公共方法。如果在一个非公共（如私有、受保护或默认）访问级别的方法上使用 `@Transactional` 注解，事务也会失效。<p>
     * 4. [常见错误]异常未被正确捕获：默认情况下，`@Transactional` 注解只会回滚受检查异常（checked exception），对于未受检异常（unchecked exception）默认是不会回滚的。如果在事务中抛出了未受检异常（如 `RuntimeException`），但没有被正确捕获，那么事务将会失效。可以通过在 `@Transactional` 注解中使用 `rollbackFor` 或 `noRollbackFor` 属性来指定回滚的异常类型。<p>
     * 5. 注解作用于静态方法：`@Transactional` 注解通常应用于实例方法上，用于作用在实例级别的事务中。如果将 `@Transactional` 注解应用于静态方法上，并尝试在静态上下文中处理事务，那么事务将会失效。<p>
     * 6. 注解作用于非 final 类的 final 方法：`@Transactional` 注解不会应用于被 `final` 修饰的类的非 final 方法上。这是由于 Spring 在运行时使用代理类来实现事务管理，无法覆盖 `final` 方法。<p>
     * 请确保在正确的场景下使用 `@Transactional` 注解，并合理配置事务的传播行为、隔离级别和异常处理，以确保事务生效。
     *
     * @param name name
     */
    @SneakyThrows
    public void transactionalLost(String name) {

        // 事务会失效,这里的仅仅只是另起了一个线程然后调用了run方法,并没有生成可以进行事务管理的代理对象和逻辑
        CountDownLatch countDownLatch = new CountDownLatch(1);
        config.commonPool().execute(new Runnable() {
            @Transactional
            @Override
            public void run() {
                // List<EmployeeDO> employeeDOS = employeeMapperPlus.selectList(new QueryWrapper<>());
                // employeeDOS.forEach(i -> log.info(String.valueOf(i)));
                EmployeeDO employeeDO = new EmployeeDO();
                employeeDO.setEmployeeNumber(1002L);
                employeeDO.setFirstName(name);
                employeeMapperPlus.updateById(employeeDO);
                countDownLatch.countDown();
                throw new RuntimeException("阻止提交");
            }
        });
        countDownLatch.await();


        // 事务生效
        try {
            transactionTest.run(name);
        } catch (Exception ignored) {
        }

        // 事务生效
        try {
            testService.run(name);
        } catch (Exception ignored) {
        }
    }

    @Transactional
    public void run(String name) {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(1002L);
        employeeDO.setFirstName(name + "run");
        employeeMapperPlus.updateById(employeeDO);
        throw new RuntimeException("error");
    }

    @Autowired
    BatchCommitTest batchCommitTest;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TkUserMapper tkUserMapper;


    @Transactional
    public void sqlSessionTransaction() {

        /*
        背景:
        在工作中实际遇到一次事务失效的问题,使用mybatis的SqlSession来进行批量提交,导致了后续的读操作读取不到提交的修改
        原因:
        使用了两个SqlSession,读数据使用了一个SqlSession,写数据使用了一个SqlSession,并且开启了mybatis的一级缓存,导致用于读取数据SqlSession感知不到另一个SqlSession的写操作而导致的事务失效
         */

        /*
        排查整体的过程如下:

        起初我认为是开启了两个事务,事务A提交的数据事务B不可见了.然后我大概看了一下整体的执行流程的源码:
            执行 mapper 代理对象的某个方法时:
            1. org.mybatis.spring.SqlSessionTemplate.SqlSessionInterceptor#invoke->org.mybatis.spring.SqlSessionUtils#getSqlSession
                - 调用org.springframework.transaction.support.TransactionSynchronizationManager#getResource这个方法尝试去获取目前是否已经创建过sqlsession了
                - 如果没有org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSession会尝试调用这个方法去组装好一个SqlSession（连接尚未建立）
                - 创建好之后通过org.mybatis.spring.SqlSessionUtils#registerSessionHolder这个方法把sqlsession放入TransactionSynchronizationManager里面，设置synchronizedWithTransaction置为true
                - 执行方法->结束后会执行commit方法,这个方法在检测到没有更新操作是不会真正执行提交的
            2. 这里如果中途使用SqlSessionFactory手动openSession的话：
                - 创建的SqlSession确实是全新的，SqlSession内绑定的Transaction也是全新的
                - 但是连接并不是新的，获取连接时会根据DataSource实例在TransactionSynchronizationManager内获取，内部为一个ThreadLocal
        我发现一个问题是这里开启了两个sqlSession,我大概猜到可能是因为sqlSession的问题导致更新操作不可见

        我又接着去看了一下spring事务相关的源码以及debug了一下jdbc提交修改的整体流程
        这里已经差不多得出结论了
        1. spring的事务是绑定在线程上的,即如果没有新开启线程,那么事务管理将被这个线程统一起来,这也就是为什么负责写操作的SqlSession的事务没有被提交的原因(mybatis-spring最终提交会结合spring事务)
        2. 对于同一个连接,前一个时刻提交的事务,在后一个时刻去获取一定能够获取到,这里没有获取到一定是mybatis本身的问题,我差不多已经知道是mybatis的一级缓存导致的了

        随后我又接着去看了一下mybatis查询部分的源码,mybatis的一级缓存在为感知到修改的时候,不会再查询数据库了,所以问题的最终原因就是: 多个SqlSession和mybatis的一级缓存导致事务失效
        这里还有一点就是mybatis-spring是mybatis和spring整合的框架,所以mybatis使用的连接也是用spring获取的,这一点很关键,如果myabatis不是从spring获取的,那么也不会有这个问题
         */

        // 下面是原始的问题代码

        // log.info("{}", userMapper.selectList(new QueryWrapper<>()).size());
        log.info("当前数据总量为: {}", tkUserMapper.selectAll().size());

        batchCommitTest.exeBatch(CollectionUtil.newArrayList(new User("hulei")), (sqlSession, user) -> {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.insert(user);
        });

        log.info("修改数据后的数据总量为: {}", tkUserMapper.selectAll().size());

        /*
        针对这个问题,可以进行如下修改:
        1. 批量提交完全使用一个新的线程,这样可以解决这个问题,但是随之而来的问题是,如果我后续操作失败了,我想要回滚数据就做不到了,所以这不是一个良好的解决方案
        new Thread(() -> {
            try {
                batchCommitTest.exeBatch(CollectionUtil.newArrayList(new User("hulei")), (sqlSession, user) -> {
                    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                    mapper.insert(user);
                });
            } finally {
                latch.countDown();
            }
        }).start();

        2. 关闭mybatis的一级缓存功能
        mybatis.configuration.local-cache-scope=statement
        mybatis-plus.configuration.local-cache-scope=statement

        这里引入了一个新的问题 引入mybatisplus之后,mybatisplus的配置会优于mybatis TODO 待研究

        3. 使用同一个SqlSession,通过SqlSessionUtils来获取与上下文相同的SqlSession来让mybatis感知到修改,从而获取最新的数据
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
                sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.insert(new User("hulei"));
        sqlSession.commit();
         */
    }

    @Autowired
    SubUserService subUserService;

    /**
     * 测试 @Transactional 注解的继承问题
     */
    public void transactionalInherited() {
        subUserService.save(new User("hulei"));
    }

    @Autowired
    ProductInfoMapper productInfoMapper;

    @Autowired
    ThreadPoolExecutor commonPool;

    public void deadLock() {
        /*
        一张表: id为主键(自增) 还有一个唯一索引(由两个字符类型的字段组成)
        并发操作: 可能存在一个在删除的线程, 两个同时在插入的线程
        错误: 数据库发生死锁,死锁发生在某一个插入的线程

        我这里复现不出来
         */
        for (int i = 0; i < 10; i++) {
            commonPool.execute(() -> {
                List<ProductInfoDO> list = new ArrayList<>();
                for (int j = 0; j < 1000; j++) {
                    list.add(ProductInfoDO.builder()
                            .productName(JMockData.mock(String.class) + JMockData.mock(String.class))
                            .category(JMockData.mock(String.class) + JMockData.mock(String.class))
                            .price(BigDecimal.valueOf(JMockData.mock(Double.class)))
                            .description(JMockData.mock(String.class) + JMockData.mock(String.class))
                            .build());
                }
                productInfoMapper.batchInsert(list);
            });
        }
    }
}

abstract class ParentUserService {

    @Autowired
    TkUserMapper tkUserMapper;

    @Transactional
    public void save(User user) {
        tkUserMapper.insertSelective(user);
    }
}

@Component
class SubUserService extends ParentUserService {

    /**
     * Transactional注解是可以继承的,所以子类即使在重写的方法上不加这个注解,也会默认使用父类的注解
     * 要打破继承关系,那么只能覆盖父类的配置
     *
     * @param user 新增的用户信息
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void save(User user) {
        tkUserMapper.insertSelective(user);
        throw new RuntimeException("阻止落库");
    }
}

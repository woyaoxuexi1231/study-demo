package com.hundsun.demo.springboot.mybatisplus;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.User;
import com.hundsun.demo.springboot.config.ThreadPoolBeanConfig;
import com.hundsun.demo.springboot.mybatisplus.mapper.EmployeeMapperPlus;
import com.hundsun.demo.springboot.mybatisplus.mapper.UserMapper;
import com.hundsun.demo.springboot.tkmybatis.mapper.TkUserMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;

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

    @SneakyThrows
    public void testTransaction(String name) {

        /*
        `@Transactional` 注解可能会失效的一些场景包括：
        1. 注解被忽略：如果未正确配置 Spring 容器或未启用事务管理，那么 `@Transactional` 注解将会被忽略，事务不会生效。请确保已正确配置 Spring 上下文，并在配置文件中开启事务支持。

        2. 类不是由 Spring 容器管理：如果使用 `@Transactional` 注解的类没有被 Spring 容器管理，或类的实例是通过 `new` 操作符手动创建的，那么 `@Transactional` 注解也会失效。在使用 `@Transactional` 注解时，确保类被 Spring 容器扫描和实例化，或显式地将类定义为 Spring Bean。

        3. 方法不是公共方法：`@Transactional` 注解默认只应用于公共方法。如果在一个非公共（如私有、受保护或默认）访问级别的方法上使用 `@Transactional` 注解，事务也会失效。

        4. 异常未被正确捕获：默认情况下，`@Transactional` 注解只会回滚受检查异常（checked exception），对于未受检异常（unchecked exception）默认是不会回滚的。如果在事务中抛出了未受检异常（如 `RuntimeException`），但没有被正确捕获，那么事务将会失效。可以通过在 `@Transactional` 注解中使用 `rollbackFor` 或 `noRollbackFor` 属性来指定回滚的异常类型。

        5. 注解作用于静态方法：`@Transactional` 注解通常应用于实例方法上，用于作用在实例级别的事务中。如果将 `@Transactional` 注解应用于静态方法上，并尝试在静态上下文中处理事务，那么事务将会失效。

        6. 注解作用于非 final 类的 final 方法：`@Transactional` 注解不会应用于被 `final` 修饰的类的非 final 方法上。这是由于 Spring 在运行时使用代理类来实现事务管理，无法覆盖 `final` 方法。

        请确保在正确的场景下使用 `@Transactional` 注解，并合理配置事务的传播行为、隔离级别和异常处理，以确保事务生效。
         */

        CountDownLatch countDownLatch = new CountDownLatch(1);
        /**
         * 事务不会生效, 没有触发点
         */
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
                throw new RuntimeException("error");
            }
        });

        countDownLatch.await();
        // 可以触发
        try {
            transactionTest.run(name);
        } catch (Exception e) {
        }

        // 可以触发
        try {
            testService.run(name);
        } catch (Exception e) {
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

        //
        /*
        执行usermapper这个代理对象的这个方法:
        1. org.mybatis.spring.SqlSessionTemplate.SqlSessionInterceptor#invoke->org.mybatis.spring.SqlSessionUtils#getSqlSession
            - 调用org.springframework.transaction.support.TransactionSynchronizationManager#getResource这个方法尝试去获取目前是否已经创建过sqlsession了
            - 如果没有org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSession会尝试调用这个方法去组装好一个SqlSession（连接尚未建立）
            - 创建好之后通过org.mybatis.spring.SqlSessionUtils#registerSessionHolder这个方法把sqlsession放入TransactionSynchronizationManager里面，设置synchronizedWithTransaction置为true
            - 执行方法->结束后会执行commit方法,这个方法在检测到没有更新操作是不会真正执行提交的
        2. 这里如果中途使用SqlSessionFactory手动openSession的话：
            - 创建的SqlSession确实是全新的，SqlSession内绑定的Transaction也是全新的
            - 但是连接并不是新的，获取连接时会根据DataSource实例在TransactionSynchronizationManager内获取，内部为一个ThreadLocal
         */

        // log.info("{}", userMapper.selectList(new QueryWrapper<>()).size());
        log.info("{}", tkUserMapper.selectAll().size());

        // 这里两种方式使用
        // 1.重新开启一个线程,由于重新开启了一个线程,连接和事务完全是独立的,当线程结束,这个新开的线程的事务会提交
        // CountDownLatch latch = new CountDownLatch(1);
        // new Thread(() -> {
        //     try {
        //         batchCommitTest.exeBatch(CollectionUtil.newArrayList(new User("hulei")), (sqlSession, user) -> {
        //             UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //             mapper.insert(user);
        //         });
        //     } finally {
        //         latch.countDown();
        //     }
        // }).start();
        // latch.countDown();

        // 2.不重新打开线程直接使用 batchCommitTest.exeBatch 来执行sql, 会导致使用了两个独立的 sqlSession, 但是连接是同一个, 最中事务不会提交
        // 但是如果mybatis的一级缓存,由于sqlSession独立,那么会因为一级缓存的问题导致我们提交的内容不可见
        batchCommitTest.exeBatch(CollectionUtil.newArrayList(new User("hulei")), (sqlSession, user) -> {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.insert(user);
        });


        // 解决上述问题的两种方案 1.关闭一级缓存 2.使整个过程的SqlSession保持统一即可
        // SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
        //         sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
        // UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        // mapper.insert(new User("hulei"));
        // sqlSession.commit();

        // userMapper.insert(new User("hulei"));
        // log.info("{}", userMapper.selectList(new QueryWrapper<>()).size());
        log.info("{}", tkUserMapper.selectAll().size());
        // System.out.println(jdbcTemplate.query("select * from users", BeanPropertyRowMapper.newInstance(User.class)).size());
    }
}

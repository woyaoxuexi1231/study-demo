package com.hundsun.demo.springboot.utils.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundsun.demo.springboot.config.ThreadPoolBeanConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mybatisplus
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-08-17 10:19
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@RestController(value = "mybatisplus")
public class UserTest extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper userMapper;

    private static Long id = 1l;

    @GetMapping("/delete")
    public void delete() {
        this.userMapper.deleteById(id);
    }

    @GetMapping("/selectList")
    public void selectList() {
        IPage<User> pageFinder = new Page<>(1, 2);
        System.out.println(("----- selectAll method test ------"));
        userMapper.pageList(pageFinder);
        // userList.forEach(System.out::println);
    }

    @GetMapping("/selectWrapper")
    public void selectWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("userId", "userName").orderByAsc("userId");
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        users.forEach(System.out::println);
    }

    @GetMapping("/simpleQuery")
    public void simpleQuery() {
        userMapper.selectList(new QueryWrapper<>());
    }


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public <T extends List<R>, R> void exeBatch(T t, BiConsumer<SqlSession, R> consumer) {
        //新获取一个模式为 BATCH, 自动提交为false的session
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (R r : t) {
                //或者使用
                //sqlSession.insert("com.example.demo.db.dao.PersonModelMapper.insertSelective", new PersonModel());
                //主意这时候不能正确返回影响条数了
                consumer.accept(sqlSession, r);
            }
            // sqlSession.flushStatements();

            sqlSession.commit();
            //清理缓存，防止溢出
            //sqlSession.clearCache();
        } catch (Exception e) {
            //异常回滚
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 批量提交测试
     * author: hulei42031
     * date: 2023-11-22 15:09
     */
    @GetMapping("/batchCommit")
    @Transactional
    public void batchCommit() {
        StopWatch stopWatch = new StopWatch();
        List<User> objects = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            User build = User.builder().id((long) i).name(System.currentTimeMillis() + "").build();
            objects.add(build);
        }


        stopWatch.start("single");
        objects = objects.stream().peek(i -> i.setName("single")).collect(Collectors.toList());
        objects.forEach(i -> userMapper.updateById(i));
        stopWatch.stop();


        stopWatch.start("updateBatchById");
        objects = objects.stream().peek(i -> i.setName("updateBatchById")).collect(Collectors.toList());
        this.updateBatchById(objects);
        stopWatch.stop();


        stopWatch.start("exeBatch");
        objects = objects.stream().peek(i -> i.setName("exeBatch")).collect(Collectors.toList());
        this.exeBatch(objects, (sqlSession, user) -> {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.updateById(user);
        });
        stopWatch.stop();


        stopWatch.start("updateBatch");
        objects = objects.stream().peek(i -> i.setName("updateBatch")).collect(Collectors.toList());
        userMapper.updateBatch(objects);
        stopWatch.stop();

        log.info("耗时分析: {}", stopWatch.prettyPrint());

        // throw new RuntimeException("error");
        /*
        1. 关于性能:
            1. 不开启连接串的 rewriteBatchedStatements=true, 耗时如下:
            ---------------------------------------------
            ns         %     Task name
            ---------------------------------------------
            1422275700  053%  single
            587211400  022%  updateBatchById
            588512600  022%  exeBatch
            087182300  003%  updateBatch

            2. 开启连接串的 rewriteBatchedStatements=true, 耗时如下:
            ---------------------------------------------
            ns         %     Task name
            ---------------------------------------------
            1081526700  076%  single
            124277100  009%  updateBatchById
            118075900  008%  exeBatch
            099868500  007%  updateBatch
            从结果来看, rewriteBatchedStatements=true要比不开更快, 但肯定也比一条一条提交更快

            3.如果使用 mybatis 的 foreach 生成多个 update 语句同时提交, 需要在连接串后添加 allowMultiQueries=true

            rewriteBatchedStatements:
                "rewriteBatchedStatements" 是一个 MySQL 驱动程序（如 JDBC）的连接属性，用于改善批量插入或更新的性能。
                当启用 "rewriteBatchedStatements" 属性后，MySQL 驱动程序会尝试将批量操作（例如 INSERT、UPDATE）合并成单个 SQL 语句，从而减少与数据库服务器之间的通信次数，提高性能。
                默认情况下，MySQL 驱动程序会将批量操作拆分为单独的 SQL 语句执行，这意味着每个插入或更新操作都需要与数据库服务器进行通信。
                启用 "rewriteBatchedStatements" 属性后，驱动程序会将批量操作重写成一条包含多个值的 SQL 语句，减少了通信次数，从而提高了性能。
                但需要注意的是，启用 "rewriteBatchedStatements" 属性可能会增加驱动程序的内存使用量，因为它需要在内存中构建较大的 SQL 语句。此外，数据库服务器也可能受到最大包大小限制，因此在处理大批量操作时，可能需要适当调整相关参数。
                总的来说，启用 "rewriteBatchedStatements" 属性可以在适当的情况下显著提高批量插入或更新操作的性能，但在使用时需要注意相关的内存和配置限制。

            *allowMultiQueries:
                "allowMultiQueries" 是一个 MySQL 驱动程序（如 JDBC）的连接属性，用于允许一次执行多个 SQL 查询。默认情况下，MySQL 驱动程序不允许执行多个查询，而是要求每个查询都作为单独的 SQL 语句执行。
                启用 "allowMultiQueries" 属性后，MySQL 驱动程序将允许将多个查询组合成一个字符串，并将其作为单个查询发送给数据库服务器。
                这可以减少与数据库服务器之间的通信次数，从而在某些情况下提高性能。使用多查询可以在一次交互中执行多个相关的操作，而不需要多次数据库通信。
                然而，需要注意的是，启用 "allowMultiQueries" 属性可能会增加潜在的安全风险，因为这样做可能导致 SQL 注入攻击。如果允许用户输入的数据直接拼接到查询字符串中，攻击者可能会利用这一特性执行恶意的操作。
                因此，应该谨慎使用 "allowMultiQueries" 属性，并确保对用户输入的数据进行适当的过滤和转义，以防止潜在的安全漏洞。只有在确保查询安全性的前提下，并且经过充分的测试和评估后，才应该启用这个属性。

        2. 关于事务问题,
            mybatisplus 的 updateBatchById 报错会回滚
            但是这里的 exeBatch 也会回滚, 我不知道是被spring回滚了, 还是他自己回滚的, 这个待研究 todo

         */
    }

    @PostMapping("/queryWithDate")
    @Transactional
    public void queryWithDate() {

        // List<User> objects = new ArrayList<>();
        // for (int i = 1; i <= 1000; i++) {
        //     objects.add(User.builder().id((long) i).name(System.currentTimeMillis() + "").build());
        // }
        // // this.updateBatchById(objects);
        // // objects.forEach(i -> i.setName(i.getName() + "a"));
        // userMapper.updateBatch(objects);
        Date date = new Date();
        /*
         * CST 北京时间, China Standard Time,又名中国标准时间 Thu Sep 28 00:53:04 CST 2023
         * GMT 格林尼治标准时间, Greenwich Mean Time Sun, 30 Aug 2020 15:09:23 GMT
         * UTC 国际协调时间, Coordinated Universal Time
         * ISO 标准时间
         *
         * 连接数据库的连接串不指定任何时区, 查出来的时间, 在 java 内存中与 mysql 实际的时间差了 13个小时
         * 但是我发现中部夏令时间(CDT)与北京时间差了正好 13个小时. CDT之前也叫 CST
         *
         * 如果不指定时区, 在查询的时候, 以时间为条件得到的结果可能不正确, 所以有以时间为条件的查询sql, 连接串最好带一个时区
         */
    }

    @GetMapping("/testNoSqlXml")
    public void testNoSqlXml() {
        // 这是一个没有 sql 实现的空标签
        userMapper.selectAll();
        /*
        ### Error querying database.  Cause: java.sql.SQLException: SQL String cannot be empty
        ### The error may exist in file [D:\Project\github\study-demo\demo-spring\demo-springboot\target\classes\mapper\UserMapper.xml]
        ### The error may involve com.hundsun.demo.springboot.mybatisplus.UserMapper.selectAll
        ### The error occurred while executing a query
        ### SQL:
        ### Cause: java.sql.SQLException: SQL String cannot be empty

        也就是说:
        1. 没有 SQL 标签 -> Invalid bound statement (not found): com.hundsun.demo.springboot.mybatisplus.UserMapper.selectAll
        2. 有 SQL 标签, 但是没有实现 -> java.sql.SQLException: SQL String cannot be empty
        这两种情况都会报错
         */
    }

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
    UserTest userTest;

    @Autowired
    TestServiceImpl testService;

    @SneakyThrows
    @GetMapping("/testTransaction")
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
            userTest.run(name);
        } catch (Exception e) {
            ;
        }

        // 可以触发
        try {
            testService.run(name);
        } catch (Exception e) {
            ;
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
}

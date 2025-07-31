package org.hulei.mybatis.spring;

import cn.hutool.core.date.StopWatch;
import com.github.javafaker.Faker;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.common.autoconfigure.annotation.DoneTime;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.mybatis.mapper.ProductInfoMapper;
import org.hulei.mybatis.mapper.XmlTagMapper;
import org.hulei.mybatis.spring.typeadapter.LocalDateTimeTypeAdapter;
import org.hulei.util.utils.MyStopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hulei
 * @since 2024/9/26 19:49
 */

@Slf4j
@RequestMapping("/xml-tag")
@RestController
public class XmlTagController {

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    /**
     * Mybatis sqlSession
     */
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    ProductInfoMapper productInfoMapper;

    @Autowired
    XmlTagMapper xmlTagMapper;

    /**
     * 用于格式化输出json
     */
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    /**
     * mybatis的ResultMap的简单使用
     */
    @GetMapping("/select-list-with-result-map")
    public void selectListWithResultMap() {
        // employeeMapper.getEmployeeTree();
        // 创建一个Gson实例并启用漂亮打印
        gson.toJson(xmlTagMapper.getEmployeeWithResultMap());
    }

    /**
     * collection 标签的使用
     * MyBatis 的 collection 标签是 用于处理一对多（One-to-Many）关联映射 的核心组件
     * 其核心作用是将数据库查询结果中的多行关联数据映射到 Java 对象的集合属性中
     */
    @GetMapping("/select-list-with-collection")
    public void selectListWithCollection(Long employeeId) {

        // 由于 collection 使用的是笛卡尔积查询后的结果再来拼接，如果分页查询会导致结果不正确！！ 所以这种方式不能进行分页
        // PageHelper.startPage(1, 50);
        // 通过Collection 来通过 Mybatis 构建列表对象
        xmlTagMapper.getDataFromResultMapWithCollection(employeeId);

        // 通过Collection来快速构建树状结果
        xmlTagMapper.getDataTree(1002L);

        /*
        树形结构的查询，mysql其实本身提供了一种巧妙的方式来实现
        mysql 可以通过使用 with recursive ... as 来实现树形结构
        这是 mysql 提供的递归查询
         */
    }

    /**
     * association标签的使用
     * 处理 一对一（One-to-One） 或 多对一（Many-to-One） 的关联关系。
     * 将单条关联数据映射到目标对象的单个属性中。
     */
    @GetMapping("/select-list-with-association")
    public void selectListWithAssociation() {
        xmlTagMapper.getDataFromResultMapWithAssociation();
    }

    /**
     * mybatis 的占位符
     */
    @GetMapping("/placeholder")
    public void placeholder() {
        /*
        ${} 会将参数值直接拼接到 SQL 语句中（类似字符串替换），最终生成的 SQL 是完整的文本。

        #{} 作为占位符, mybatis 会创建一个预编译的SQL语句
        把我们实际的参数解析为一个普通的字符串,即使是遇到一些特殊的字符也会被转义, 这有效防止了 SQL 的注入

        预编译的 SQL 语句（Prepared Statement）是一种数据库优化技术。
        其核心思想是：
         - 在执行 SQL 前，先将 SQL 的“结构”（模板）发送到数据库进行编译和优化，生成可复用的执行计划；
         - 后续执行时仅需传递参数值，无需重复编译。
         - 即使参数包含恶意字符（如 ' OR '1'='1），也会被当作普通字符串处理，无法篡改 SQL 结构。
         */
        // 在开启多语句执行时（allowMultiQueries=true），这个 sql 将删除其他表的数据，甚至可以删除任何东西
        xmlTagMapper.dollarSign("1; delete from big_data_products where id = 2;");
        // 获取所有用户(永真条件)，同样造成风险，对数据库造成巨大压力
        xmlTagMapper.dollarSign("1 OR '1'='1'");

        // 使用 #{} 这里就是安全的 sql，不管内容是什么，也仅仅当作普通字符串处理，即使这个字符串内部有任何特殊字符，也会经过转义
        xmlTagMapper.poundSign("1; delete from big_data_products where id = 3;");
    }

    @SneakyThrows
    @DoneTime
    @GetMapping("/fetch-size")
    public void fetchSize() {
        /*
        fetchSize 是 MyBatis 中用于控制数据库查询时每次从结果集中获取的记录数的配置参数。它可以显著影响大数据量查询的性能。


         */

        // 两个线程都去获取20万条数据,每次一千条,然后一个fetchSize是1,一个是100, 在开启游标查询的情况下,这个参数对于查询速度是有很大影响的
        threadPoolExecutor.execute(() -> {
            MyStopWatch stopWatch = new MyStopWatch();
            stopWatch.start("fetchSize1Tags");
            for (int i = 0; i < 1; i++) {
                PageHelper.startPage(i + 1, 1000, false);
                xmlTagMapper.fetchSize1();
            }
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        });

        threadPoolExecutor.execute(() -> {
            MyStopWatch stopWatch = new MyStopWatch();
            stopWatch.start("fetchSize100Tags");
            /*
            开启pageHelper之后,查询数据之前都会统计一次数量,避免空查询,
            select count(*),查看执行计划,这是一个使用主键的查询,但是大数据量的情况下依旧会花费大量的时间, 500万的数据查了58秒
            所以数据量较大时,关闭这个选项
             */
            for (int i = 0; i < 1; i++) {
                PageHelper.startPage(i + 1, 1000, false);
                xmlTagMapper.fetchSize100();
            }
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        });


        // mybatis适配流式查询
        threadPoolExecutor.execute(() -> {
            List<Employee> employeeDOS = xmlTagMapper.mybatisStreamQuery();
            employeeDOS.forEach((r) -> log.info("mybatisStreamQuery: employee: {}", r));
        });


        // mybatis使用ResultHandler适配流式查询
        threadPoolExecutor.execute(() -> xmlTagMapper.resultSetOpe(resultContext -> {
            Employee object = resultContext.getResultObject();
            log.info("resultSetOpe: employee: {}", object);
        }));

    }

    /**
     * 对于xml实现不完整的sql的报错问题
     * 1. 没有 SQL 标签 -> Invalid bound statement (not found): com.hundsun.demo.springboot.common.mapper.UserMapper.selectAll
     * 2. 有 SQL 标签, 但是没有实现 -> java.sql.SQLException: SQL String cannot be empty
     */
    @GetMapping("/test-no-sql-xml")
    public void testNoSqlXml() {
        // 这是一个没有 sql 实现的空标签
        xmlTagMapper.noXml();
        /*
        ### Error querying database.  Cause: java.sql.SQLException: SQL String cannot be empty
        ### The error may exist in file [D:\Project\github\study-demo\demo-spring\demo-springboot\target\classes\mapper\UserMapper.xml]
        ### The error may involve com.hundsun.demo.springboot.common.mapper.UserMapper.selectAll
        ### The error occurred while executing a query
        ### SQL:
        ### Cause: java.sql.SQLException: SQL String cannot be empty
         */
    }

    private static final Faker faker = new Faker();

    /**
     * 获取插入数据的主键
     */
    @RequestMapping(value = "insertAndGetId")
    public void insertAndGetId() {
        BigDataUser bigDataUser = new BigDataUser();
        bigDataUser.setName(faker.name().fullName());
        bigDataUser.setEmail(faker.internet().emailAddress());
        xmlTagMapper.insertGenerateKey(bigDataUser);
        log.info("数据插入成功, 返回的id为: {}", bigDataUser.getId());
    }

}

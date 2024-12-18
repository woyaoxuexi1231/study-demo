package org.hulei.springboot.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigUser;
import org.hulei.entity.mybatisplus.domain.Employees;
import org.hulei.entity.mybatisplus.domain.User;
import org.hulei.springboot.mybatisplus.mapper.BiguserMapper;
import org.hulei.springboot.mybatisplus.mapper.EmployeesMapper;
import org.hulei.springboot.mybatisplus.mapper.UserMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

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
@RestController
@RequestMapping("/mybatisPlus")
public class MybatisPlusController extends ServiceImpl<UserMapperPlus, User> {

    @Resource
    EmployeesMapper employeeMapper;

    @Resource
    private UserMapperPlus userMapperPlus;

    @Autowired
    BiguserMapper biguserMapper;

    private static final Long id = 1L;

    @GetMapping("/delete")
    public void delete() {
        this.userMapperPlus.deleteById(id);
    }

    /**
     * mybatisPlus插件提供的分页工具 IPage
     */
    @GetMapping("/mybatisPlusIPage")
    public void mybatisPlusIPage() {
        IPage<User> pageFinder = new Page<>(1, 2);
        userMapperPlus.pageList(pageFinder);
    }

    /**
     * QueryWrapper的简单使用
     */
    @GetMapping("/selectWrapper")
    public void selectWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("userId", "userName").orderByAsc("userId");
        List<User> users = userMapperPlus.selectList(new QueryWrapper<>());
        users.forEach(System.out::println);
    }

    /**
     * 数据库时区连接串的一些小细节问题
     */
    @PostMapping("/queryWithDate")
    @Transactional
    public void queryWithDate() {
        /*
         * 连接数据库的连接串不指定任何时区, 查出来的时间, 在 java 内存中与 mysql 实际的时间差了 13个小时
         * 但是我发现中部夏令时间(CDT)与北京时间差了正好 13个小时. CDT之前也叫 CST
         *
         * CST 北京时间, China Standard Time,又名中国标准时间 Thu Sep 28 00:53:04 CST 2023
         * GMT 格林尼治标准时间, Greenwich Mean Time Sun, 30 Aug 2020 15:09:23 GMT
         * UTC 国际协调时间, Coordinated Universal Time
         * ISO 标准时间
         *
         * 如果不指定时区, 在查询的时候, 以时间为条件得到的结果可能不正确, 所以有以时间为条件的查询sql, 连接串最好带一个时区
         */
    }

    /**
     * 对于xml实现不完整的sql的报错问题
     * 1. 没有 SQL 标签 -> Invalid bound statement (not found): com.hundsun.demo.springboot.common.mapper.UserMapper.selectAll
     * 2. 有 SQL 标签, 但是没有实现 -> java.sql.SQLException: SQL String cannot be empty
     */
    @GetMapping("/testNoSqlXml")
    public void testNoSqlXml() {
        // 这是一个没有 sql 实现的空标签
        userMapperPlus.selectAll();
        /*
        ### Error querying database.  Cause: java.sql.SQLException: SQL String cannot be empty
        ### The error may exist in file [D:\Project\github\study-demo\demo-spring\demo-springboot\target\classes\mapper\UserMapper.xml]
        ### The error may involve com.hundsun.demo.springboot.common.mapper.UserMapper.selectAll
        ### The error occurred while executing a query
        ### SQL:
        ### Cause: java.sql.SQLException: SQL String cannot be empty
         */
    }


    /**
     * 分页参数 startPage 的两个参数测试
     */
    // @Transactional
    @GetMapping(value = "/pageHelper")
    public void pageHelper(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        /*
        pagehelper.auto-runtime-dialect=true 每次查询通过连接信息获取对应的数据源信息, 这个连接用完后关闭
        开启后, 每一次分页都会去获取连接, 根据这个连接的具体信息来开启不同的分页上下文
        PageAutoDialect.getDialect()

        pageSizeZero 参数 - pageSize 为 0 的时候会查出所有数据而不进行分页
            - 在稍低版本中 pageNum 为 0 不会影响这个参数的使用, 稍新版本中 pageNum 为 0 不会查数据(这里使用 5.2.0 版本)
            - pageNum=0 始终不会有数据,不管pagesize是多少
         */
        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        System.out.println(Arrays.toString(employeeMapper.selectList(new LambdaQueryWrapper<>()).toArray()));
        PageHelper.startPage(pageNum, pageSize);
        System.out.println(Arrays.toString(employeeMapper.selectList(new LambdaQueryWrapper<>()).toArray()));
    }

    /**
     * 获取插入数据的主键
     */
    @RequestMapping(value = "insertAndGetId")
    public void insertAndGetId() {
        User user = new User("hulei");
        userMapperPlus.insertOne(user);
        log.info("数据插入成功, 返回的id为: " + user.getId());
    }

    /**
     * 使用$和#作为mybatis占位符的区别
     */
    @GetMapping("/sqlInject")
    public void sqlInject() {
        Employees injectRsp = employeeMapper.getEmployeeByIdTestInject(1002L);
    }

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/insertMockData")
    public void insertMockData() {
        final Faker faker = new Faker(Locale.CHINA);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(() -> {
                // 一万条数据插入一次
                List<BigUser> bigusers = new ArrayList<>();
                for (int k = 0; k < 10; k++) {
                    for (int j = 0; j < 10000; j++) {
                        BigUser biguser = new BigUser();
                        biguser.setUserName(faker.name().fullName());
                        biguser.setSsn(UUID.randomUUID().toString());
                        biguser.setName(faker.name().fullName());
                        biguser.setPhoneNumber(faker.phoneNumber().cellPhone());
                        biguser.setPlate(JMockData.mock(String.class));
                        biguser.setAddress(faker.address().fullAddress());
                        biguser.setBuildingNumber(faker.address().fullAddress());
                        biguser.setCountry(faker.country().currency());
                        biguser.setBirth(faker.date().birthday().toString());
                        biguser.setCompany(faker.company().name());
                        biguser.setJob(faker.job().title());
                        biguser.setCardNumber(faker.number().digit());
                        biguser.setCity(faker.country().capital());
                        biguser.setWeek(faker.date().birthday().toString());
                        biguser.setEmail(faker.internet().emailAddress());
                        biguser.setTitle(faker.book().title());
                        biguser.setParagraphs(faker.commerce().productName());
                        bigusers.add(biguser);
                    }
                    biguserMapper.batchInsert(bigusers);
                    bigusers.clear();
                }
            });
        }
    }
}

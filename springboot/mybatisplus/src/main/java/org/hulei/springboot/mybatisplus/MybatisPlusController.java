package org.hulei.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.User;
import lombok.extern.slf4j.Slf4j;
import com.hundsun.demo.commom.core.mapper.EmployeeMapperPlus;
import com.hundsun.demo.commom.core.mapper.UserMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private UserMapperPlus userMapperPlus;

    private static final Long id = 1L;

    @GetMapping("/delete")
    public void delete() {
        this.userMapperPlus.deleteById(id);
    }

    @GetMapping("/selectList")
    public void selectList() {
        IPage<User> pageFinder = new Page<>(1, 2);
        System.out.println(("----- selectAll method test ------"));
        userMapperPlus.pageList(pageFinder);
        // userList.forEach(System.out::println);
    }

    @GetMapping("/selectWrapper")
    public void selectWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("userId", "userName").orderByAsc("userId");
        List<User> users = userMapperPlus.selectList(new QueryWrapper<>());
        users.forEach(System.out::println);
    }

    @GetMapping("/simpleQuery")
    public void simpleQuery() {
        userMapperPlus.selectList(new QueryWrapper<>());
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
        userMapperPlus.selectAll();
        /*
        ### Error querying database.  Cause: java.sql.SQLException: SQL String cannot be empty
        ### The error may exist in file [D:\Project\github\study-demo\demo-spring\demo-springboot\target\classes\mapper\UserMapper.xml]
        ### The error may involve com.hundsun.demo.springboot.common.mapper.UserMapper.selectAll
        ### The error occurred while executing a query
        ### SQL:
        ### Cause: java.sql.SQLException: SQL String cannot be empty

        也就是说:
        1. 没有 SQL 标签 -> Invalid bound statement (not found): com.hundsun.demo.springboot.common.mapper.UserMapper.selectAll
        2. 有 SQL 标签, 但是没有实现 -> java.sql.SQLException: SQL String cannot be empty
        这两种情况都会报错
         */
    }

    @Autowired
    BatchCommitTest batchCommitTest;

    /**
     * 批量提交测试
     * author: hulei42031
     * date: 2023-11-22 15:09
     */
    @GetMapping("/batchCommit")
    public void batchCommit() {
        batchCommitTest.batchCommit();
    }

    @GetMapping("/printSelectAll")
    public void printSelectAll() {
        employeeMapper.selectList(Wrappers.lambdaQuery(EmployeeDO.builder().build())).forEach(System.out::println);
    }

    @Resource
    EmployeeMapperPlus employeeMapper;

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
}

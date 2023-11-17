package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

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

    @GetMapping("mybatisplus")
    public void mybatisplus() {
        userMapper.selectList(new QueryWrapper<>());
    }


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public <T extends List<R>, R> void exeBatch(T t, Consumer<R> consumer) {
        //新获取一个模式为 BATCH, 自动提交为false的session
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < 1000; i++) {
                //或者使用
                //sqlSession.insert("com.example.demo.db.dao.PersonModelMapper.insertSelective", new PersonModel());
                //主意这时候不能正确返回影响条数了
                consumer.accept(t.get(i));
            }
            sqlSession.flushStatements();

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


    @GetMapping("/mybatisplustest")
    @Transactional
    public void mybatisplustest() {
        // User user = new User();
        // // insert 方法选择性插入, 没有的字段是不插入的, 但是如果一个字段都没有, 会报错
        // userMapper.insert(user);

        // User update = new User();
        // update.setId(1L);
        // update.setAge(2);
        // LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        // updateWrapper.eq(User::getId, update.getId());
        // // update 方法也是选择性更新
        // userMapper.update(update, updateWrapper);

        List<User> objects = new ArrayList<>();
        // for (int i = 0; i < 10; i++) {
        //     objects.add(new User());
        // }

        // this.saveBatch(objects);

        for (int i = 1; i <= 1000; i++) {
            User build = User.builder().id((long) i).name(System.currentTimeMillis() + "").build();
            // objects.add();
            userMapper.updateById(build);
        }
        this.updateBatchById(objects);
    }

    @PostMapping("/mybatisplustest2")
    @Transactional
    public void mybatisplustest2() {

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
}

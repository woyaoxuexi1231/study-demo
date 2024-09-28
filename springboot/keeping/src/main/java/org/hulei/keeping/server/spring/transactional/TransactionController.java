package org.hulei.keeping.server.spring.transactional;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.jsonzou.jmockdata.JMockData;
import org.hulei.commom.core.model.pojo.ProductInfoDO;
import org.hulei.commom.core.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hulei.keeping.server.common.mapper.ProductInfoMapper;
import org.hulei.springboot.mybatisplus.controller.BatchCommitController;
import org.hulei.commom.core.mapper.UserMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.spring.transactional
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-05-09 15:02
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Configuration
@Slf4j
@RestController
@RequestMapping("/transactional")
public class TransactionController {

    @Bean
    public BatchCommitController batchCommitTest(){
        return new BatchCommitController();
    }

    @Autowired
    BatchCommitController batchCommitController;

    @Autowired
    UserMapperPlus userMapperPlus;

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

        随后我又接着去看了一下mybatis查询部分的源码,mybatis的一级缓存在未感知到修改的时候,不会再查询数据库了,所以问题的最终原因就是: 多个SqlSession和mybatis的一级缓存导致事务失效
        这里还有一点就是mybatis-spring是mybatis和spring整合的框架,所以mybatis使用的连接也是用spring获取的,这一点很关键,如果myabatis不是从spring获取的,那么也不会有这个问题
         */

        // 下面是原始的问题代码

        // log.info("{}", userMapper.selectList(new QueryWrapper<>()).size());
        log.info("当前数据总量为: {}", userMapperPlus.selectList(new LambdaQueryWrapper<>()).size());

        batchCommitController.exeBatch(CollectionUtil.newArrayList(new User("hulei")), (sqlSession, user) -> {
            UserMapperPlus mapper = sqlSession.getMapper(UserMapperPlus.class);
            mapper.insert(user);
        });

        log.info("修改数据后的数据总量为: {}", userMapperPlus.selectList(new LambdaQueryWrapper<>()).size());

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

        这里引入了一个新的问题 引入mybatisplus之后,mybatisplus的配置会优于mybatis

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
    @GetMapping("/transactionalInherited")
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



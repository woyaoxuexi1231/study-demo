package org.hulei.mybatis.spring.cache;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.jpa.pojo.Employee;
import org.hulei.mybatis.mapper.BigDataUserMapper;
import org.hulei.mybatis.mapper.CustomerMapper;
import org.hulei.util.dto.PageQryReqDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author hulei
 * @since 2024/11/17 17:39
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cache")
@RestController
public class MybatisCacheController {

    private final CustomerMapper customerMapper;

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * mybatis 一级缓存
     *
     * @param req      分页信息
     * @param request  httpReq
     * @param response httpRsp
     */
    @PostMapping(value = "/localCache")
    public void localCache(@Valid @RequestBody PageQryReqDTO req, HttpServletRequest request, HttpServletResponse response) {

        try (SqlSession session = sqlSessionFactory.openSession()) {

            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            PageHelper.startPage(0,1);
            log.info("第一次查询结果: {}", mapper.selectById(103));
            // log.info("第一次查询结果: {}", mapper.selectById(103));
            session.commit();

            /*
            一级缓存 mybatis.configuration.local-cache-scope
            1. statement, 缓存仅限在单个查询语句中，其实也就是没有缓存，每次查询都是当前读，会去数据库拿最新的数据
            2. session，缓存在单个会话内，这里为了实现这种场景，专门单开了一个 session
               如果只是单纯使用注入进来的 mapper 对象且不开启事务的情况下，单次查询之后session就完了，试验不了
               如果非要使用注入进来的 mapper 来实验也可以，方法开启事务即可，这样 session 在一个事务内就不会直接关闭，还会接着用
               这里如果使用 session 级别，第二次查询是不会去查询数据的
             */
        }

    }

    /**
     * mybatis 二级缓存
     */
    @GetMapping("/cache")
    public void cache() {

        /*
        二级缓存
        要开启二级缓存
            1. mybatis.configuration.cache-enabled=true
            2. 要么在 mapper 接口上使用 @CacheNamespace, 要么在 mapper.xml 文件中配置一个 <cache/>标签

        注意:
            1. @CacheNamespace 和  <cache/> 在使用时两者的适用范围不一样(通用mapper-spring-boot-starter 4.2.1)
                - @CacheNamespace 只适用通用 mapper 的直接使用的 api, 或者直接使用 @Select 类似这样的接口层的 sql
                - <cache/> 只适用于 xml 文件内的 sql
            2. 要使用二级缓存，缓存的对象必须要实现 Serializable 接口


        缓存命中率: Cache Hit Ratio
        - 开启二级缓存的话, 同一个session内好像并不增加缓存命中次数 Cache Hit Ratio [org.hulei.tkmybatis.mapper.EmployeeMapper]: 0.0 一直是 0
        - 缓存统计的也是针对整个 mapper 文件的命中率, 所有语句的命中率会统一统计
        - 二级缓存和一级缓存的优先级问题，在二级缓存没有生成的时候，第一次的 session 内一级缓存会根据自己的配置进行查询，后续二级缓存生成后，就会优先使用二级缓存
         */

        log.info("selectById: {}", customerMapper.selectById(103));

        // 同样的, 虽然这个不是通用 mapper 的 api, 但是他使用 @Select 查询数据, 也只有 @CacheNamespace 才会生效
        log.info("selectByIdWithAnnotation: {}", customerMapper.selectByIdWithAnnotation(103));
    }

    private final BigDataUserMapper bigDataUserMapper;

    @GetMapping("/trans-failed-cause-cache")
    @Transactional
    public void transFailedCauseCache() {
        /*
        背景:
        在工作中实际遇到一次事务失效的问题,使用 mybatis 的 SqlSession 来进行批量提交, 导致了后续的读操作读取不到提交的修改
        原因:
        使用了两个 SqlSession, 读数据使用了一个 SqlSession, 写数据使用了一个 SqlSession, 并且开启了 mybatis 的一级缓存, 导致用于读取数据 SqlSession 感知不到另一个 SqlSession 的写操作而导致的事务失效

        排查整体的过程如下:

        起初我认为是开启了两个事务, 事务A提交的数据事务B不可见了. 然后我大概看了一下整体的执行流程的源码:
            执行 mapper 代理对象的某个方法时:
            1. org.mybatis.spring.SqlSessionTemplate.SqlSessionInterceptor#invoke -> org.mybatis.spring.SqlSessionUtils#getSqlSession
                - 调用 org.springframework.transaction.support.TransactionSynchronizationManager#getResource 这个方法尝试去获取目前是否已经创建过 sqlsession 了
                - 如果没有 org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSession 会尝试调用这个方法去组装好一个 SqlSession（连接尚未建立）
                - 创建好之后通过 org.mybatis.spring.SqlSessionUtils#registerSessionHolder 这个方法把 sqlsession 放入 TransactionSynchronizationManager 里面，设置 synchronizedWithTransaction 置为 true
                - 执行方法 -> 结束后会执行 commit 方法, 这个方法在检测到没有更新操作是不会真正执行提交的
            2. 这里如果中途使用 SqlSessionFactory 手动 openSession 的话：
                - 创建的 SqlSession 确实是全新的，SqlSession 内绑定的 Transaction 也是全新的
                - 但是连接并不是新的，获取连接时会根据 DataSource 实例在 TransactionSynchronizationManager 内获取，内部为一个 ThreadLocal
        我发现一个问题是这里开启了两个 sqlSession, 我大概猜到可能是因为sqlSession的问题导致更新操作不可见

        我又接着去看了一下 spring 事务相关的源码以及 debug 了一下 jdbc 提交修改的整体流程
        这里已经差不多得出结论了
        1. spring 的事务是绑定在线程上的, 即如果没有新开启线程, 那么事务管理将被这个线程统一起来, 这也就是为什么负责写操作的 SqlSession 的事务没有被提交的原因(mybatis-spring 最终提交会结合 spring 事务)
        2. 对于同一个连接, 前一个时刻提交的事务, 在后一个时刻去获取一定能够获取到(当前读), 这里没有获取到一定是 mybatis 本身的问题, 我差不多已经知道是 mybatis 的一级缓存导致的了

        随后我又接着去看了一下 mybatis 查询部分的源码, mybatis 的一级缓存在未感知到修改的时候, 不会再查询数据库了, 所以问题的最终原因就是: 多个 SqlSession 和 mybatis 的一级缓存导致事务失效
        这里还有一点就是 mybatis-spring 是 mybatis 和 spring 整合的框架, 所以 mybatis 使用的连接也是用 spring 获取的, 这一点很关键, 如果 myabatis 不是从 spring 获取的, 那么也不会有这个问题
         */

        // 下面是原始的问题代码
        BigDataUser gen = BigDataUser.gen();

        SqlSession readSession = sqlSessionFactory.openSession();
        BigDataUserMapper readSessionMapper = readSession.getMapper(BigDataUserMapper.class);
        log.info("当前数据总量为: {}", readSessionMapper.getUserByName(gen.getName()).size());


        // 新获取一个模式为 BATCH, 自动提交为false的session
        SqlSession writeSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            BigDataUserMapper mapper = writeSession.getMapper(BigDataUserMapper.class);
            mapper.savaOne(gen);
            writeSession.commit();
        } catch (Exception e) {
            // 异常回滚
            log.error("exeBatch出现异常,", e);
            writeSession.rollback();
        } finally {
            writeSession.close();
        }

        log.info("当前数据总量为: {}", readSessionMapper.getUserByName(gen.getName()).size());

        /*
        针对这个问题,可以进行如下修改:
        1. 批量提交完全使用一个新的线程, 这样可以解决这个问题, 但是随之而来的问题是, 如果我后续操作失败了, 我想要回滚数据就做不到了, 所以这不是一个良好的解决方案
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

        2. 关闭 mybatis 的一级缓存功能
        mybatis.configuration.local-cache-scope=statement
        mybatis-plus.configuration.local-cache-scope=statement

        这里引入了一个新的问题 引入 mybatisplus 之后, mybatisplus 的配置会优于 mybatis

        3. 使用同一个SqlSession,通过SqlSessionUtils来获取与上下文相同的SqlSession来让mybatis感知到修改,从而获取最新的数据
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
                sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.insert(new User("hulei"));
        sqlSession.commit();
         */
    }
}

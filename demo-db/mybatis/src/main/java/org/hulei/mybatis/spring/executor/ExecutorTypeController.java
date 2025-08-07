package org.hulei.mybatis.spring.executor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.mybatis.mapper.BigDataUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hulei
 * @since 2025/6/26 16:19
 */

@Slf4j
@RestController
@RequestMapping(value = "/executor")
public class ExecutorTypeController {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    BigDataUserMapper bigDataUserMapper;

    @GetMapping("/executor-type-test")
    public void executorTypeTest() {
        int count = 10000;

        /*
        Mybatis 提供了三种不同的执行器类型
         - SIMPLE，默认执行器，每次执行都会创建一个新的预处理语句（PreparedStatement），不进行特殊处理。
         - REUSE，重用执行器，会重用预处理语句（PreparedStatement），减少重复编译 SQL 的开销。
         - BATCH，批处理执行器，支持批量操作，将多个 SQL 语句一起提交到数据库执行，提高批量插入、更新等操作的性能。
         */
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SIMPLE 执行器耗时");
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.SIMPLE)) {
            BigDataUserMapper mapper = session.getMapper(BigDataUserMapper.class);
            for (int i = 0; i < count; i++) {
                mapper.savaOne(BigDataUser.gen());
            }
        }
        stopWatch.stop();

        stopWatch.start("REUSE 执行器耗时");
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.REUSE)) {
            BigDataUserMapper mapper = session.getMapper(BigDataUserMapper.class);
            for (int i = 0; i < count; i++) {
                mapper.savaOne(BigDataUser.gen());
            }
        }
        stopWatch.stop();

        /*
        ExecutorType.BATCH 或者是 xml 手写 foreach 都可以达到批量插入的效果，区别：
         - ExecutorType.BATCH 使用 jdbc 批处理API（mysql连接串需要配置rewriteBatchedStatements=true）
         - foreach 进行的是 SQL 拼接
        数量特别巨大时还是推荐 Batch 提交， sql 拼接有超长风险
         */
        stopWatch.start("BATCH 执行器耗时");
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            BigDataUserMapper mapper = session.getMapper(BigDataUserMapper.class);
            for (int i = 0; i < count; i++) {
                mapper.savaOne(BigDataUser.gen());
            }
            session.commit();
        }
        stopWatch.stop();

        stopWatch.start("xml 拼接执行耗时");
        List<BigDataUser> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(BigDataUser.gen());
        }
        bigDataUserMapper.batchInsert(list);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}

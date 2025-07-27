package org.hulei.springboot.mybatisplus.controller;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataUsers;
import org.hulei.entity.mybatisplus.util.BatchExecutor;
import org.hulei.springboot.mybatisplus.mapper.BigDataUsersMapperPlus;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author woaixuexi
 * @since 2024/4/2 20:51
 */

@Slf4j
@Component
@RestController
@RequestMapping
public class BatchCommitController extends ServiceImpl<BigDataUsersMapperPlus, BigDataUsers> {

    @Resource
    private BigDataUsersMapperPlus bigDataUsersMapperPlus;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    private static final Faker faker = new Faker();

    /**
     * 批量提交测试
     * author: hulei42031
     * date: 2023-11-22 15:09
     */
    @GetMapping("/batchCommit")
    @Transactional
    public void batchCommit() {
        StopWatch stopWatch = new StopWatch();
        List<BigDataUsers> objects = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            BigDataUsers bigDataUser = new BigDataUsers();
            bigDataUser.setName(faker.name().fullName());
            bigDataUser.setEmail(faker.internet().emailAddress());
            objects.add(bigDataUser);
        }

        stopWatch.start("单条循环插入");
        objects.forEach(i -> bigDataUsersMapperPlus.insertOne(i));
        stopWatch.stop();


        /*
        saveBatch 和 exeBatch 这两种方式要开启 rewriteBatchedStatements=true 参数性能才会有显著提升
         */
        stopWatch.start("mybatisPlus提供的批量插入");
        this.saveBatch(objects);
        stopWatch.stop();

        stopWatch.start("手写的mybatis batch形式批量提交");
        BatchExecutor.exeBatch(
                sqlSessionTemplate,
                objects,
                (sqlSession, user) -> {
                    BigDataUsersMapperPlus mapper = sqlSession.getMapper(BigDataUsersMapperPlus.class);
                    mapper.insertOne(user);
                }
        );
        stopWatch.stop();


        /*
        这种方式必须要开启 allowMultiQueries=true 这个参数，否则是不能执行的
        而且这种方式要注意 sql 超长的问题，最好是分割执行
         */
        stopWatch.start("批量更新(需要打开多语句执行参数)");
        splitList(objects, 100).forEach((list) -> bigDataUsersMapperPlus.insertBatch(list));
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
            但是这里的 exeBatch 也会回滚, 我不知道是被spring回滚了, 还是他自己回滚的, 这个待研究
            2024年4月14日 视情况而定,看是否包含在spring事务内,如果包含在spring事务内,那么由spring决定回滚/提交
                        这里exeBatch虽然开启了新的SqlSession,但是由于在同一个线程内,连接是同一个,在提交时spring会校验是否回滚/提交
                        *注意 这种情况可能会因为mybatis的一级缓存导致某些情况下的数据问题,在com.hundsun.demo.springboot.mybatisplus.TransactionTest.sqlSessionTransaction方法有个小实验
         */
    }

    public static <T> List<List<T>> splitListStream(List<T> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(i * batchSize, Math.min((i + 1) * batchSize, list.size())))
                .collect(Collectors.toList());
    }

    public static <T> List<List<T>> splitList(List<T> list, int batchSize) {
        List<List<T>> result = new ArrayList<>();
        if (list == null || list.isEmpty() || batchSize <= 0) {
            return result;
        }
        int size = list.size();
        for (int i = 0; i < size; i += batchSize) {
            int end = Math.min(i + batchSize, size); // 避免越界
            List<T> sub = list.subList(i, end);
            result.add(new ArrayList<>(sub)); // 转换为独立列表（可选）
        }
        return result;
    }
}

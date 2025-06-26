package org.hulei.mybatis.spring.executor;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.ProductInfo;
import org.hulei.mybatis.mapper.ProductInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author hulei
 * @since 2025/6/26 16:19
 */

@Slf4j
@RestController
@RequestMapping(value = "/executor")
public class ExecutorController {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Faker faker = new Faker(Locale.CHINA);

    @GetMapping("/simple")
    public void simpleExecutor() {
        Random random = new Random();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.SIMPLE)) {
            ProductInfoMapper mapper = session.getMapper(ProductInfoMapper.class);
            for (int i = 0; i < 1; i++) {
                ProductInfo record = new ProductInfo();
                record.setProductName(faker.name().firstName());
                record.setCategory(faker.lorem().word());
                record.setPrice(BigDecimal.valueOf(random.nextDouble()));
                record.setDescription(faker.address().firstName());
                mapper.insert(record);
                log.info("数据生成的主键为：{}", record.getId());
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @GetMapping("/reuse")
    public void reuseExecutor() {
        Random random = new Random();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.REUSE)) {
            ProductInfoMapper mapper = session.getMapper(ProductInfoMapper.class);
            for (int i = 0; i < 1000; i++) {
                ProductInfo record = new ProductInfo();
                record.setProductName(faker.name().firstName());
                record.setCategory(faker.lorem().word());
                record.setPrice(BigDecimal.valueOf(random.nextDouble()));
                record.setDescription(faker.address().firstName());
                mapper.insert(record);
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @GetMapping("/batch")
    public void batchExecutor() {
        /*
        这里使用 ExecutorType.BATCH 或者是 xml 手写 foreach 都可以达到批量插入的效果，区别：
            ExecutorType.BATCH 使用 jdbc 批处理API（mysql连接串需要配置rewriteBatchedStatements=true）， foreach 进行的是 SQL 拼接
        数量特别巨大时还是推荐 Batch 提交， sql 拼接有超长风险
         */

        Random random = new Random();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProductInfoMapper mapper = session.getMapper(ProductInfoMapper.class);
            for (int i = 0; i < 1000; i++) {
                ProductInfo record = new ProductInfo();
                record.setProductName(faker.name().firstName());
                record.setCategory(faker.lorem().word());
                record.setPrice(BigDecimal.valueOf(random.nextDouble()));
                record.setDescription(faker.address().firstName());
                mapper.insert(record);
            }
            session.commit();
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @Autowired
    ProductInfoMapper productInfoMapper;

    @GetMapping("/xml-batch")
    public void xmlBatch() {
        Random random = new Random();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<ProductInfo> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ProductInfo record = new ProductInfo();
            record.setProductName(faker.name().firstName());
            record.setCategory(faker.lorem().word());
            record.setPrice(BigDecimal.valueOf(random.nextDouble()));
            record.setDescription(faker.address().firstName());
            list.add(record);
        }
        productInfoMapper.batchInsert(list);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}

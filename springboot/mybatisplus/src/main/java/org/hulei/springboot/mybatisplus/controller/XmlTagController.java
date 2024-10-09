package org.hulei.springboot.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.commom.core.annotation.DoneTime;
import org.hulei.commom.core.mapper.BiguserMapper;
import org.hulei.commom.core.mapper.CustomerMapper;
import org.hulei.commom.core.mapper.EmployeeMapperPlus;
import org.hulei.commom.core.mapper.OrderdetailsMapper;
import org.hulei.commom.core.mapper.OrdersMapper;
import org.hulei.commom.core.mapper.ProductlinesMapper;
import org.hulei.commom.core.mapper.ProductsMapper;
import org.hulei.commom.core.model.pojo.Biguser;
import org.hulei.commom.core.model.pojo.CustomerDO;
import org.hulei.commom.core.model.pojo.EmployeeDO;
import org.hulei.springboot.mybatisplus.LocalDateTimeTypeAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hulei
 * @since 2024/9/26 19:49
 */

@Slf4j
@RequestMapping("/xmlTag")
@RestController
public class XmlTagController {

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    /**
     * 一堆mapper配置
     */
    @Resource
    EmployeeMapperPlus employeeMapper;
    @Resource
    OrdersMapper ordersMapper;
    @Autowired
    private OrderdetailsMapper orderdetailsMapper;
    @Autowired
    ProductsMapper productsMapper;
    @Autowired
    ProductlinesMapper productlinesMapper;
    @Autowired
    BiguserMapper biguserMapper;
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * Mybatis sqlSession
     */
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    /**
     * 用于格式化输出json
     */
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    /**
     * mybatis的ResultMap的简单使用
     *
     * @return
     */
    @GetMapping("/selectListWithResultMap")
    public List<EmployeeDO> selectListWithResultMap() {
        // employeeMapper.getEmployeeTree();
        // 创建一个Gson实例并启用漂亮打印
        return employeeMapper.getEmployeeWithResultMap();
    }

    /**
     * collection标签的使用
     *
     * @return rsp
     */
    @GetMapping("/selectListWithCollection")
    public List<?> selectListWithCollection(Long employeeId) {

        // 通过Collection来通过Mybatis构建列表对象
        return employeeMapper.getEmployeeWithCustomers(employeeId);

        // return ordersMapper.selectOrderFullInfo();

        // 通过Collection来快速构建树状结果
        // return employeeMapper.getEmployeeTree(1002L);
    }

    /**
     * association标签的使用
     *
     * @return rsp
     */
    @GetMapping("/selectListWithAssociation")
    public List<?> selectListWithAssociation() {
        return productsMapper.selectProductFullInfo();
    }

    @SneakyThrows
    @DoneTime
    @GetMapping("/selectTagsTest")
    public void selectTagsTest() {

        // 两个线程都去获取20万条数据,每次一千条,然后一个fetchSize是1,一个是100, 在开启游标查询的情况下,这个参数对于查询速度是有很大影响的
        // threadPoolExecutor.execute(() -> {
        //     StopWatch stopWatch = new StopWatch();
        //     stopWatch.start("fetchSize1Tags");
        //     for (int i = 0; i < 20; i++) {
        //         PageHelper.startPage(i + 1, 10000, false);
        //         biguserMapper.fetchSize1Tags();
        //     }
        //     stopWatch.stop();
        //     System.out.println(stopWatch.prettyPrint());
        // });
        // threadPoolExecutor.execute(() -> {
        //     StopWatch stopWatch = new StopWatch();
        //     stopWatch.start("fetchSize100Tags");
        //     /*
        //     开启pageHelper之后,查询数据之前都会统计一次数量,避免空查询,
        //     select count(*),查看执行计划,这是一个使用主键的查询,但是大数据量的情况下依旧会花费大量的时间, 500万的数据查了58秒
        //     所以数据量较大时,关闭这个选项
        //      */
        //     for (int i = 0; i < 20; i++) {
        //         PageHelper.startPage(i + 1, 10000, false);
        //         biguserMapper.fetchSize100Tags();
        //     }
        //     stopWatch.stop();
        //     System.out.println(stopWatch.prettyPrint());
        // });


        // mybatis适配流式查询
        // threadPoolExecutor.execute(() -> {
        //     List<EmployeeDO> employeeDOS = employeeMapper.mybatisStreamQuery();
        //     employeeDOS.forEach((r) -> log.info("mybatisStreamQuery: employee: {}", r));
        // });


        // mybatis使用ResultHandler适配流式查询
        // threadPoolExecutor.execute(() -> {
        //     employeeMapper.resultSetOpe(new ResultHandler<EmployeeDO>() {
        //         @Override
        //         public void handleResult(ResultContext<? extends EmployeeDO> resultContext) {
        //             EmployeeDO object = resultContext.getResultObject();
        //             log.info("resultSetOpe: employee: {}", object);
        //         }
        //     });
        // });


        // mybatis select其他标签测试
        // threadPoolExecutor.execute(() -> {
        //     List<EmployeeDO> employeeDOS = employeeMapper.selectTagsTest();
        // });

    }

    @GetMapping("/insertTagsTest")
    public void insertTagsTest() {
        final Faker faker = new Faker(Locale.CHINA);
        Biguser biguser = new Biguser();
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

        // 这个insert返回自动生成的键的值, mysql好像并不支持返回多个自动生成的列的值
        biguserMapper.insertOne(biguser);

        log.info("自动生成的键: id={}, createTime={}, updateTime={} ", biguser.getId(), biguser.getCreateTime(), biguser.getUpdateTime());

        LambdaQueryWrapper<CustomerDO> wrapper = Wrappers.lambdaQuery(CustomerDO.class);
        CustomerDO customerDO = customerMapper.selectOne(wrapper.eq(CustomerDO::getCustomernumber, 103));

        // 这里数据库定义的是(10,2),更新时发现出现了精度丢失,但是却没有报错
        customerDO.setCreditlimit(BigDecimal.valueOf(10.324523423423));
        customerMapper.updateOne(customerDO);

        CustomerDO customerDO1 = new CustomerDO();
        customerDO1.setCustomername(faker.name().fullName());
        customerDO1.setContactlastname(faker.name().lastName());
        customerDO1.setContactfirstname(faker.name().firstName());
        customerDO1.setPhone(faker.phoneNumber().cellPhone());
        customerDO1.setAddressline1(faker.address().fullAddress());
        customerDO1.setAddressline2(faker.address().fullAddress());
        customerDO1.setCity(faker.country().capital());
        customerDO1.setState(faker.hobbit().location());
        customerDO1.setPostalcode(faker.hobbit().location());
        customerDO1.setCountry(faker.country().name());
        customerDO1.setSalesrepemployeenumber(1003);
        // 这里同样的,插入操作也没有报错,但是出现了精度丢失
        customerDO1.setCreditlimit(BigDecimal.valueOf(12352350.325498237));
        customerMapper.insert(customerDO1);
    }

}

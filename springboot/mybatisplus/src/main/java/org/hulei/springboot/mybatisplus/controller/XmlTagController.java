package org.hulei.springboot.mybatisplus.controller;

import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.commom.core.annotation.DoneTime;
import org.hulei.commom.core.mapper.BiguserMapper;
import org.hulei.commom.core.mapper.EmployeeMapperPlus;
import org.hulei.commom.core.mapper.OrderdetailsMapper;
import org.hulei.commom.core.mapper.OrdersMapper;
import org.hulei.commom.core.mapper.ProductlinesMapper;
import org.hulei.commom.core.mapper.ProductsMapper;
import org.hulei.commom.core.model.pojo.EmployeeDO;
import org.hulei.commom.core.utils.StopWatch;
import org.hulei.springboot.mybatisplus.LocalDateTimeTypeAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    public List<?> selectListWithCollection() {

        // 通过Collection来通过Mybatis构建列表对象
        return employeeMapper.getEmployeeWithCustomers();

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
        // 两个线程都去获取20万条数据,每次一千条,然后一个fetchSize是1,一个是100
        threadPoolExecutor.execute(() -> {
        });

        StopWatch stopWatch = new StopWatch();
        // stopWatch.start("fetchSize1Tags");
        // // for (int i = 0; i < 20; i++) {
        // //     PageHelper.startPage(i + 1, 10000);
        // //     biguserMapper.fetchSize1Tags();
        // // }
        // PageHelper.startPage(1,10000);
        // biguserMapper.fetchSize1Tags();
        // stopWatch.stop();
        stopWatch.start("fetchSize100Tags");
        PageHelper.startPage(1, 1000, false);
        log.info("一共查询了{}条数据", biguserMapper.fetchSize100Tags().size());
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

}

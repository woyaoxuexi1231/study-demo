package com.hundsun.demo.spring.service.impl;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.spring.jdbc.DynamicDataSourceType;
import com.hundsun.demo.spring.jdbc.DynamicDataSourceTypeManager;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import com.hundsun.demo.spring.model.pojo.ProductsDO;
import com.hundsun.demo.spring.mybatis.CustomerMapper;
import com.hundsun.demo.spring.mybatis.MyBatisOperationType;
import com.hundsun.demo.spring.service.YiibaidbService;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-13 16:01
 */

@Data
public class YiibaidbServiceImpl implements YiibaidbService, ApplicationContextAware {

    /**
     * applicationContext - 获取 JdbcTemplate bean
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public List<CustomerDO> jdbcTemplateQuery() {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
        return jdbcTemplate.query("select * from customers limit 0,10", new BeanPropertyRowMapper<>(CustomerDO.class));
    }

    @Override
    public void jdbcTemplateUpdate() {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
        jdbcTemplate.execute("update customers set phone = '40.32.2554' where customerNumber = '103'");
        throw new RuntimeException("更新失败, 准备回滚...");
    }

    @Override
    public void multipleDataSource(DynamicDataSourceType dynamicDataSourceType) {
        System.out.println();
        System.out.println("-------------------------------------- Spring多数据源 --------------------------------------");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("multipleDataSourceJdbcTemplate");
        // 使用指定数据源查询数据
        DynamicDataSourceTypeManager.set(dynamicDataSourceType);
        System.out.println("当前绑定的数据源为 " + dynamicDataSourceType);
        List<ProductsDO> productsDOS = jdbcTemplate.query("select * from products limit 0,10", new BeanPropertyRowMapper<>(ProductsDO.class));
        productsDOS.forEach(System.out::println);
    }

    @Override
    public void multipleDataSourceTransaction(DynamicDataSourceType dynamicDataSourceType) {
        System.out.println();
        System.out.println("-------------------------------------- Spring多数据源 + 事务 --------------------------------------");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("multipleDataSourceJdbcTemplate");
        System.out.println("当前绑定的数据源为 " + dynamicDataSourceType);
        if (dynamicDataSourceType.equals(DynamicDataSourceType.MASTER)) {
            jdbcTemplate.execute("update customers set phone = '40.32.2554' where customerNumber = '103'");
        }
        if (dynamicDataSourceType.equals(DynamicDataSourceType.SECOND)) {
            jdbcTemplate.execute("update customers set phone = '40.32.2552' where customerNumber = '103'");
            throw new RuntimeException("从SECOND数据源更新出现问题! 正准备回滚...");
        }

    }

    /**
     * 自动注入 Mybatis生成的 mapperBean
     */
    private CustomerMapper customerMapper;

    @Override
    public void mybatisSpringTransaction(MyBatisOperationType myBatisOperationType, DynamicDataSourceType dynamicDataSourceType) {

        System.out.println();
        System.out.println("-------------------------------------- Spring + Mybatis --------------------------------------");

        /*
        Q1: Mybatis 是如何去拿这个动态数据源的信息的?? - 2023/02/18
        A1: 不管啥框架, 只要在 Spring 里面, 最终都是通过 Spring 来获取 connect, 对于动态数据源来说, 既然我们自己定义了多数据源的获取方法, 那么 Spring 会通过我们定义的逻辑来获取对应的数据源 - 2023/02/26
         */
        System.out.println("当前绑定的数据源为 " + dynamicDataSourceType + " - 执行 " + myBatisOperationType + " 操作");
        // select
        if (myBatisOperationType.equals(MyBatisOperationType.SELECT)) {
                /*
                pageHelper
                pageSizeZero 参数 - pageSize 为 0 的时候会查出所有数据而不进行分页, 在稍低版本中 pageNum 为 0 不会影响这个参数的使用, 稍新版本中 pageNum 为 0 不会查数据(这里使用 5.2.0 版本)
                 */
            PageHelper.startPage(1, 10);
            // 通过 spring Bean 的方式使用 Mybatis
            List<CustomerDO> customerDOS = customerMapper.selectAll();
            // customerDOS.forEach(System.out::println);
        }

        // update
        if (myBatisOperationType.equals(MyBatisOperationType.UPDATE)) {
            CustomerDO customerDO = new CustomerDO();
            customerDO.setCustomernumber(103);
            customerDO.setPhone("40.32.100");
            customerMapper.updateOne(customerDO);
            // if (dynamicDataSourceType.equals(DynamicDataSourceType.SECOND)) {
            //     throw new RuntimeException("数据源 SECOND 更新出错!");
            // }
        }

    }

}

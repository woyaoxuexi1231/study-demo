package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.spring.jdbc.DataSourceType;
import com.hundsun.demo.spring.jdbc.DataSourceTypeManager;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import com.hundsun.demo.spring.model.pojo.ProductsDO;
import com.hundsun.demo.spring.service.YiibaidbService;
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
    public void multipleDataSource(DataSourceType dataSourceType) {
        System.out.println();
        System.out.println("-------------------------------------- Spring多数据源 --------------------------------------");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("multipleDataSourceJdbcTemplate");
        // 使用指定数据源查询数据
        DataSourceTypeManager.set(dataSourceType);
        System.out.println("当前绑定的数据源为 " + dataSourceType);
        List<ProductsDO> productsDOS = jdbcTemplate.query("select * from products limit 0,10", new BeanPropertyRowMapper<>(ProductsDO.class));
        productsDOS.forEach(System.out::println);
        System.out.println("-------------------------------------- Spring多数据源 --------------------------------------");
        System.out.println();
    }

    @Override
    public void multipleDataSourceTransaction(DataSourceType dataSourceType) {
        System.out.println();
        System.out.println("-------------------------------------- Spring多数据源 + 事务 --------------------------------------");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("multipleDataSourceJdbcTemplate");
        // 使用指定数据源更新数据
        DataSourceTypeManager.set(dataSourceType);
        System.out.println("当前绑定的数据源为 " + dataSourceType);
        try {
            if (dataSourceType.equals(DataSourceType.MASTER)) {
                jdbcTemplate.execute("update customers set phone = '40.32.2554' where customerNumber = '103'");
            }
            if (dataSourceType.equals(DataSourceType.SECOND)) {
                jdbcTemplate.execute("update customers set phone = '40.32.2552' where customerNumber = '103'");
                throw new RuntimeException("从SECOND数据源更新出现问题! 正准备回滚...");
            }
        } finally {
            System.out.println("-------------------------------------- Spring多数据源 + 事务 --------------------------------------");
            System.out.println();
        }
    }

}

package com.hundsun.demo.spring.db.dynamicdb;

import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.CustomerDO;
import com.hundsun.demo.commom.core.model.ProductsDO;
import com.hundsun.demo.spring.mybatis.CustomerMapper;
import com.hundsun.demo.spring.mybatis.MyBatisOperationType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.db.dynamicdb
 * @className: DynamicTestServiceImpl
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/14 0:01
 */

public class DynamicTestServiceImpl implements DynamicTestService, ApplicationContextAware {

    /**
     * applicationContext - 获取 JdbcTemplate bean
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
}

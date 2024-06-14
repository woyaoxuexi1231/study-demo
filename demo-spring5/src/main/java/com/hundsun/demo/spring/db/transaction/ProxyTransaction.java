package com.hundsun.demo.spring.db.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author hulei42031
 * @since 2024-03-27 14:06
 */

@Slf4j
public class ProxyTransaction implements ApplicationContextAware {

    /**
     * applicationContext - 获取 JdbcTemplate bean
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void handleTransaction() {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("masterDataSourceJdbcTemplate");
        jdbcTemplate.execute("update customers set phone = '40.32.ProxyTransaction' where customerNumber = '103'");
        throw new RuntimeException("更新失败! ");
    }
}

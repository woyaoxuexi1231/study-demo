package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.spring.model.pojo.CustomerDO;
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
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate1");
        return jdbcTemplate.query("select * from customers limit 0,10", new BeanPropertyRowMapper<>(CustomerDO.class));
    }

}

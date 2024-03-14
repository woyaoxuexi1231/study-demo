package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.commom.core.model.CustomerDO;
import com.hundsun.demo.spring.service.YiibaidbService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
@Slf4j
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

    public void handleTransaction() {
        // 1. 手动开启事务
        PlatformTransactionManager pm = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = pm.getTransaction(definition);
        try {
            JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
            jdbcTemplate.execute("update customers set phone = '40.32.9999' where customerNumber = '103'");
            throw new RuntimeException("更新失败! ");
        } catch (Exception e) {
            log.error("SQL执行异常, 准备回滚... ", e);
            status.setRollbackOnly();
        }

        if (status.isRollbackOnly()) {
            pm.rollback(status);
            log.info("回滚成功! ");
        } else {
            pm.commit(status);
            log.info("提交成功! ");
        }

    }

}

package com.hundsun.demo.spring.db.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.db
 * @className: SpringTransactionService
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/14 22:38
 */

@Slf4j
public class SpringTransactionService implements ApplicationContextAware {

    /**
     * applicationContext - 获取 JdbcTemplate bean
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * spring手动提交事务
     */
    public void handleTransaction() {
        // 1. 手动开启事务
        PlatformTransactionManager pm = (PlatformTransactionManager) applicationContext.getBean("masterDataSourceTransactionManager");
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = pm.getTransaction(definition);
        try {
            JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("masterDataSourceJdbcTemplate");
            jdbcTemplate.execute("update customers set phone = '40.32.SpringTransactionService' where customerNumber = '103'");
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

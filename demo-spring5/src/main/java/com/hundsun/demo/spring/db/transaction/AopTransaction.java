package com.hundsun.demo.spring.db.transaction;

import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author hulei42031
 * @since 2024-03-27 15:35
 */

@Setter
public class AopTransaction {

    JdbcTemplate jdbcTemplate;

    public void handleTransaction() {
        jdbcTemplate.execute("update customers set phone = '40.32.AopTransaction' where customerNumber = '103'");
        throw new RuntimeException("更新失败! ");
    }
}

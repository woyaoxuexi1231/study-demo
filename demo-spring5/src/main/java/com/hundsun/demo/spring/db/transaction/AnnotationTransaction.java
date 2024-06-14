package com.hundsun.demo.spring.db.transaction;

import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hulei42031
 * @since 2024-03-27 15:57
 */

@Setter
public class AnnotationTransaction {

    JdbcTemplate jdbcTemplate;

    @Transactional
    public void handleTransaction() {
        jdbcTemplate.execute("update customers set phone = '40.32.AnnotationTransaction' where customerNumber = '103'");
        throw new RuntimeException("更新失败! ");
    }
}

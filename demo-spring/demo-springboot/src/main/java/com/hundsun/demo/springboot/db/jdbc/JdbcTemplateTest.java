package com.hundsun.demo.springboot.db.jdbc;

import com.hundsun.demo.commom.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woaixuexi
 * @since 2024/4/3 0:40
 */

@RestController
@RequestMapping("/jdbctemplate")
public class JdbcTemplateTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @RequestMapping("/test")
    public void test() {
        System.out.println(jdbcTemplate.query("select * from users", BeanPropertyRowMapper.newInstance(User.class)).size());
        jdbcTemplate.execute("insert users (name) values ('hulei');");
        System.out.println(jdbcTemplate.query("select * from users", BeanPropertyRowMapper.newInstance(User.class)).size());
    }
}

package org.hulei.springboot.jdbc.jdbc;

import org.hulei.commom.core.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Statement;
import java.util.Objects;

/**
 * @author woaixuexi
 * @since 2024/4/3 0:40
 */

@Slf4j
@RestController
@RequestMapping("/jdbctemplate")
public class JdbcTemplateTest {

    @Autowired
    JdbcTemplate template;

    @Transactional
    @RequestMapping("/test")
    public void test() {
        System.out.println(template.query("select * from users", BeanPropertyRowMapper.newInstance(User.class)).size());
        template.execute("insert users (name) values ('hulei');");
        System.out.println(template.query("select * from users", BeanPropertyRowMapper.newInstance(User.class)).size());
    }

    /**
     * 获取插入数据的主键
     */
    @RequestMapping(value = "insertAndGetId")
    public void insertAndGetId() {
        String sql = "insert users (name) values ('hulei')";
        // 获取插入数据的自增主键
        KeyHolder holder = new GeneratedKeyHolder();
        template.update(connection -> connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), holder);
        int id = Objects.requireNonNull(holder.getKey()).intValue();
        log.info("数据插入成功, 返回的id为: " + id);
    }
}

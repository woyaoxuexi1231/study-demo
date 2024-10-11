package org.hulei.springboot.jdbc.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.jdbc.entity.EmployeeDO;
import org.hulei.springboot.jdbc.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/tongpeifu")
    public void tongpeifu() {
        /*
         BeanPropertyRowMapper springjdbc提供的一种默认的关系映射工具
         默认认为数据库列名为下划线形式,javabean为驼峰形式,这两者之间可以进行映射
         */
        template.query(
                "select * from employees e where e.first_name like ? and e.last_name like ?",
                BeanPropertyRowMapper.newInstance(EmployeeDO.class),
                new Object[]{"%a%", "%t%"}
        ).forEach(System.out::println);

        // SingleColumnRowMapper用于处理单行结果,如果出现多行会报错 Incorrect column count: expected 1, actual 9
        template.query(
                "select email from employees e where e.first_name like ? and e.last_name like ?",
                SingleColumnRowMapper.newInstance(String.class),
                new Object[]{"%a%", "%t%"}
        ).forEach(System.out::println);

        template.query(
                "select * from employees e where e.first_name like ? and e.last_name like ?",
                (rs, rowNum) -> rs.getString("last_name"),
                new Object[]{"%a%", "%t%"}
        ).forEach(System.out::println);
    }
}

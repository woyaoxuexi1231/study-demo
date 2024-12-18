package org.hulei.springboot.jdbc.template;

import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.Employees;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * @author woaixuexi
 * @since 2024/4/3 0:40
 */

@Slf4j
@RestController
@RequestMapping("/jdbc-template")
public class JdbcTemplateController {

    final JdbcTemplate jdbcTemplate;

    public JdbcTemplateController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获取插入数据的主键
     */
    @RequestMapping(value = "get-generated-key")
    public void getGeneratedKey() {
        /*
        这里涉及两个类
        1. PreparedStatementCreator - 顾名思义，这是用于创建 PreparedStatement 对象
        2. GeneratedKeyHolder - 它是 KeyHolder接口的一个通用实现类，专门用于接收和保存数据库新增记录时生成的自增长主键值。

        Spring框架为了解决获取自增主键值的问题, 使用这两个类来执行 update方法。
        在执行完插入操作后，GeneratedKeyHolder将包含新增记录的主键值。
         */
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "insert users (name) values (?)";
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        // 这里配置 preparedStatement 返回自增键，随后 spring 会把自增键塞到 KeyHolder 中去
                        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, JMockData.mock(String.class));
                        return preparedStatement;
                    }
                },
                holder
        );

        Long generatedId = Optional.ofNullable(holder.getKeyList().get(0))
                .map(Object::toString) // 通常返回的是 String类型，需要转换
                .map(Long::parseLong)  // 转换为 Long类型
                .orElse(-1L);          // 如果没有获取到主键，则返回-1（或适当的默认值）

        log.info("数据插入成功, 返回的id为: {}", generatedId);
    }

    @GetMapping("/wildcard")
    public void wildcard() {
        /*
        JdbcTemplate 内部使用了 PreparedStatement 来执行查询和更新操作，PreparedStatement 通过将参数值与 SQL 语句分离来防止 SQL 注入攻击。
        如果使用了正确的参数化查询方法，通配符是安全的，不会有 SQL 注入风险。
         */
        jdbcTemplate.query(
                        "select e.email from employees e where e.employee_number = ?",
                        SingleColumnRowMapper.newInstance(String.class),
                        new Object[]{1002})
                .forEach(entity -> log.info("{}", entity));

        jdbcTemplate.query(
                "select * from employees e where e.first_name like ? and e.last_name like ?",
                BeanPropertyRowMapper.newInstance(Employees.class),
                "%a%", "%t%"
        ).forEach(entity -> log.info("{}", entity));
    }

    @GetMapping("/rowMapper")
    public void rowMapper() {

        /*
        jdbc 仅提供了 ResultSet 结果集来获取查询结果，需要进行非常复杂的字段映射
        Spring 在这方面做了简化，使用 RowMapper 接口提供了一种列到字段的关系映射

        Spring RowMapper 用于将结果集映射到 Java 对象，spring默认提供了一些实现。
        1. BeanPropertyRowMapper<T> 这个 RowMapper 将每一行数据映射到指定类的实例上，基于反射机制将列名与类的属性名进行匹配。
           默认情况下，它认为数据库列名是下划线形式（例如 first_name），而 JavaBean 的属性名是驼峰形式（例如 firstName）。这两者之间可以自动进行映射。
        2. ColumnMapRowMapper 这个 RowMapper 将每一行数据映射到一个 Map<String, Object> 对象，其中键是列名，值是列的值。
        3. SingleColumnRowMapper 这个 RowMapper 将结果集中的单列映射到一个指定类型的对象上，适用于查询单列数据的场景。

        也可以根据需求自定义不同的 RowMapper
         */

        jdbcTemplate.query(
                        "select * from employees e where e.employee_number = 1002",
                        BeanPropertyRowMapper.newInstance(Employees.class))
                .forEach(entity -> log.info("BeanPropertyRowMapper : {}", entity));

        jdbcTemplate.query(
                        "select e.email from employees e where e.employee_number = 1002",
                        SingleColumnRowMapper.newInstance(String.class))
                .forEach(entity -> log.info("SingleColumnRowMapper : {}", entity));

        jdbcTemplate.query(
                        "select * from employees e where e.employee_number = 1002",
                        (RowMapper<Object>) (rs, rowNum) -> rs.getString("last_name"))
                .forEach(entity -> log.info("custom lambda : {}", entity));
    }
}

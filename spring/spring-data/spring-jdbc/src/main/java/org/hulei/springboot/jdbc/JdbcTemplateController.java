package org.hulei.springboot.jdbc;

import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.jdbc.entity.EmployeeDO;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * @author woaixuexi
 * @since 2024/4/3 0:40
 */

@Slf4j
@RestController
@RequestMapping("/jdbctemplate")
public class JdbcTemplateController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 获取插入数据的主键
     */
    @RequestMapping(value = "insertAndGetId")
    public void insertAndGetId() {

        String sql = "insert users (name) values (?)";
        /*
        Spring框架为了解决这一问题，提供了GeneratedKeyHolder类。它是KeyHolder接口的一个通用实现类，专门用于接收和保存数据库新增记录时生成的自增长主键值。
        当使用Spring的JdbcTemplate进行数据库操作时，可以通过传递一个PreparedStatementCreator和一个KeyHolder（通常是GeneratedKeyHolder的实例）给JdbcTemplate的update方法。
        在执行完插入操作后，GeneratedKeyHolder将包含新增记录的主键值。
         */
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
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

    @GetMapping("/wildcardAndRowMapper")
    public void wildcardAndRowMapper() {

        /*
        BeanPropertyRowMapper 是 Spring JDBC 提供的一种默认的关系映射工具。它的主要功能是将数据库查询结果自动映射到 JavaBean 对象中。
        默认情况下，它认为数据库列名是下划线形式（例如 first_name），而 JavaBean 的属性名是驼峰形式（例如 firstName）。
        这两者之间可以自动进行映射。
         */
        jdbcTemplate.query(
                "select * from employees e where e.first_name like ? and e.last_name like ?",
                BeanPropertyRowMapper.newInstance(EmployeeDO.class),
                new Object[]{"%a%", "%t%"}
        ).forEach(entity -> {
            log.info("BeanPropertyRowMapper : {}", entity);
        });

        /*
        SingleColumnRowMapper 用于处理单列结果,如果出现多列会报错 Incorrect column count: expected 1, actual x
         */
        jdbcTemplate.query(
                "select e.email from employees e where e.employee_number = ?",
                SingleColumnRowMapper.newInstance(String.class),
                new Object[]{1002}
        ).forEach(entity -> {
            log.info("SingleColumnRowMapper : {}", entity);
        });

        /*
        使用 Lambda 表达式自定义 RowMapper，以处理更复杂的映射需求。
         */
        jdbcTemplate.query(
                "select * from employees e where e.first_name like ? and e.last_name like ?",
                new RowMapper<Object>() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("last_name");
                    }
                },
                "%a%", "%t%"
        ).forEach(entity -> {
            log.info("custom lambda : {}", entity);
        });
    }
}

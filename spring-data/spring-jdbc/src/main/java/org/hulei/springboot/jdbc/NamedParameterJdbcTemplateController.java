package org.hulei.springboot.jdbc;

import org.hulei.springboot.jdbc.entity.EmployeeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/10 14:48
 */

@RestController
public class NamedParameterJdbcTemplateController {

    /**
     * 相较于jdbctemplate,他允许使用命名参数,jdbctemplate只能使用?作为占位符. 使用命名参数可读性和可维护性要强得多
     */
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @GetMapping("/selectEmployees")
    public void selectEmployees() {

        String sql = "SELECT * FROM employees WHERE last_name = :name";  // 使用命名参数 :name

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", "Patterson");  // 设置参数的值

        namedParameterJdbcTemplate
                .query(sql, parameters, BeanPropertyRowMapper.newInstance(EmployeeDO.class))
                .forEach(System.out::println);
    }
}

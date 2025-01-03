package org.hulei.springboot.jdbc.template;

import org.hulei.entity.mybatisplus.domain.Employees;
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
     * NamedParameterJdbcTemplate 是对 JdbcTemplate 的扩展
     * 支持使用命名参数而不是传统的 ? 占位符。这使得 SQL 语句更具可读性，特别是在需要大量参数时。
     */
    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NamedParameterJdbcTemplateController(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @GetMapping("/selectEmployees")
    public void selectEmployees() {

        String sql = "SELECT * FROM employees WHERE last_name = :name";  // 使用命名参数 :name

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", "Patterson");  // 设置参数的值

        namedParameterJdbcTemplate
                .query(sql,
                        parameters,
                        BeanPropertyRowMapper.newInstance(Employees.class))
                .forEach(System.out::println);
    }
}

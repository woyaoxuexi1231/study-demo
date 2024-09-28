package com.hundsun.demo.spring.db.jdbc;

import org.hulei.commom.core.model.pojo.CustomerDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdbc
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-12 10:50
 */

@Slf4j
public class DataSourceTest {


    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://192.168.80.128:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "123456";

    /*
    在 Java 中，连接 Oracle 数据库时可以使用 SID 或服务名作为连接标识，对应的 JDBC URL 格式如下：

    使用 SID 连接的示例代码：
    String jdbcUrl = "jdbc:oracle:thin:@host:port:SID";
    String username = "your-username";
    String password = "your-password";

    使用服务名连接的示例代码：
    String jdbcUrl = "jdbc:oracle:thin:@//host:port/serviceName";
    String username = "your-username";
    String password = "your-password";

    你需要将示例代码中的 `host`、`port`、`SID/serviceName`、`username` 和 `password` 替换为你实际的连接信息和凭据。
     */
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String ORACLE_USER = "SCOTT";
    private static final String ORACLE_PASS = "Abcd1234";

    // private static final String ORACLE_SQL = "select * from DEPT";
    private static final String MYSQL_SQL = "select * from customers limit 0,3";

    public static void main(String[] args) {
        springDataSource();
    }

    /**
     * 通过 spring 的 DriverManagerDataSource + JdbcTemplate 访问
     * <p>
     * 常见的 DataSource 实现类有:
     * org.apache.common.dbcp.BasicDataSource
     * org.springframework.jdbc.datasource.DriverManagerDataSource - 每次都获取都返回一个新的数据库连接, 没有提供缓冲池的功能, 在某些情况下应该避免将其应用于生产环境
     * org.springframework.jdbc.datasource.SingleConnectionDataSource - 每次都返回同一个数据库连接
     * <p>
     * 下面是拥有缓冲池功能的 DataSource:
     * HikariDataSource
     * alibaba Druid
     */
    public static void springDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        dataSource.setDriverClassName(MYSQL_DRIVER);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // JdbcTemplate 内部会通过 DataSourceUtils 来获取连接
        List<CustomerDO> customerDOS = jdbcTemplate.query(MYSQL_SQL, new BeanPropertyRowMapper<>(CustomerDO.class));
        System.out.println(customerDOS);
    }
}

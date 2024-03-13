package com.hundsun.demo.spring.jdbc;

import com.hundsun.demo.commom.core.model.CustomerDO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdbc
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-12 10:50
 */

public class DataSourceTest {


    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://192.168.80.128:3306/yiibaidb?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "123456";

    /*
    在 Java 中，连接 Oracle 数据库时可以使用 SID 或服务名作为连接标识，对应的 JDBC URL 格式如下：

    1. 使用 SID 连接 Oracle 数据库的 JDBC URL 格式：
       ```
       jdbc:oracle:thin:@host:port:SID
       ```
       其中，`host` 是 Oracle 数据库服务器的主机名或 IP 地址，`port` 是 Oracle 数据库服务器监听的端口号（默认为 1521），`SID` 是数据库的系统标识符。

    2. 使用服务名连接 Oracle 数据库的 JDBC URL 格式：
       ```
       jdbc:oracle:thin:@//host:port/serviceName
       ```
       其中，`host` 是 Oracle 数据库服务器的主机名或 IP 地址，`port` 是 Oracle 数据库服务器监听的端口号（默认为 1521），`serviceName` 是数据库的服务名。

    请注意，在使用服务名连接数据库时，URL 的开头需要添加双斜杠（`//@`）。

    以下是两种连接方式的示例代码：

    使用 SID 连接的示例代码：
    ```java
    String jdbcUrl = "jdbc:oracle:thin:@host:port:SID";
    String username = "your-username";
    String password = "your-password";
    ```

    使用服务名连接的示例代码：
    ```java
    String jdbcUrl = "jdbc:oracle:thin:@//host:port/serviceName";
    String username = "your-username";
    String password = "your-password";
    ```

    你需要将示例代码中的 `host`、`port`、`SID/serviceName`、`username` 和 `password` 替换为你实际的连接信息和凭据。
     */
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String ORACLE_USER = "SCOTT";
    private static final String ORACLE_PASS = "Abcd1234";


    // private static final String ORACLE_SQL = "select * from DEPT";

    private static final String MYSQL_SQL = "select * from customers limit 0,3";


    public static void main(String[] args) {
        jdbcTransaction();
    }


    public static void jdbcTransaction() {

        /*
        jdbc 的一个简单的事务小测试
        jdbc 默认的隔离级别是读已提交
        这里使用两个事务, 一个写, 一个更新
        这里读事务不会读取写事务未提交的事务
         */

        new Thread(() -> jdbc(statement -> {

            statement.execute("update customers set phone = '40.32.2554' where customerNumber = '103' ");

            statement.execute("update customers set phone = '7025551839' where customerNumber = '112' ");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new SQLException("执行失败, 准备回滚...");

        })).start();

        new Thread(() -> jdbc(statement -> {
            ResultSet rs = statement.executeQuery(MYSQL_SQL);
            while (rs.next()) {
                // 通过字段检索
                System.out.println(rs.getInt("customerNumber") + " - " + rs.getString("customerName") + " - " + rs.getString("phone"));
            }
        })).start();

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

    /**
     * 通过 JDBC 直连
     */
    public static void jdbc(SqlExecuteFunc consumer) {

        Connection connection = null;
        Statement statement = null;

        try {

            // 注册 JDBC 驱动类
            Class.forName(MYSQL_DRIVER);

            // 获取连接, 实例化 Statement 对象
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            statement = connection.createStatement();
            // rConn.setTransactionIsolation(4);

            // 关闭事务的自动提交
            connection.setAutoCommit(false);

            boolean isRollback = false;
            try {
                // 执行操作
                consumer.execute(statement);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                isRollback = true;
                connection.rollback();
            }

            if (!isRollback) {
                connection.commit();
            }

            /*
            手动提交事务
             */
            if (isRollback) {

            } else {
                connection.commit();
            }

            // 关闭连接
            statement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            // 找不到 JDBC 驱动类
            throw new RuntimeException(e);
        } catch (SQLException e) {
            // 连接获取失败
            throw new RuntimeException(e);
        } finally {

            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                // throw new RuntimeException(e);
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}

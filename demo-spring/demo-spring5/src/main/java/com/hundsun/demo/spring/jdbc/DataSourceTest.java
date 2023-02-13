package com.hundsun.demo.spring.jdbc;

import com.hundsun.demo.spring.model.pojo.CustomerDO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdbc
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-12 10:50
 */

public class DataSourceTest {


    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://192.168.175.128:3306/yiibaidb?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "123456";

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
            try {
                statement.execute("update customers set phone = '40.32.2554' where customerNumber = '103' ");
                Thread.sleep(2000);
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, true)).start();

        new Thread(() -> jdbc(statement -> {
            try {
                ResultSet rs = statement.executeQuery(MYSQL_SQL);
                while (rs.next()) {
                    // 通过字段检索
                    System.out.println(rs.getInt("customerNumber") + " - " + rs.getString("customerName") + " - " + rs.getString("phone"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, false)).start();

    }

    /**
     * 通过 spring 的 DriverManagerDataSource + JdbcTemplate 访问
     */
    public static void springDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        dataSource.setDriverClassName(MYSQL_DRIVER);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<CustomerDO> customerDOS = jdbcTemplate.query(MYSQL_SQL, new BeanPropertyRowMapper<>(CustomerDO.class));
        System.out.println(customerDOS);
    }

    /**
     * 通过 JDBC 直连
     */
    public static void jdbc(Consumer<Statement> consumer, boolean isRollback) {

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

            // 执行操作
            consumer.accept(statement);

            /*
            手动提交事务
             */
            if (isRollback) {
                connection.rollback();
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

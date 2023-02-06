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

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdbc
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-12 10:50
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
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

    private static final String MYSQL_SQL = "select * from customers limit 0,10";


    public static void main(String[] args) {
        jdbc();
    }

    /**
     * 通过 spring 的 DriverManagerDataSource + JdbcTemplate 访问
     */
    public static void springDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        dataSource.setDriverClassName(MYSQL_DRIVER);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Object object = jdbcTemplate.query(MYSQL_SQL, new BeanPropertyRowMapper<>(CustomerDO.class));

        System.out.println(object);
    }

    /**
     * 通过 JDBC 直连
     */
    public static void jdbc() {

        Connection connection = null;
        Statement statement = null;

        try {

            // 注册 JDBC 驱动类
            Class.forName(MYSQL_DRIVER);
            // 获取连接
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            // 实例化 Statement 对象
            statement = connection.createStatement();

            // 处理结果
            ResultSet rs = statement.executeQuery(MYSQL_SQL);
            while (rs.next()) {
                // 通过字段检索
                System.out.println(rs.getString("customerName") + " - " + rs.getString("phone"));
            }

            // 完成后关闭
            rs.close();
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

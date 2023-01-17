package com.hundsun.demo.spring.jdbc;

import com.hundsun.demo.spring.model.pojo.Student;
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
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "123456";

    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String ORACLE_USER = "SCOTT";
    private static final String ORACLE_PASS = "Abcd1234";


    private static final String ORACLE_SQL = "select * from DEPT";

    private static final String MYSQL_SQL = "select * from student";



    /**
     * 通过 spring 的 DriverManagerDataSource + JdbcTemplate 访问
     */
    public static void springDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(MYSQL_URL,MYSQL_USER,MYSQL_PASS);
        dataSource.setDriverClassName(MYSQL_DRIVER);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Object object = jdbcTemplate.query(MYSQL_SQL,new BeanPropertyRowMapper<>(Student.class));

        System.out.println(object);
    }

    /**
     * 通过 JDBC 直连
     */
    public static void jdbc() {


        Connection connection = null;
        Statement statement = null;
        try {
            // 注册 jdbc 驱动类
            Class.forName(ORACLE_DRIVER);
            // 获取连接
            connection = DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASS);
            // 实例化 Statement 对象
            statement = connection.createStatement();


            ResultSet rs = statement.executeQuery(ORACLE_SQL);

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int DEPTNO = rs.getInt("DEPTNO");
                String DNAME = rs.getString("DNAME");
                String LOC = rs.getString("LOC");

                // 输出数据
                System.out.print("DEPTNO: " + DEPTNO);
                System.out.print(", DNAME: " + DNAME);
                System.out.print(", LOC: " + LOC);
                System.out.println();
            }
            // 完成后关闭
            rs.close();
            statement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            // 找不到 jdbc 驱动类, 后面的操作都没意义了, 连不上
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

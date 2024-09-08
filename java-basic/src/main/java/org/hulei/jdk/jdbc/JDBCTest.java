package org.hulei.jdk.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author hulei
 * @since 2024/9/8 15:48
 */

@Slf4j
public class JDBCTest {

    public static void main(String[] args) {
        jdbcTransaction();
    }

    /**
     * 最原始的 jdbc 数据库使用
     *
     * @param consumer
     */
    public static void jdbc(SqlExecuteFunc consumer) {

        Connection connection = null;
        Statement statement = null;

        try {

            // 注册 JDBC 驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 获取连接, 实例化 Statement 对象
            connection = DriverManager.getConnection(
                    "jdbc:mysql://192.168.80.128:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai",
                    "root",
                    "123456");
            statement = connection.createStatement();
            // rConn.setTransactionIsolation(4);

            // 关闭事务的自动提交
            connection.setAutoCommit(false);

            boolean isRollback = false;
            try {
                // 执行操作
                consumer.execute(statement);
            } catch (Exception e) {
                log.error("execute error,", e);
                isRollback = true;
            }
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
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                // throw new RuntimeException(e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                log.info("sql exception - ", se);
            }
        }
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
            ResultSet rs = statement.executeQuery("select * from customers limit 0,3");
            while (rs.next()) {
                // 通过字段检索
                System.out.println(rs.getInt("customerNumber") + " - " + rs.getString("customerName") + " - " + rs.getString("phone"));
            }
        })).start();

    }
}

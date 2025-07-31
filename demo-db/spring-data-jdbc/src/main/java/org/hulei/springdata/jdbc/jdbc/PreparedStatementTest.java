package org.hulei.springdata.jdbc.jdbc;

import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PreparedStatement 是 JDBC 中用于执行预编译 SQL 语句的接口。
 * <p>
 * 1. 性能提升：预编译的 SQL 语句可以在执行前进行一次编译，这样多次执行相同的 SQL 语句时就不需要重复编译，从而提高了执行速度。
 * 2. 安全性：通过使用 PreparedStatement，可以防止 SQL 注入攻击。用户输入的数据会被自动转义，确保安全。
 * 3. 可读性和维护性：预编译的 SQL 语句可以使用占位符，使代码更清晰、更易于维护。
 *
 * @author hulei
 * @since 2024/9/28 23:29
 */


public class PreparedStatementTest {

    /**
     * 预编译语言(预备语言)
     * 1.数据库会把 sql 语句编译成一个查询执行计划,可以多次复用,避免每次执行相同的 sql 重新编译
     * 2.编译后的 sql 参数使用占位符表示, 每次执行的时候具体的参数被绑定到占位符上进行替换
     * 3.参数在编译阶段明确类型化, 不合法的字符会被进行转义, 避免了sql注入的问题
     */
    static PreparedStatement queryPS;
    static PreparedStatement updatePS;

    static {
        try {
            queryPS = ConnectFactory.getConnection().prepareStatement("select * from users where name = ?");
            updatePS = ConnectFactory.getConnection().prepareStatement("update users set name = ? where id = ?");
            // PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static void main(String[] args) {

        /*
        PreparedStatement 之所以是安全的，可以在这些 setXXX 方法中找到答案
        拿 setString 举例子，在 mysql的启动实现类 ClientPreparedStatement 这个类使用 com.mysql.cj.ClientPreparedQueryBindings.setString 这个方法进行转义操作
         */

        queryPS.setString(1, "zhangsan");
        ResultSet resultSet = queryPS.executeQuery();
        CommonUtil.simplePrintResultSet(resultSet);

        updatePS.setString(1, "psUpdate");
        updatePS.setInt(2, 1);
        int executed = updatePS.executeUpdate();
        System.out.printf("修改了 %d 条数据%n", executed);
        ConnectFactory.getConnection().commit();
    }
}

package org.hulei.jdk.jdbc;

import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
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
            queryPS = ConnectFactory.getConnection().prepareStatement("select * from users where id = ?");
            updatePS = ConnectFactory.getConnection().prepareStatement("update users set name = ? where id = ?");
            // PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        queryPS.setInt(1, 1);
        ResultSet resultSet = queryPS.executeQuery();
        CommonUtil.prettyPrintResultSet(resultSet);

        updatePS.setString(1, "psUpdate");
        updatePS.setInt(2, 1);
        int executed = updatePS.executeUpdate();
        System.out.printf("修改了 %d 条数据%n", executed);
        ConnectFactory.getConnection().commit();
    }
}

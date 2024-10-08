package org.hulei.jdk.jdbc;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author hulei
 * @since 2024/9/28 23:25
 */

@Slf4j
public class ConnectFactory {

    @Getter
    private static final Connection connection;

    static {
        try {
            // 根据不同的数据库,注册这个数据库指定的驱动器类
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 数据库连接信息,jdbc使用一种与普通URL相似的语法来描述数据库,不同的数据库有不同的连接标准
            // logger=com.mysql.cj.log.StandardLogger&profileSQL=true 可以打印jdbc的操作日志和sql日志
            // allowMultiQueries=true
            String url = "jdbc:mysql://192.168.80.128:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&logger=com.mysql.cj.log.StandardLogger&profileSQL=true&allowMultiQueries=true";
            String username = "root";
            String password = "123456";
            // 连接数据库
            connection = DriverManager.getConnection(url, username, password);
            // 关闭事务的自动提交
            connection.setAutoCommit(false);
            // connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (ClassNotFoundException e) {
            // 找不到 JDBC 驱动类
            throw new RuntimeException(e);
        } catch (SQLException e) {
            // 连接获取失败
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Statement getStatement() {
        return connection.createStatement();
    }

    @SneakyThrows
    public static Statement getUpdatealeStatement() {
        // todo 这些参数的含义
        return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    /**
     * finalize() 方法是 Object 类的一个保护方法，它可以在一个对象被垃圾收集器回收之前由垃圾收集器调用。
     * 这个方法的主要目的是允许对象在销毁前执行一些清理工作，比如关闭文件句柄或网络连接等。
     * <p>
     * 但是 finalize() 方法存在一些问题
     * 1. 不确定性:这个方法的调用取决于垃圾收集器的实现和运行时的行为
     * 2. 性能影响:垃圾收集器需要做额外的工作来调用finalize()方法
     * 3. 资源管理不当:由于调用的不确定性,可能导致资源释放的不及时,资源一直被占用
     * 4. 复杂性增加
     * 由于上述原因 finalize() 在JDK9被标记为 deprecated
     * 后续推荐使用 try-with-resources 来手动释放资源
     */
    @Override
    protected void finalize() {
        try {
            connection.close();
        } catch (SQLException se) {
            log.error("sql exception", se);
        }
    }

    /**
     * 打印 ResultSet 中的所有数据，并使每一列的输出对齐。
     *
     * @param rs 要打印的 ResultSet 对象。
     * @throws SQLException 如果访问数据库时发生错误。
     */
    public static void printResultSet(ResultSet rs) throws SQLException {

        if (rs == null) {
            System.out.println("ResultSet is null.");
            return;
        }

        // 检索列的数量和名字
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 获取每列的最大宽度
        int[] columnWidths = new int[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnWidths[i - 1] = metaData.getColumnName(i).length();
        }

        // 遍历 ResultSet 计算每列数据的最大宽度
        rs.beforeFirst();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                int columnLength = rs.getString(i) != null ? rs.getString(i).length() : 4; // 如果值为空，则假定长度为4
                if (columnLength > columnWidths[i - 1]) {
                    columnWidths[i - 1] = columnLength;
                }
            }
        }

        // 打印表头
        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) System.out.print(" | ");
            String columnName = metaData.getColumnName(i);
            System.out.printf("%-" + columnWidths[i - 1] + "s", columnName);
        }
        System.out.println();

        // 打印分隔线
        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) System.out.print("-+-");
            for (int j = 0; j < columnWidths[i - 1]; j++) {
                System.out.print("-");
            }
        }
        System.out.println();

        // 遍历 ResultSet 并打印每一行
        rs.beforeFirst(); // 再次将指针移动回结果集的开头
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) System.out.print(" | ");

                // 根据列类型获取值并打印
                String value;
                switch (metaData.getColumnType(i)) {
                    case java.sql.Types.INTEGER:
                        value = String.valueOf(rs.getInt(i));
                        break;
                    case java.sql.Types.VARCHAR:
                    case java.sql.Types.CHAR:
                        value = rs.getString(i);
                        break;
                    case java.sql.Types.DOUBLE:
                    case java.sql.Types.FLOAT:
                        value = String.valueOf(rs.getDouble(i));
                        break;
                    case java.sql.Types.DATE:
                        value = String.valueOf(rs.getDate(i));
                        break;
                    case java.sql.Types.TIMESTAMP:
                        value = String.valueOf(rs.getTimestamp(i));
                        break;
                    default:
                        value = String.valueOf(rs.getObject(i));
                        break;
                }

                System.out.printf("%-" + columnWidths[i - 1] + "s", value);
            }
            System.out.println(); // 每打印完一行换行
        }

        System.out.println();
    }

}

package org.hulei.basic.jdk.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.function.Consumer;

/**
 * @author hulei
 * @since 2024/9/29 9:57
 */

@Slf4j
public class CommonUtil {

    /**
     * 打印 ResultSet 中的所有数据，并使每一列的输出对齐。
     *
     * @param rs 要打印的 ResultSet 对象。
     * @throws SQLException 如果访问数据库时发生错误。
     */
    public static void prettyPrintResultSet(ResultSet rs) throws SQLException {

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

        simplePrintResultSet(rs);
    }

    public static void simplePrintResultSet(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    System.out.print(" | ");
                }
                // 根据列类型获取值并打印
                String value = getColumnType(metaData, rs, i);
                System.out.print(value);
            }
            System.out.println(); // 每打印完一行换行
        }
    }

    public static String getColumnType(ResultSetMetaData metaData, ResultSet rs, int column) throws SQLException {
        switch (metaData.getColumnType(column)) {

            case Types.INTEGER:
                return String.valueOf(rs.getInt(column));
            case Types.VARCHAR:
            case Types.CHAR:
                return rs.getString(column);
            case Types.DOUBLE:
            case Types.FLOAT:
                return String.valueOf(rs.getDouble(column));
            case Types.DATE:
                return String.valueOf(rs.getDate(column));
            case Types.TIMESTAMP:
                return String.valueOf(rs.getTimestamp(column));
            default:
                return String.valueOf(rs.getObject(column));
        }
    }

    /**
     * 事务的提交和回滚
     *
     * @param consumer 执行的sql逻辑
     */
    public static void doExecuteUpdate(Consumer<Statement> consumer, Connection connection) throws SQLException {
        // ConnectFactory已经关闭了默认的数据库提交行为
        try {
            boolean isRollback = false;
            try {
                // 执行操作
                consumer.accept(connection.createStatement());
            } catch (Exception e) {
                log.error("execute error,", e);
                isRollback = true;
            }
            // 判断是否需要回滚
            if (isRollback) {
                connection.rollback();
            } else if (!connection.isReadOnly()) {
                connection.commit();
            }
        } catch (SQLException e) {
            log.info("execute error,", e);
        }
    }
}

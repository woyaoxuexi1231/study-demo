package org.hulei.springdata.jdbc.jdbc;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * @author hulei
 * @since 2024/9/29 14:48
 */

public class SavePointTest {

    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = ConnectFactory.getConnection();
        Statement statement = ConnectFactory.getStatement();

        String updateSQL1 = "update users set name = 'SavePoint' where id = 36";
        String updateSQL2 = "update users set name = 'SavePoint' where id = 37";
        statement.executeUpdate(updateSQL1);
        Savepoint savepoint = connection.setSavepoint(); // 带保存点的回滚,可以使得这个保存点之前的操作不会被回滚
        statement.executeUpdate(updateSQL2);
        connection.rollback(savepoint);
        connection.commit();
    }
}

package org.hulei.jdk.jdbc;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author hulei
 * @since 2024/9/29 10:03
 */

public class MoreResultTest {

    @SneakyThrows
    public static void main(String[] args) {
        Statement statement = ConnectFactory.getStatement();

        String usersQuery = "select id,name from users limit 0,1;";
        String updateSQL = "update users set name = 'moreResults' where id = 1;";
        String biguserQuery = "select * from biguser limit 0,1;";

        boolean isResult = statement.execute(usersQuery + updateSQL + biguserQuery);
        while (true) {
            if (isResult) {
                ResultSet rs = statement.getResultSet();
                ConnectFactory.printResultSet(rs);
            } else {
                int updateCount = statement.getUpdateCount();
                if (updateCount == -1) {
                    System.out.println("there is no more result");
                    break;
                } else {
                    System.out.println("Update count: " + updateCount);
                }
            }
            // true代表当前是ResultSet, false代表是一个updateCount
            isResult = statement.getMoreResults();
        }
    }
}

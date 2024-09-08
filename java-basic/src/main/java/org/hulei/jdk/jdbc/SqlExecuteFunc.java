package org.hulei.jdk.jdbc;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: SqlExecute
 * @description:
 * @author: h1123
 * @createDate: 2023/2/14 22:18
 */

@FunctionalInterface
public interface SqlExecuteFunc {

    void execute(Statement execute) throws SQLException;
}

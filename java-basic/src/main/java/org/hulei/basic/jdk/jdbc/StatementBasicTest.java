package org.hulei.basic.jdk.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Consumer;

/**
 * @author hulei
 * @since 2024/9/8 15:48
 */

@Slf4j
public class StatementBasicTest {

    /**
     * 数据库连接
     */
    public static Connection connection = ConnectFactory.getConnection();

    private final static String usersQuery = "select id,name from users limit 0,1;";
    private final static String bigUserQuery = "select * from big_user limit 0,1;";
    private final static String insertSQL = "insert into users(name) select 'zhangsan'";
    private final static String updateSQL = "update users set name = 'zhangsan' where id = 34";


    @SneakyThrows
    public static void main(String[] args) {
        // 简单的数据查询操作
        simpleStatement();

        // 使用可更新的 statement 操作
        updatableStatement();

        // 使用 execute 方法执行多个语句
        multipleResultSet();

        // 指定一个简单的 update 语句
        CommonUtil.doExecuteUpdate(getSimpleExecute(), connection);

        // 执行 insert 语句，并且获得自增主键
        CommonUtil.doExecuteUpdate(getGeneratedKeys(), connection);

        // 批量执行
        CommonUtil.doExecuteUpdate(getBatchUpdate(), connection);
    }

    @SneakyThrows
    public static void simpleStatement() {
        // 使用默认的 statement 操作
        Statement statement = connection.createStatement();
        /*
        一般来说一个 statement 只执行一个 sql，同时执行两个 sql 是否报错会根据具体的配置来决定。
        mysql 通过配置 allowMultiQueries=true 连接参数是可以执行多个 sql 的。
        但是对于 executeQuery 来说，仅能拿到第一个查询语句的结果，
        使用 execute 执行多条语句可以获取所有结果集的结果  org.hulei.jdk.jdbc.MoreResultOp 这个类有对多结果集的操作
         */
        ResultSet set = statement.executeQuery(usersQuery);
        CommonUtil.prettyPrintResultSet(set);
    }

    @SneakyThrows
    public static void updatableStatement() {

        // 这里获取一个不可滚动, 但是可以更新的 statement
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(usersQuery);
        while (resultSet.next()) {

            /* 这里关于使用 resultSet 获取数据集的时候需要注意的点 */

            // 1. 对于 resultSet 的下标索引它是根据 sql 列的返回顺序决定的, 默认从 1 开始而不是 0
            log.info("使用下标直接获取数据: id: {}, name: {}", resultSet.getLong(1), resultSet.getString(2));

            // 2. 使用列名来获取元素是一种更安全更方便的操作方式, 这些方法会进行合理转换, 如果数据库类型是 double类型, 但是我们使用 result.getString 来接收, jdbc 会进行合理的类型转换
            log.info("使用列名获取数据: id: {}, name: {}", resultSet.getLong("id"), resultSet.getString("name"));

            // 3. 通过 getObject 检索此 ResultSet 对象当前行中指定列的值，并将从列的 SQL 类型转换为请求的 Java 数据类型（如果支持转换）。如果不支持转换或为类型指定了 null，则会引发 SQLException。
            log.info("使用 getObject 获取 name: {}", resultSet.getObject("name", String.class));


            /* updatableStatement 这个 statement 的 resultSetConcurrency 参数是 CONCUR_UPDATABLE, 是支持更新的  */

            // 使用 updateXXX 方法可以用于更新数据, 这里实际会根据主键更新: UPDATE `test`.`users` SET `test`.`users`.`id`=1,`test`.`users`.`name`='updateString' WHERE `test`.`users`.`id`<=>1
            resultSet.updateString("name", "updateString");
            resultSet.updateRow();

            // 显示提交
            connection.commit();

            log.info("updateString 之后再获取 name: {}", resultSet.getObject("name", String.class));
        }

        // 对于返回更新的数量函数,用于返回最近一次查询的更改的数据的数量,如果sql返回的是ResultSet或者没有更新数据库都返回 -1
        System.out.println(statement.getUpdateCount());
    }

    @SneakyThrows
    public static void multipleResultSet() {

        Statement statement = ConnectFactory.getStatement();

        String usersQuery = "select id,name from users limit 0,1;";
        String updateSQL = "update users set name = 'moreResults' where id = 1;";
        String bigUserQuery = "select * from big_user limit 0,1;";

        boolean isResult = statement.execute(usersQuery + updateSQL + bigUserQuery);
        while (true) {
            if (isResult) {
                ResultSet rs = statement.getResultSet();
                CommonUtil.prettyPrintResultSet(rs);
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

    public static Consumer<Statement> getSimpleExecute() {
        return new Consumer<Statement>() {
            @SneakyThrows
            @Override
            public void accept(Statement statement) {
                /*
                方法返回的结果是受到 sql 影响的行数,或者对不返回行数的语句返回 0.
                executeUpdate可以执行 insert,update,delete 之类的操作, 也可以执行 create table和 drop table之类的数据定义语句.
                执行查询时必须使用 executeQuery.
                execute 可以执行任意的 sql, 此方法通常只用于有用户提供的交互式查询
                 */
                int updateCount = statement.executeUpdate(updateSQL);
                log.info("更新了 {} 条数据", updateCount);
            }
        };
    }

    public static Consumer<Statement> getGeneratedKeys() {
        return new Consumer<Statement>() {
            @SneakyThrows
            @Override
            public void accept(Statement statement) {
                /*
                Statement.RETURN_GENERATED_KEYS 能有效得帮助我们获取自动生成的键
                executeUpdate 有多个重载的方法,目前除了 executeUpdate(String sql, int autoGeneratedKeys) 能够成功外
                剩下两个都不知道怎么用 todo, 方法标签都表明 This method cannot be called on a PreparedStatement or CallableStatement.
                 */
                int insertCount = statement.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);
                // ResultSet接口初始化时被设定在第一行之前的位置,所以在访问元素的时候必须调用next方法将它移动到第一行
                // log.info("插入了 {} 条数据,生成的键为: {}", count, statement.getGeneratedKeys().getLong(1)); // 此处报错 java.sql.SQLException: Before start of result set
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    log.info("生成的键为: {}", id);
                }
                log.info("插入了 {} 条数据", insertCount);

            }
        };
    }

    public static Consumer<Statement> getBatchUpdate() {
        return new Consumer<Statement>() {
            @SneakyThrows
            @Override
            public void accept(Statement statement) {
                statement.addBatch(insertSQL);
                statement.addBatch(insertSQL);
                statement.addBatch(insertSQL);
                int[] results = statement.executeBatch();
                for (int result : results) {
                    log.info("执行结果: {}", result);
                }
            }
        };
    }

}

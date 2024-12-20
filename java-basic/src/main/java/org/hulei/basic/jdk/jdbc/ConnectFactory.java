package org.hulei.basic.jdk.jdbc;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
            /*
            通过调用 Class.forName 方法，Java 虚拟机会尝试加载 com.mysql.cj.jdbc.Driver 类。这是 MySQL Connector/J 的驱动类。
            在加载驱动类时，静态代码块会执行，该静态代码块会自动将驱动程序注册到 DriverManager 中。
            DriverManager 是一个管理 JDBC 驱动程序的类，它维护着一个可用于建立数据库连接的驱动程序列表。
            在 com.mysql.cj.jdbc.Driver 这个类的静态代码块部分有这部分代码 java.sql.DriverManager.registerDriver(new Driver());

            早期的 JDBC 程序中，需要显式地加载数据库驱动程序，以便 DriverManager 可以识别和使用它们。
            然而，自从 JDBC 4.0（Java 6 及以上版本）以来，如果驱动程序 JAR 文件包含 META-INF/services/java.sql.Driver 文件，则可以通过服务提供者机制自动加载驱动程序。
            因此，对于现代应用程序，显式调用 Class.forName 通常是不必要的。
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
            /*
            数据库连接信息,jdbc使用一种与普通URL相似的语法来描述数据库,不同的数据库有不同的连接标准
            useUnicode=true 指定是否使用 Unicode 字符编码。true 表示使用 Unicode 字符编码，以确保正确处理多语言字符。
            characterEncoding=UTF-8 指定字符编码为 UTF-8。UTF-8 是一种常用的字符编码，能够表示几乎所有的文字和符号。
            zeroDateTimeBehavior=convertToNull
            serverTimezone=Asia/Shanghai
            logger=com.mysql.cj.log.StandardLogger
            profileSQL=true
            allowMultiQueries=true
            useCursorFetch=true
             */
            @SuppressWarnings("StringBufferReplaceableByString")
            StringBuilder url = new StringBuilder("jdbc:mysql://192.168.3.101:3306/test?useUnicode=true&characterEncoding=UTF-8");
            // 指定如何处理 0000-00-00 00:00:00 这样的零日期时间值。convertToNull 表示将这些零日期时间值转换为 null。这是因为在某些 SQL 模式下，0000-00-00 00:00:00 不是有效的时间。
            url.append("&zeroDateTimeBehavior=convertToNull");
            // 指定数据库服务器的时区。Asia/Shanghai 表示上海时区。设置正确的时区可以避免时区差异导致的时间错误。
            url.append("&serverTimezone=Asia/Shanghai");
            // 可以打印 jdbc 的操作日志和sql日志
            url.append("&logger=com.mysql.cj.log.StandardLogger");
            // 启用 SQL 语句的性能分析。true 表示启用，这会在日志中记录 SQL 语句的执行时间和其他性能相关信息，便于优化和调试。
            url.append("&profileSQL=true");
            // 参数设置后允许在 execute 中执行多条语句
            url.append("&allowMultiQueries=true");
            // 允许游标查询设置fetchSize的大小
            url.append("&useCursorFetch=true");
            /*
            配置是否使用 com.mysql.cj.jdbc.ServerPreparedStatement，即预编译和转义在服务器进行。
            这里我配置了很久，发现false不生效，最后在源码的 com.mysql.cj.jdbc.JdbcPropertySetImpl.postInitialization 这个方法内找到了

            if (getBooleanProperty(PropertyKey.useCursorFetch).getValue()) {
                // assume server-side prepared statements are wanted because they're required for this functionality
                super.<Boolean>getProperty(PropertyKey.useServerPrepStmts).setValue(true);
            }
            TODO 开启游标查询之后，不管这个参数配置成什么，都必须使用 useServerPrepStmts=true
             */
            url.append("&useServerPrepStmts=false");
            String username = "root";
            String password = "123456";
            // 连接数据库
            connection = DriverManager.getConnection(url.toString(), username, password);
            // 关闭事务的自动提交
            connection.setAutoCommit(false);
            /*
            设置数据库连接的事务隔离级别（这里设置是会话级别的），数据库提供了四种标准的事务隔离级别，从最低到最高分别是：
            READ UNCOMMITTED：允许一个事务读取其他事务尚未提交的更改，可能会导致脏读（Dirty Read）。
            READ COMMITTED：只允许一个事务读取其他事务已经提交的更改，避免了脏读。
            REPEATABLE READ：确保在同一个事务中多次读取相同的数据行时，其值保持不变，避免了不可重复读（Non-repeatable Read）。
            SERIALIZABLE：提供最高的隔离级别，完全隔离事务，避免了幻读（Phantom Read），但性能开销也最大。

            mysql默认的事务隔离级别是 TRANSACTION_REPEATABLE_READ
             */
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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
        /*
        创建一个默认的 statement, Statement 是专门用于执行 sql 并返回执行结果的对象
        将使用两个默认的参数来配置数据集类型和数据集是否可更新

        TYPE_FORWARD_ONLY 和 CONCUR_READ_ONLY
         */
        return connection.createStatement();
    }

    @SneakyThrows
    public static Statement getUpdatableStatement() {
        /*
        默认情况下, 结果集是不支持滚动和更新的, 要获取可滚动和可更新的结果集需要两个参数
        resultSetType:
            TYPE_FORWARD_ONLY - 结果集不能滚动(默认)
            TYPE_SCROLL_INSENSITIVE - 结果集可以滚动, 但对数据库变化不敏感
            TYPE_SCROLL_SENSITIVE - 对数据库变化敏感, 查询结束后如果数据库数据发生变化, 后续如果滚动回来可以感知到这个变化
        resultSetConcurrency:
            CONCUR_READ_ONLY - 结果集不能用于更新数据库(默认)
            CONCUR_UPDATABLE - 结果集可以用于更新数据库
         */
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

}

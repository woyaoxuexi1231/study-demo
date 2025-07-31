package org.hulei.springdata.jdbc.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.util.utils.MyStopWatch;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author hulei
 * @since 2024/10/8 15:32
 */

@Slf4j
public class SelectModTest {

    public static final String sql100 = "select * from test.big_data_users limit 0,100";
    public static final String sql100000 = "select * from test.big_data_users limit 0,100000";
    public static final String sql10000 = "select * from test.big_data_products limit 0,10000";


    public static void main(String[] args) {

        /*
        普通查询、流式查询 和 游标查询 是针对数据量大小和处理方式不同而采用的三种查询技术。它们主要的区别在于结果的返回方式、内存占用和适用场景。

        普通查询（Standard Query）
            📌 特点：
             - 一次性将结果全部加载到内存中。数据量大时，占用大量内存。可能导致客户端或应用内存溢出（OOM）。
             - 查询执行完毕后，客户端可以立即获取全部数据。
             - 适合小数据量查询或需要对所有结果快速处理的场景。

        流式查询（Streaming Query）
            📌 特点：
             - 查询结果按行（或批量）逐步传输到客户端，不加载全部数据到内存。
             - JDBC 驱动或编程接口层面控制。
             - 常用于 Java / JDBC 等客户端。
          当使用 流式响应方式（即 Row-at-a-time Protocol） 时：
            - MySQL 逐行执行查询计划；
            - 每获取一行就立刻通过网络发送给客户端；
            - 没有结果集缓存，也不保留整份数据；
            - 内存中只维护当前行的中间状态。
            MySQL Server 端不会因流式查询造成巨大内存压力

        游标查询（Cursor Query）
            📌 特点：
             - 通常用于 存储过程 中。
             - 显式创建游标，然后逐条读取数据。
             - 支持服务端控制的逐条读取，对大数据操作更安全。
         */

        // normalSelectMod();
        streamSelectMod();
        cursorSelectMod();
    }

    /**
     * 普通查询
     * 应用代码简单,数据量小时操作速度快
     * 数据量大时容易导致OOM
     */
    public static void normalSelectMod() {
        try {
            Statement statement = ConnectFactory.getStatement();
            /*
            查询十万条数据,就显得有点力不从心了, 1.查询时间长 2.消耗内存大

            JDK1.8 -Xmx20m -Xms20m 这里将导致 OOM, 报错 GC overhead limit exceeded
            JDK17 -Xmx20m -Xms20m java.sql.SQLException: Java heap space
             */
            ResultSet resultSet = statement.executeQuery(sql100000);
            buildData(resultSet);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void streamSelectMod() {

        /*
        开启 mysql 的流式查询有两种方式：
          1. statement.setFetchSize(Integer.MIN_VALUE);
             执行查询但不会立即获取所有结果
             逐行(其实并不是逐行)从服务器传输数据（真正的流式处理）
             保持数据库连接打开直到所有结果被读取完毕

          2. enableStreamingResults()
             启用结果集的流式传输模式
             使用服务器端游标（如果可用）
             按需获取数据行

        ** 这里需要特别注意的是：如果对于需要把数据全部拿出来再进行处理的场景，流式查询是没有任何用户得
        ** 所以流式查询一般要结合 ResultSet 对于数据进行一行一行处理时对于内存消耗是非常小的

        分批的从TCP通道中读取mysql服务返回的数据，每次读取的数据量并不是一行（通常是一个package大小），jdbc客户端在调用rs.next()方法时会根据需要从TCP流通道中读取部分数据。
        所以也并不能配置每次拿多少数据，也并不是一行一行获取数据！！
         */

        MyStopWatch myStopWatch = new MyStopWatch();

        try {
            myStopWatch.start();
            Statement statement = ConnectFactory.getStatement();

            // statement.setFetchSize(Integer.MIN_VALUE);

            // statement.setFetchSize(1);
            ((com.mysql.cj.jdbc.JdbcStatement) statement).enableStreamingResults();

            ResultSet resultSet = statement.executeQuery(sql100000);
            buildData(resultSet);
            myStopWatch.stop();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        System.out.println(myStopWatch.prettyPrint());
    }

    @SneakyThrows
    public static void cursorSelectMod() {
        MyStopWatch myStopWatch = new MyStopWatch();
        myStopWatch.start();
        try {
            Statement statement = ConnectFactory.getStatement();
            /*
            游标查询的开启条件:

            1. 设置 FetchSize 大小
            2. 连接串参数 useCursorFetch=true
             */
            statement.setFetchSize(1);
            ResultSet resultSet = statement.executeQuery(sql100000);
            buildData(resultSet);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        myStopWatch.stop();
        System.out.println(myStopWatch.prettyPrint());
    }

    @SneakyThrows
    public static void buildData(ResultSet resultSet) {
        int count = 0;
        while (resultSet.next()) {
            count++;
        }
        System.out.println("查询了" + count + "条数据");
    }

}

package org.hulei.jdk.jdbc;

import com.github.jsonzou.jmockdata.JMockData;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 可滚动结果集: 如果展示一张表或者查询结果的可视化展示,我们通常希望用户可以在结果集上前后移动,对于可滚动结果集而言,我们可以任意向前/向后移动,甚至可以跳到任意位置
 * 可更新结果集: 如果希望编辑结果集中的内容,在可更新结果集中就可以编辑这些内容以更新数据库
 *
 * @author hulei
 * @since 2024/9/29 10:44
 */

public class ScrollResultSetTest {

    @SneakyThrows
    public static void main(String[] args) {
        // 默认情况下,结果集是不支持滚动和更新的,要获取可滚动和可更新的结果集需要两个参数
        // resultSetType: TYPE_FORWARD_ONLY-结果集不能滚动(默认), TYPE_SCROLL_INSENSITIVE-结果集可以滚动,但对数据库变化不敏感, TYPE_SCROLL_SENSITIVE-对数据库变化敏感,查询结束后如果数据库数据发生变化,后续如果滚动回来可以感知到这个变化
        // resultSetConcurrency: CONCUR_READ_ONLY-结果集不能用于更新数据库(默认), CONCUR_UPDATABLE-结果集可以用于更新数据库
        Statement statement = ConnectFactory.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        // mysql8对TYPE_SCROLL_SENSITIVE这个参数不起作用
        System.out.println("TYPE_SCROLL_SENSITIVE: " + ConnectFactory.getConnection().getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
        ResultSet resultSet = statement.executeQuery("select * from biguser limit 0,10");

        // previous将移动结果集到上一行,如果已经位于第一行之前,方法返回false
        System.out.println(resultSet.previous());
        // relative直接向前/向后移动指定行数, 负数为向前移动
        System.out.println(resultSet.relative(2));
        System.out.println(resultSet.previous());
        System.out.println(resultSet.relative(-1));

        ConnectFactory.printResultSet(resultSet);
        // 转到指定行
        resultSet.absolute(0);
        ConnectFactory.printResultSet(resultSet);

        // 第一行之前
        resultSet.beforeFirst();

        // 修改第一条数据的车牌
        resultSet.next();
        resultSet.updateString("plate", JMockData.mock(String.class));
        resultSet.updateRow();
        ConnectFactory.getConnection().commit();

        // 新增一条数据可以使用这种方式进行新增
        // resultSet.moveToInsertRow();
        // resultSet.insertRow();
        // resultSet.moveToCurrentRow();

        // 删除数据,同时删除结果集和数据库的数据
        // resultSet.deleteRow();
    }
}

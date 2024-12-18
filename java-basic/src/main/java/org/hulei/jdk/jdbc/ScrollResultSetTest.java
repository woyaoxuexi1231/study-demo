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

        Statement statement = ConnectFactory.getConnection()
                .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        // mysql8 对 TYPE_SCROLL_SENSITIVE 这个参数不起作用, 也就是说即使设置了这个参数，数据库变化的时候结果集也不会发生变化
        System.out.println("TYPE_SCROLL_SENSITIVE: " + ConnectFactory.getConnection().getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));

        ResultSet resultSet = statement.executeQuery("select * from big_user limit 0,10");
        // previous将移动结果集到上一行,如果已经位于第一行之前,方法返回false
        System.out.println(resultSet.previous());
        // relative直接向前/向后移动指定行数, 负数为向前移动
        System.out.println(resultSet.relative(2));
        System.out.println(resultSet.previous());
        System.out.println(resultSet.relative(-1));

        CommonUtil.prettyPrintResultSet(resultSet);
        // 转到指定行
        resultSet.absolute(0);
        CommonUtil.prettyPrintResultSet(resultSet);

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

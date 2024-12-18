package org.hulei.jdk.jdbc;

import cn.hutool.core.date.StopWatch;
import lombok.SneakyThrows;
import org.hulei.entity.jpa.pojo.BigUser;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql分三种查询形式
 * 1. 普通查询
 * 2. 流式查询
 * 3. 游标查询
 * 流式查询和游标查询都是为了解决大数据量而内存不够出现的, 流式查询没有明确指定每次从服务器获取数据的数量, 游标查询可以设置
 * 在内存充足的情况下,使用普通查询是最快的
 * 只有在一条一条处理数据时这种内存上的差异才会体现出来
 *
 * @author hulei
 * @since 2024/10/8 15:32
 */

public class SelectModTest {

    public static final String sql100 = "select id, user_name, ssn, name, phone_number, plate, address, building_number, country, birth, company, job, card_number, city, week, email, title, paragraphs, create_time, update_time from big_user limit 0,100";
    public static final String sql100000 = "select id, user_name, ssn, name, phone_number, plate, address, building_number, country, birth, company, job, card_number, city, week, email, title, paragraphs, create_time, update_time from big_user limit 0,100000";


    public static void main(String[] args) {
        // normalSelectMod();
        // streamSelectMod();
        // cursorSelectMod();
        // normalSelect();
        streamSelect();
    }

    /**
     * 普通查询
     * 应用代码简单,数据量小时操作速度快
     * 数据量大时容易导致OOM
     */
    @SneakyThrows
    public static void normalSelectMod() {
        Statement statement = ConnectFactory.getStatement();

        StopWatch stopWatch = new StopWatch();
        // 简单的查询100条数据,这是非常快的
        stopWatch.start("查询100条数据");
        ResultSet resultSet = statement.executeQuery(sql100);
        buildBigUsers(resultSet);
        stopWatch.stop();

        // 查询十万条数据,就显得有点力不从心了, 1.查询时间长 2.消耗内存大
        // -Xmx10m -Xms10m 这里将导致OOM, 报错 GC overhead limit exceeded
        stopWatch.start("查询100000条数据");
        ResultSet resultSet2 = statement.executeQuery(sql100000);
        buildBigUsers(resultSet2);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    @SneakyThrows
    public static void streamSelectMod() {
        Statement statement = ConnectFactory.getConnection().createStatement();
        // 对于 fetchSize 这个参数来说,jdbc本身想让实现者实现设置拉取数量的这种能力,但是mysql驱动并不支持
        // 开启mysql的流式查询需要:
        // 1. statement.setFetchSize(Integer.MIN_VALUE);
        // 2. ((com.mysql.jdbc.Statement)stat).enableStreamingResults();
        // 开启后会一行一行的从服务端获取数据,通信开销是比较大的,但是内存问题解决了,即使我们这里设置很小的内存,也不会出现OOM
        statement.setFetchSize(Integer.MIN_VALUE);

        StopWatch stopWatch = new StopWatch();
        // 简单的查询100条数据,这是非常快的
        stopWatch.start("查询100条数据");
        ResultSet resultSet = statement.executeQuery(sql100);
        buildBigUsers(resultSet);
        stopWatch.stop();

        // 查询十万条数据,就显得有点力不从心了, 1.查询时间长 2.消耗内存大
        // -Xmx10m -Xms10m 这里将导致OOM, 报错 GC overhead limit exceeded
        stopWatch.start("查询100000条数据");
        ResultSet resultSet2 = statement.executeQuery(sql100000);
        buildBigUsers(resultSet2);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    @SneakyThrows
    public static void cursorSelectMod() {
        Statement statement = ConnectFactory.getConnection().createStatement();
        // 游标查询的开启条件:
        // 1. 设置FetchSize大小
        // 2. 连接串参数 useCursorFetch=true
        statement.setFetchSize(20000);

        StopWatch stopWatch = new StopWatch();
        // 简单的查询100条数据,这是非常快的
        stopWatch.start("查询100条数据");
        ResultSet resultSet = statement.executeQuery(sql100);
        // ConnectFactory.justPrintResultSet(resultSet);
        buildBigUsers(resultSet);
        stopWatch.stop();

        // 查询十万条数据,就显得有点力不从心了, 1.查询时间长 2.消耗内存大
        // -Xmx10m -Xms10m 这里将导致OOM, 报错 GC overhead limit exceeded
        stopWatch.start("查询100000条数据");
        ResultSet resultSet2 = statement.executeQuery(sql100000);
        // ConnectFactory.justPrintResultSet(resultSet2);
        buildBigUsers(resultSet2);
        stopWatch.stop();


        System.out.println(stopWatch.prettyPrint());
    }

    @SneakyThrows
    public static void buildBigUsers(ResultSet resultSet) {
        List<BigUser> bigUsers = new ArrayList<>();
        while (resultSet.next()) {
            BigUser bigUser = new BigUser();
            bigUser.setId(resultSet.getLong("id"));
            bigUser.setUserName(resultSet.getString("user_name"));
            bigUser.setSsn(resultSet.getString("ssn"));
            bigUser.setName(resultSet.getString("name"));
            bigUser.setPhoneNumber(resultSet.getString("phone_number"));
            bigUser.setPlate(resultSet.getString("plate"));
            bigUser.setAddress(resultSet.getString("address"));
            bigUser.setBuildingNumber(resultSet.getString("building_number"));
            bigUser.setCountry(resultSet.getString("country"));
            bigUser.setBirth(resultSet.getString("birth"));
            bigUser.setCompany(resultSet.getString("company"));
            bigUser.setJob(resultSet.getString("job"));
            bigUser.setCardNumber(resultSet.getString("card_number"));
            bigUser.setCity(resultSet.getString("city"));
            bigUser.setWeek(resultSet.getString("week"));
            bigUser.setEmail(resultSet.getString("email"));
            bigUser.setTitle(resultSet.getString("title"));
            bigUser.setParagraphs(resultSet.getString("paragraphs"));
            bigUser.setParagraphs(resultSet.getString("paragraphs"));
            bigUser.setCreateTime(resultSet.getTimestamp(("create_time")).toLocalDateTime());
            bigUser.setUpdateTime(resultSet.getTimestamp(("update_time")).toLocalDateTime());
            bigUsers.add(bigUser);
        }
        System.out.println("查询了" + bigUsers.size() + "条数据");
    }

    @SneakyThrows
    public static void normalSelect() {
        Statement statement = ConnectFactory.getStatement();
        int pageNum = 0, pageSize = 50000;
        while (true) {
            int count = 0;
            ResultSet resultSet = statement.executeQuery("select * from big_user limit " + pageNum * pageSize + ","+ pageSize);
            while (resultSet.next()) {
                count++;
            }
            pageNum++;
            if (count == 0) {
                break;
            }
        }

    }

    @SneakyThrows
    public static void streamSelect() {
        Statement statement = ConnectFactory.getStatement();
        statement.setFetchSize(Integer.MIN_VALUE);

        ResultSet resultSet = statement.executeQuery("select * from big_user");
        while (resultSet.next()) {

        }
    }


}

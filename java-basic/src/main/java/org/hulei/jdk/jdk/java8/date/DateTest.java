package org.hulei.jdk.jdk.java8.date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hulei
 * @since 2024/9/29 14:59
 */

public class DateTest {

    public static void main(String[] args) {
        // date();
        // calendar();
        // javatime();
        dateFormat();
    }

    /**
     * Date 类表示时间点以自 1970 年 1 月 1 日 00:00:00 GMT 以来的毫秒数（称为 “epoch”）的方式存储。
     * Date 类不是线程安全的，多个线程同时访问同一个 Date 对象可能会出现问题。
     */
    @Deprecated
    public static void date() {


        Date date = new Date();
        Date dateFromMillis = new Date(1639526400000L);

        System.out.println(date); // Sun Sep 29 15:14:31 CST 2024
        System.out.println(dateFromMillis); // Wed Dec 15 08:00:00 CST 2021

        // 获取时间
        System.out.println(date.getTime());

        // Date类大量的方法都被弃用了
        System.out.println(date.getYear() + 1900); // 年份表示从1900年开始,需要额外处理,增加了复杂性和错误的可能
        System.out.println(date.getMonth() + 1); // 月份从0开始,而不是1
        System.out.println(date.getDate());
        date.setTime(System.currentTimeMillis()); // 设置新的时间点,并不是线程安全的,可能导致问题
    }

    /**
     * Calendar相比于Date类,提供了更多的api,但并非线程安全,多线程操作依旧会存在问题
     */
    public static void calendar() {
        // 创建实例
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());

        // 设置日期和时间
        calendar.set(2024, Calendar.SEPTEMBER, 28, 14, 30, 0);
        System.out.println(calendar.getTime());

        // 获取日期和时间
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);  // 注意：月份从0开始，即0代表1月
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        System.out.println("当前年份: " + year);
        System.out.println("当前月份 (0-11): " + month);
        System.out.println("当前日期: " + day);
        System.out.println("当前小时: " + hour);
        System.out.println("当前分钟: " + minute);
        System.out.println("当前秒: " + second);

        // 日期和时间的直接操作,加减法
        System.out.println("当前时间: " + calendar.getTime());
        // 加5天
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        System.out.println("加5天后: " + calendar.getTime());
        // 减2个月
        calendar.add(Calendar.MONTH, -2);
        System.out.println("减2个月后: " + calendar.getTime());
        // 加3年
        calendar.add(Calendar.YEAR, 3);
        System.out.println("加3年后: " + calendar.getTime());

        // 时间字段常量的定义
        System.out.println("Year: " + calendar.get(Calendar.YEAR));
        System.out.println("Month: " + calendar.get(Calendar.MONTH)); // 0-based
        System.out.println("Day of Month: " + calendar.get(Calendar.DAY_OF_MONTH) + 1);
        System.out.println("Day of Week: " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("Day of Year: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("Week of Year: " + calendar.get(Calendar.WEEK_OF_YEAR));
        System.out.println("AM/PM: " + calendar.get(Calendar.AM_PM));
    }

    /**
     * 地球自转一天24小时, 一共是86400秒
     * Java的 Date 和TimeApi 需要遵循
     * 1. 每天86400秒
     * 2. 每天正午与官方时间精准匹配
     * 3. 在其他时间点上,以精准定义的方式与官方时间接近匹配
     */
    public static void javatime() {
        // Instant表示时间线上的某一个点
        Instant instant = Instant.now();
        System.out.println(instant);
        Instant instantEnd = Instant.now();
        // Duration用于计算两个时刻之间的时间量
        Duration between = Duration.between(instant, instantEnd);
        System.out.println(between);


        // 本地日期,这是一个不包含任何时区信息的日期
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        LocalDate customLocalDate = LocalDate.of(1998, 12, 31);
        System.out.println(customLocalDate);
        Period period = Period.between(customLocalDate, localDate);
        System.out.println(period);
        System.out.println(localDate.plus(Period.ofDays(1)));
        System.out.println(localDate.getDayOfWeek());

        // 日期调整器,类似的像每个月的第一个星期二
        LocalDate localDate1 = LocalDate.of(2024, 10, 1);
        System.out.println(localDate1.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)));

        // 本地时间,和本地日期类似,不包含时区信息
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);

        // 时区时间
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2024, 9, 30, 15, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTime.toLocalDate());
        System.out.println(zonedDateTime.toLocalTime());
        System.out.println(zonedDateTime.toLocalDateTime());
    }

    public static void dateFormat() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter); // 2024-09-29 16:53:01
        System.out.println(formattedDate);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.systemDefault());
        System.out.println(zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)); // 2024-09-29T16:53:01.825+08:00[Asia/Shanghai]
        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(zonedDateTime)); // 2024年9月29日 星期日 下午04时53分01秒 CST
        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).format(zonedDateTime)); // 2024年9月29日 下午04时53分37秒

    }
}

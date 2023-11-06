package com.hundsun.demo.springboot.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils
 * @className: DateUtil
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 16:05
 */

public class DateUtil {

    private static Integer firstDayOfWeek(Integer business) {
        Calendar instance = Calendar.getInstance();
        // 设置时区
        instance.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 根据交易日设置时间
        instance.set(business / 10000, getMonth(((business % 10000) / 100)), business % 100);
        // 设置周一为一周的第一天
        // instance.setFirstDayOfWeek(Calendar.MONDAY);
        // 获取交易日是一周的第几天

        boolean isFirstSunday = (instance.getFirstDayOfWeek() == Calendar.SUNDAY);

        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);

        if (isFirstSunday) {
            dayOfWeek = dayOfWeek - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
        }

        instance.add(Calendar.DATE, -dayOfWeek + 1);
        int startYear = instance.get(Calendar.YEAR);
        int startMonth = instance.get(Calendar.MONTH) + 1;
        int startDate = instance.get(Calendar.DATE);

        System.out.println("时间: " + instance.getTime());
        // System.out.println("一周第一天: " + instance.getFirstDayOfWeek());

        return startYear * 10000 + startMonth * 100 + startDate;

        // int weekOfYear = instance.get(Calendar.WEEK_OF_YEAR);
        // // 小于当前交易日的数据, 查 dayOfWeek 条出来
        // LambdaQueryWrapper<TradeDayDO> wrapper = new LambdaQueryWrapper<>();
        // wrapper.lt(TradeDayDO::getTradeDate, business).orderByDesc(TradeDayDO::getTradeDate);
        // PageHelper.startPage(1, dayOfWeek);
        // List<TradeDayDO> tradeDayDOS = tradeDayMapper.selectList(wrapper);
    }

    private static int getMonth(Integer month) {
        int[] calenderMonths = new int[]{
                Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH,
                Calendar.APRIL, Calendar.MAY, Calendar.JUNE,
                Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
                Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER
        };
        for (int calenderMonth : calenderMonths) {
            if (calenderMonth == month - 1) {
                return calenderMonth;
            }
        }
        return 12;
    }
}

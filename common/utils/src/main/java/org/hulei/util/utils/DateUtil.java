package org.hulei.util.utils;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author woaixuexi
 * @since 2024/4/9 23:54
 */

public class DateUtil {
    public static Date getDate() {
        // 设置时区为东八区（北京时间）
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        // 获取当前时间
        Date now = new Date();
        // 根据指定时区获取当前时间
        return new Date(now.getTime() + timeZone.getRawOffset());
    }
}

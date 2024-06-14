package com.hundsun.demo.spring.util;

import com.google.common.base.CaseFormat;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.util
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-10-20 14:38
 */

public class StringCaseUtil {

    /**
     * 小驼峰转下划线
     *
     * @param lowerCamel lowerCamel
     * @return Underscore
     */
    public static String lowerCamelToUnderscore(String lowerCamel) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, lowerCamel);
    }

    /**
     * 下划线转小驼峰
     *
     * @param underscore underscore
     * @return lowerCamel
     */
    public static String underscoreToLowerCamel(String underscore) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscore);
    }
}

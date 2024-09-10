package org.hulei.keeping.server.db.dynamicdb.core;

import lombok.extern.slf4j.Slf4j;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: DataSourceTypeManager
 * @description: 定义一个 ThreadLocal 封装类
 * @author: h1123
 * @createDate: 2023/2/18 16:07
 */
@Slf4j
public class DataSourceToggleUtil {

    private static final ThreadLocal<String> DATA_SOURCE_TYPE_THREAD_LOCAL = new ThreadLocal<String>() {

        /**
         * 初始化默认为 MASTER 标志的数据库
         * @return MASTER
         */
        protected String initialValue() {
            return "first";
        }
    };

    public static String get() {
        return DATA_SOURCE_TYPE_THREAD_LOCAL.get();
    }

    public static void set(String dataSourceTag) {
        // log.debug("切换到数据源为 {}", dataSourceTag);
        DATA_SOURCE_TYPE_THREAD_LOCAL.set(dataSourceTag);
    }

    /**
     * 清楚标志, 默认绑定 MASTER
     */
    public static void reSet() {
        DATA_SOURCE_TYPE_THREAD_LOCAL.set("first");
    }

}

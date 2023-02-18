package com.hundsun.demo.spring.jdbc;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: DataSourceTypeManager
 * @description: 定义一个 ThreadLocal 封装类
 * @author: h1123
 * @createDate: 2023/2/18 16:07
 */

public class DataSourceTypeManager {

    private static final ThreadLocal<DataSourceType> DATA_SOURCE_TYPE_THREAD_LOCAL = new ThreadLocal<DataSourceType>() {

        /**
         * 初始化默认为 MASTER 标志的数据库
         * @return MASTER
         */
        protected DataSourceType initialValue() {
            return DataSourceType.MASTER;
        }
    };

    public static DataSourceType get() {
        return DATA_SOURCE_TYPE_THREAD_LOCAL.get();
    }

    public static void set(DataSourceType dataSourceType) {
        DATA_SOURCE_TYPE_THREAD_LOCAL.set(dataSourceType);
    }

    /**
     * 清楚标志, 默认绑定 MASTER
     */
    public static void reSet() {
        DATA_SOURCE_TYPE_THREAD_LOCAL.set(DataSourceType.MASTER);
    }

}

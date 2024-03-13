package com.hundsun.demo.spring.db.jdbc;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: DataSourceTypeManager
 * @description: 定义一个 ThreadLocal 封装类
 * @author: h1123
 * @createDate: 2023/2/18 16:07
 */

public class DynamicDataSourceTypeManager {

    private static final ThreadLocal<DynamicDataSourceType> DATA_SOURCE_TYPE_THREAD_LOCAL = new ThreadLocal<DynamicDataSourceType>() {

        /**
         * 初始化默认为 MASTER 标志的数据库
         * @return MASTER
         */
        protected DynamicDataSourceType initialValue() {
            return DynamicDataSourceType.MASTER;
        }
    };

    public static DynamicDataSourceType get() {
        return DATA_SOURCE_TYPE_THREAD_LOCAL.get();
    }

    public static void set(DynamicDataSourceType dynamicDataSourceType) {
        DATA_SOURCE_TYPE_THREAD_LOCAL.set(dynamicDataSourceType);
    }

    /**
     * 清楚标志, 默认绑定 MASTER
     */
    public static void reSet() {
        DATA_SOURCE_TYPE_THREAD_LOCAL.set(DynamicDataSourceType.MASTER);
    }

}

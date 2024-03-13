package com.hundsun.demo.spring.db.dynamicdb;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: DataSourceTypeManager
 * @description: 定义一个 ThreadLocal 封装类
 * @author: h1123
 * @createDate: 2023/2/18 16:07
 */

public class DynamicDataSourceTypeManager {

    /**
     * 每个线程下都有一个 DynamicDataSourceType 的变量来指示当前线程对应的数据源是什么
     * 这个 ThreadLocal 并不会在线程被创建好立马被创建, 会一直持续到调用 ThreadLocal.get() 方法时才被初始化
     * initialValue()方法是在get()的最后用来获取初始值的(如果有子类实现的话,ThreadLocal本身返回的是null)
     */
    private static final ThreadLocal<DynamicDataSourceType> DATA_SOURCE_TYPE_THREAD_LOCAL = new ThreadLocal<DynamicDataSourceType>() {
        /**
         * 初始化默认为 MASTER 标志的数据库
         * @return MASTER
         */
        protected DynamicDataSourceType initialValue() {
            return DynamicDataSourceType.MASTER;
        }
    };

    /**
     * 获取当前线程的数据源标识
     *
     * @return key
     */
    public static DynamicDataSourceType get() {
        return DATA_SOURCE_TYPE_THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程的数据源标识
     *
     * @param dynamicDataSourceType key
     */
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

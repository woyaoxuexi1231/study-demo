package com.hundsun.demo.spring.db.dynamicdb;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: ThreadLocalVariableRountingDataSource
 * @description:
 * @author: h1123
 * @createDate: 2023/2/18 16:20
 */

public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 在单数据源情况下, 我们注入的数据源 bean 是一个单纯的 DriverManagerDataSource
     * 而在多数据源的情形下, 我们注入的继承 AbstractRoutingDataSource 的一个实现类, 并且需要实现 determineCurrentLookupKey 这个方法以实现动态数据源的切换
     * 在 AbstractRoutingDataSource#determineTargetDataSource方法内我们可以看到具体选择数据源的逻辑
     * <p>
     * 这里使用自定义的枚举类 DynamicDataSourceType 作为 key-datasource 的 key
     * DynamicDataSourceTypeManager 作为每个线程切换 key 的一个管理类
     * <p>
     * 在applicationContext.xml配置文件内,分别定义了三个数据源,且三个数据源分别与 DynamicDataSourceType的三个枚举值对应
     * <p>
     * 测试用例我们使用 MultipleDataSourceTestEvent+MultipleDataSourceTestListener =>(调用) DynamicTestService
     *
     * @return key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 这里可以从某个地方（比如ThreadLocal）获取当前的数据源标识
        // 这个标识可以在你的应用中根据业务需要设置
        return DynamicDataSourceTypeManager.get();
    }
}

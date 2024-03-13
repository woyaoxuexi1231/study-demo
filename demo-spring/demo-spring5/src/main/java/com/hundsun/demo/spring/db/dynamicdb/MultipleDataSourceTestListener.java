package com.hundsun.demo.spring.db.dynamicdb;

import lombok.Data;
import org.springframework.context.ApplicationListener;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.listener
 * @className: MultipleDataSourceTestListener
 * @description:
 * @author: h1123
 * @createDate: 2023/2/18 16:41
 */

@Data
public class MultipleDataSourceTestListener implements ApplicationListener<MultipleDataSourceTestEvent> {

    private DynamicTestService dynamicTestService;

    @Override
    public void onApplicationEvent(MultipleDataSourceTestEvent multipleDataSourceTestEvent) {
        // yiibaidbService.multipleDataSource(multipleDataSourceTestEvent.getDataSourceType());
        // 使用指定数据源更新数据
        DynamicDataSourceTypeManager.set(multipleDataSourceTestEvent.getDataSourceType());
        dynamicTestService.multipleDataSourceTransaction(multipleDataSourceTestEvent.getDataSourceType());
    }
}

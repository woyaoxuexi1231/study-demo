package com.hundsun.demo.spring.mybatis;

import com.hundsun.demo.spring.db.dynamicdb.DynamicDataSourceTypeManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.mybatisplugins
 * @className: MyBatisTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 21:59
 */

@Data
@Slf4j
public class MyBatisDynamicdbTestListener implements ApplicationListener<MybatisEvent> {

    private MyBatisDynamicdbService myBatisDynamicdbService;

    @Override
    public void onApplicationEvent(MybatisEvent event) {
        // 切换数据源
        DynamicDataSourceTypeManager.set(event.getDataSourceType());
        this.myBatisDynamicdbService.mybatisSpringTransaction(event.getMyBatisOperationType(), event.getDataSourceType());
    }
}

package com.hundsun.demo.spring.mybatis;

import com.hundsun.demo.spring.init.listener.MybatisEvent;
import com.hundsun.demo.spring.jdbc.DynamicDataSourceTypeManager;
import com.hundsun.demo.spring.service.YiibaidbService;
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
public class MyBatisTest implements ApplicationListener<MybatisEvent> {

    private YiibaidbService yiibaidbService;

    @Override
    public void onApplicationEvent(MybatisEvent event) {
        // 切换数据源
        DynamicDataSourceTypeManager.set(event.getDataSourceType());
        this.yiibaidbService.mybatisSpringTransaction(event.getMyBatisOperationType(), event.getDataSourceType());
    }
}

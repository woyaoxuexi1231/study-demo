package com.hundsun.demo.dubbo.provider.service.impl;

import com.hundsun.demo.dubbo.provider.api.service.ExportDemoService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author hulei
 * @since 2025/7/11 22:11
 */

@DubboService
public class ExportDemoServiceImpl implements ExportDemoService {

    @Override
    public void sayHello(String name) {

    }
}

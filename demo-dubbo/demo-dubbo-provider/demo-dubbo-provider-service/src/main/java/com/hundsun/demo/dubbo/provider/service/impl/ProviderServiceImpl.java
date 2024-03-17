package com.hundsun.demo.dubbo.provider.service.impl;

import com.hundsun.demo.commom.core.annotation.DoneTime;
import com.hundsun.demo.dubbo.provider.api.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:15
 */

@DubboService(weight = 70)
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    @Value(value = "${server.port}")
    String port;

    @Value(value = "${dubbo.provider.group:null}")
    String group;

    @Value(value = "${dubbo.provider.version:null}")
    String version;

    @DoneTime
    @Override
    public String RpcInvoke() {
        // throw new RuntimeException("error");
        return String.format("hello rpc! port: %s, group: %s, version: %s",
                port,
                StringUtils.isEmpty(ProviderServiceImpl.class.getAnnotation(DubboService.class).group()) ? group : ProviderServiceImpl.class.getAnnotation(DubboService.class).group(),
                StringUtils.isEmpty(ProviderServiceImpl.class.getAnnotation(DubboService.class).version()) ? version : ProviderServiceImpl.class.getAnnotation(DubboService.class).version()
        );
    }

}

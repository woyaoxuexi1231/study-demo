package com.hundsun.demo.dubbo.provider.service.impl;

import org.apache.dubbo.rpc.RpcException;
import org.hulei.common.autoconfigure.annotation.DoneTime;
import com.hundsun.demo.dubbo.provider.api.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei42031
 * @since 2022-05-21 15:15
 */

/*
在 Dubbo 中，Provider 端可以通过配置来约束或影响 Consumer 端的行为
这些约束都可以通过 DubboService 进行配置，这些配置会通过注册中心传递给消费者
 */
// @DubboService(weight = 70, group = "test", version = "1.0", timeout = 3000)
@DubboService(weight = 70, timeout = 3000)
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    @Value(value = "${server.port}")
    String port;

    @Value(value = "${dubbo.provider.group:default}")
    String group;

    @Value(value = "${dubbo.provider.version:0}")
    String version;

    @DoneTime
    @Override
    public String RpcInvoke() {

        /*
        这里设置了一个四秒的停顿，而消费端使用默认的3秒超时时间时，会因为服务超时导致消费端进行重试(默认重试2次)
        所以这里会看到一个现象是服务提供端显示服务被调用了3次，而消费端只能触发provider提供的服务降级ProviderServiceMock的结果
         */
        // LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(4));

        try {
            return String.format("hello rpc! here address is : %s, port: %s, group: %s, version: %s",
                    InetAddress.getLocalHost().getHostAddress(),
                    port,
                    StringUtils.isEmpty(ProviderServiceImpl.class.getAnnotation(DubboService.class).group()) ? group : ProviderServiceImpl.class.getAnnotation(DubboService.class).group(),
                    StringUtils.isEmpty(ProviderServiceImpl.class.getAnnotation(DubboService.class).version()) ? version : ProviderServiceImpl.class.getAnnotation(DubboService.class).version()
            );
        } catch (UnknownHostException e) {
            // 只有当抛出Rpc异常或其他dubbo的业务异常时，在服务消费端才会进行服务降级，其他异常不会服务降级
            throw new RpcException(e);
        }
    }

}

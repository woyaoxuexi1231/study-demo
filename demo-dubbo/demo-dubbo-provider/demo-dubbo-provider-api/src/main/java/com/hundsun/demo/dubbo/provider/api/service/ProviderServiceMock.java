package com.hundsun.demo.dubbo.provider.api.service;

import com.hundsun.demo.dubbo.provider.api.service.ProviderService;

/**
 * 这个是mock类,用于服务降级.
 * 1. 服务端如果配置 mock=true,那么这个类必须方法在与 ProviderService 同一个包下面
 * 2. 服务端 mock 如果配置全限定类名,那么无所为
 * 3. mock="[fail|force]return|throw xxx"
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.service.impl
 * @className: MockService
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/16 13:24
 */

public class ProviderServiceMock implements ProviderService {

    @Override
    public String RpcInvoke() {
        return "Sorry, the service is currently unavailable.";
    }
}

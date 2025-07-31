package com.hundsun.demo.dubbo.provider.api.service;

/**
 * 这个是mock类,用于服务降级.
 * 1. 服务端如果配置 mock=true，那么这个类必须方法在与 ProviderService 同一个包下面
 * 2. 服务端 mock 如果配置全限定类名，那么无所谓
 * 3. mock="[fail|force]return|throw xxx"
 *
 * @author woaixuexi
 * @since 2024/3/16 13:24
 */
public class ProviderServiceMock implements ProviderService {

    @Override
    public String RpcInvoke() {
        /*
        Dubbo 的 mock 是一种本地伪装机制，当 Provider 出现不可用时，Consumer 端可以通过本地模拟的 Mock 类返回降级结果，避免调用失败直接影响用户体验。

        以下场景会触发 mock
        - provider 服务未注册，在注册中心找不到 provider 的服务
        - provider 抛出异常，	如超时（TimeoutException）、业务异常（RpcException）。如果抛出其他异常不一定会触发 mock，而是报错会到消费端
        - 网络不通，Consumer 无法连接到 Provider 的 Dubbo 端口
        - Provider 主动熔断，通过 Sentinel/Dubbo 熔断规则主动拒绝请求
         */
        return "抱歉！服务器繁忙！请稍后再试！";
    }
}

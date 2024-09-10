package org.hulei.keeping.server.dubbo.provider;

import org.hulei.keeping.server.dubbo.api.GreetingsService;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.dubbo.provider
 * @className: Application
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/16 16:14
 */

public class Application {

    public static void main(String[] args) {
        // 定义具体的服务
        ServiceConfig<GreetingsService> service = new ServiceConfig<>();
        service.setInterface(GreetingsService.class);
        service.setRef(new GreetingsServiceImpl());
        // 启动 Dubbo
        DubboBootstrap.getInstance()
                .application("first-dubbo-provider")
                .registry(new RegistryConfig("zookeeper://192.168.80.128:2181"))
                .protocol(new ProtocolConfig("dubbo", 8299))
                .service(service)
                .start()
                .await();
    }
}
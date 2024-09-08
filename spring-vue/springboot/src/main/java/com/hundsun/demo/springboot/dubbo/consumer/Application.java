package com.hundsun.demo.springboot.dubbo.consumer;

import com.hundsun.demo.springboot.dubbo.api.GreetingsService;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

import java.io.IOException;
import java.util.HashMap;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.dubbo.consumer
 * @className: Application
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/16 16:15
 */

public class Application {
    public static void main(String[] args) throws IOException {
        ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
        reference.setInterface(GreetingsService.class);

        HashMap<String, String> configMap = new HashMap<>();
        // configMap.put(QOS_PORT,"22223");
        ProtocolConfig dubboConfig = new ProtocolConfig("dubbo", 8201);
        // dubboConfig.setParameters(configMap);

        DubboBootstrap.getInstance()
                .application("first-dubbo-consumer")
                .registry(new RegistryConfig("zookeeper://192.168.80.128:2181"))
                .protocol(dubboConfig)
                .reference(reference);

        GreetingsService service = reference.get();
        String message = service.sayHi("dubbo");
        System.out.println("Receive result ======> " + message);
        System.in.read();
    }
}

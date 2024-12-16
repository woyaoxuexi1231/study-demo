package org.hulei.springboot.spring.actuate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SpringBoot自带监控功能Actuator，可以帮助实现对程序内部运行情况监控，比如监控状况、Bean加载情况、环境变量、日志信息、线程信息等
 * 默认带有以下接口
 * <p>
 * /actuator                                可以查看所有支持的api<br>
 * /actuator/health                         可以查看当前应用程序的健康指标<br>
 * /actuator/info                           可以获取应用程序的定制信息<br>
 * /actuator/beans                          可以查看应用程序上下文里全部的bean以及他们的关系<br>
 * <p>
 * 当前也可以像这样自定义一个端点  /actuator/myCustomEndpoint
 * <p>
 * management.endpoint.shutdown.enabled=true 可以通过 /actuator/shutdown 直接关闭程序
 *
 * @author hulei
 * @since 2024/9/3 21:22
 */

@Component
@Endpoint(id = "myCustomEndpoint")
public class MyCustomEndpoint {

    @ReadOperation
    public String myCustomEndpoint() {
        return "Hello from my custom endpoint!";
    }
}

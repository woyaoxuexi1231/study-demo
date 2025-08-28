package org.hulei.springcloud.client.api;

import org.hulei.util.dto.SimpleReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author hulei
 * @since 2025/7/29 16:15
 */

/*
1. 启动阶段（在容器刷新前）通过 FeignClientsRegistrar(实现ImportBeanDefinitionRegistrar) 的 registerBeanDefinitions 正式开始注册 feign client
2. 扫描 EnableFeignClients 指定jar包下所有使用 @FeignClient 修饰的类
3. 根据扫描到的结果构建 FeignClientFactoryBean，这个 bean 包含了当前 FeignClient所在的类，以及标注的一些配置信息
4. bean的刷新阶段会通过 FeignClientFactoryBean 创建对应的代理对象
5. 最终在 ReflectiveFeign 类中生成代理对象，代理对象内持有一个 ReflectiveFeign 对象
6. 调用时通过 ReflectiveFeign 对象内的 method 变量，每个方法会对应一个 SynchronousMethodHandler，最终通过 SynchronousMethodHandler 内部包装的使用 RequestTemplate 进行服务调用

注意：
  - @FeignClient 不能和 @RequestMapping 同时使用
  - FeignClient 无法应对方法有多个
 */
@FeignClient(name = "${spring.application.name}")
@Service
public interface HiControllerFeign {

    @GetMapping("/hi")
    public String hi();

    @GetMapping("/hi-request-param")
    public String hiWithRequestParam(@RequestParam(required = false, name = "param") String param);

    @GetMapping(path = {"/hi-path-param/{param}"}, name = "Restful 风格的 URL 请求")
    public String hiWithPathParam(@PathVariable(name = "param") String param);

    @GetMapping("/hi-databinding")
    public String hiDataBinding(@SpringQueryMap SimpleReqDTO reqDTO); // @SpringQueryMap 会将对象属性转换为 URL 查询参数，对于参数绑定来说非常有用

    @PostMapping("/hi-body")
    public String hiBody(@RequestBody SimpleReqDTO reqDTO);

    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map);
}

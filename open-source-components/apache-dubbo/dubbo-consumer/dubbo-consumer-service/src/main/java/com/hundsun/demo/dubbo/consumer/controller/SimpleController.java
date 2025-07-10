package com.hundsun.demo.dubbo.consumer.controller;

import com.hundsun.demo.dubbo.provider.api.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.hulei.util.dto.ResultDTO;
import org.hulei.util.utils.ResultDTOBuild;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.dubbodemo.consumer.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:21
 */

@Slf4j
@RestController
@RequestMapping("/consumer")
@Validated
public class SimpleController {

    /**
     * DubboReference 是 Apache Dubbo 框架中用于 引用远程 Dubbo 服务 的核心注解，类似于 Spring 的 @Autowired，但用于 跨 JVM 的 RPC 调用。
     * 它的主要作用是在 服务消费者（Consumer） 端注入 Dubbo 提供的远程服务接口，让开发者可以像调用本地方法一样调用远程服务。
     */
    @DubboReference(
            // 不检查提供者是否可用
            check = false
            /*
            负载均衡，dubbo 默认提供了多种负载均衡策略：
                1.随机负载均衡（RandomLoadBalance）：随机选择一个服务提供者来处理请求，每个服务提供者被选择的概率相等。[完全的随机,但是受权重影响]
                2.轮询负载均衡（RoundRobinLoadBalance）：按照固定顺序轮询选择服务提供者来处理请求，每个服务提供者均匀分配请求。[完全的公平,但是受权重影响]
                3.最小活跃数负载均衡（LeastActiveLoadBalance）：选择活跃数最小的服务提供者来处理请求，用于尽可能地避免某些服务提供者负载过高。[相同活跃数,受权重影响]
                4.一致性哈希算法(ConsistentHashLoadBalance )将服务消费者的请求根据请求的特征（通常是请求的某些属性，如请求的 IP 地址、服务接口等）映射到固定数量的虚拟节点上。
                5.(ShortestResponseLoadBalance)优先选择响应时间最短的服务提供者来处理新的请求。这种策略的目的是减少系统的平均响应时间，并提高服务的整体性能。[相同响应时间,受权重影响]
             */
            , loadbalance = RoundRobinLoadBalance.NAME
            // cluster = "forking", // 集群策略
            , mock = "true" // 实现服务降级,当服务不可用(服务可以但是不报错,这个mock配置是不会生效的)时,会自动进行本地服务降级
            // 分组和版本的*配置，会影响负载均衡，但是不清楚原因 TODO 2024年3月24日
            // , group = "*"
            // , version = "*"
            // merger = "true",
            // timeout = 30000 // 服务调用超时,0则不触发超时报错
    )
    ProviderService providerService;

    /**
     * 这是一个rpc调用,提供方返回一串字符串
     * <p>
     * Dubbo 中 zookeeper 做注册中心，如果注册中心集群都挂掉，发布者和订阅者之间还能通信么？
     * 1.新的服务提供者和消费者将无法进行注册和发现过程
     * 2.对于已经建立的服务提供者和消费者，Dubbo 客户端通常会缓存注册信息。只要服务提供者和消费者之间的网络是连通的，且没有重启过这些应用（重启会导致尝试重新从注册中心获取信息）
     * 3.对于已有的提供者服务宕机后,消费者会检测到这个掉线(与这个服务者断开连接了),那么会把这个服务提供者从自己的服务信息列表中移除他
     *
     * @return rsp
     */
    @GetMapping("/simpleRpcInvoke")
    public ResultDTO<?> simpleRpcInvoke() {
        return ResultDTOBuild.resultSuccessBuild(providerService.RpcInvoke());
    }

    @Operation(description = "测试 DubboReference 的负载均衡")
    @GetMapping("/testRandomInvoke")
    public ResultDTO<?> testRandomInvoke() {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String s = providerService.RpcInvoke();
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        log.info("==============================================================");
        map.forEach((k, v) -> log.info("{}: {} times", k, v));
        log.info("==============================================================");
        return ResultDTOBuild.resultSuccessBuild(map);
    }


    @GetMapping("/sticky")
    public void sticky() {
        /*
        ReferenceConfig 类用于配置 Dubbo 服务的相关信息，例如应用信息、注册中心信息、服务接口、负载均衡策略、集群容错策略等。通过配置 ReferenceConfig 对象，我们可以告诉 Dubbo 如何访问和调用远程的 Dubbo 服务。

        同时，ReferenceConfig 类还提供了 get() 方法，用于获取对应服务的代理对象。获取到代理对象后，我们就可以像调用本地对象一样来调用远程的 Dubbo 服务。

        以下是 ReferenceConfig 类的一些常用属性和方法：

        setApplication(ApplicationConfig application): 设置应用配置信息。
        setRegistry(RegistryConfig registry): 设置注册中心配置信息。
        setInterface(Class<?> interfaceClass): 设置服务接口。
        setUrl(String url): 设置直连 URL 地址。
        setLoadbalance(String loadbalance): 设置负载均衡策略。
        setCluster(String cluster): 设置集群容错策略。
        setVersion(String version): 设置服务版本。
        setGroup(String group): 设置服务分组。
        setTimeout(int timeout): 设置超时时间。
        setRetries(int retries): 设置重试次数。
        setSticky(boolean sticky): 设置粘滞连接策略。Dubbo 会在首次调用服务时选择一个服务提供者，并与其建立连接。之后，在同一个方法内的后续调用中，Dubbo 将会尽可能地继续使用与该服务提供者的连接，而不再重新选择其他服务提供者。可以减少连接建立的开销，提高连接的复用率，从而降低网络通信的延迟，提升系统的性能和稳定性。
        setProxy(String proxy): 设置代理类型（JDK 代理或者 Javassist 代理）
         */
        ReferenceConfig<ProviderService> reference = new ReferenceConfig<>();
        reference.setInterface(ProviderService.class);
        reference.setSticky(true);
        ProviderService service = reference.get();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            String s = service.RpcInvoke();
            if (map.containsKey(s)) {
                map.put(s, map.get(s) + 1);
            } else {
                map.put(s, 1);
            }
        }
        log.info("==============================================================");
        map.forEach((k, v) -> log.info("{}: {} times", k, v));
        log.info("==============================================================");
    }

}

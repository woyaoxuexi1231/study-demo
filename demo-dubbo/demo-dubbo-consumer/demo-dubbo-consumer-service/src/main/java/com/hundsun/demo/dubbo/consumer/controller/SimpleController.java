package com.hundsun.demo.dubbo.consumer.controller;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.provider.api.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.LeastActiveLoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.ShortestResponseLoadBalance;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 微服务
     * loadbalance:
     * -- 1.随机负载均衡（RandomLoadBalance）：随机选择一个服务提供者来处理请求，每个服务提供者被选择的概率相等。[完全的随机,但是受权重影响]
     * -- 2.轮询负载均衡（RoundRobinLoadBalance）：按照固定顺序轮询选择服务提供者来处理请求，每个服务提供者均匀分配请求。[完全的公平,但是受权重影响]
     * -- 3.最小活跃数负载均衡（LeastActiveLoadBalance）：选择活跃数最小的服务提供者来处理请求，用于尽可能地避免某些服务提供者负载过高。[相同活跃数,受权重影响]
     * -- 4.一致性哈希算法(ConsistentHashLoadBalance )将服务消费者的请求根据请求的特征（通常是请求的某些属性，如请求的 IP 地址、服务接口等）映射到固定数量的虚拟节点上。
     * -- 5.(ShortestResponseLoadBalance)优先选择响应时间最短的服务提供者来处理新的请求。这种策略的目的是减少系统的平均响应时间，并提高服务的整体性能。[相同响应时间,受权重影响]
     */
    @DubboReference(
            check = false, // 不检查提供者是否可用
            loadbalance = ConsistentHashLoadBalance.NAME, // 负载均衡策略
            // cluster = "forking", // 集群策略
            mock = "true", // 实现服务降级,当服务不可用(服务可以但是不报错,这个mock配置是不会生效的)时,会自动进行本地服务降级
            // group = "*", // *代表所有,但是匹配不了空串 dubbo 2.7.8 todo 为什么这个会影响负载均衡策略呢
            // version = "*", // *代表所有,但是匹配不了空串 dubbo 2.7.8 todo 为什么这个会影响负载均衡策略呢
            // merger = "true",
            timeout = 30000 // 服务调用超时,0则不触发超时报错
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
        int p1 = 0;
        int p2 = 0;
        for (int i = 0; i < 1000; i++) {
            String s = providerService.RpcInvoke();
            if (s.contains("10290")) {
                p1++;
            } else {
                p2++;
            }
        }
        log.info("p1:{}, p2:{}", p1, p2);
        return new ResultDTO<>();
    }


}

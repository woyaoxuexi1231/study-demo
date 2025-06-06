package com.hundsun.demo.springcloud.eureka.ribbon.client.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author hulei
 * @since 2023/5/6 22:22
 */

@Deprecated
@Configuration
public class BeanConfig {

    /**
     * restTemplate
     * <p>
     * 注解 @LoadBalanced 开启负载均衡
     * <p>
     * LoadBalancerClient 是一个负载均衡的客户端, RibbonLoadBalancerClient 是它的一个实现, 最终的负载均衡的请求由 RibbonLoadBalancerClient 来处理
     * <p>
     * LoadBalancerClient 具体交给了 ILoadBalancer 来处理, ILoadBalancer 通过配置 IRule, IPing 等, 向 EurekaClient 获取注册列表的信息
     * 默认每十秒向 EurekaClient 发一次 ping, 进而检查是否需要更新服务的注册信息, 最后, 在得到服务注册列表信息之后, ILoadBalancer 通过 IRule 的策略进行负载均衡
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        // 由于从从 Spring Cloud 2020.0 版本（即 Spring Cloud Ilford）开始，不再对 @LoadBalanced 对 Netflix Ribbon 提供支持。Netflix Ribbon 已经在 Spring Cloud 中被标记为过时（deprecated），并且在将来的版本中可能会被移除。
        // 所以这个服务已经不再能够提供调用支持了
        return new RestTemplate();
    }
}

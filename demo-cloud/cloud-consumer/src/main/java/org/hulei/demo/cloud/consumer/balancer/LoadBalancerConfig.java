package org.hulei.demo.cloud.consumer.balancer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author hulei
 * @since 2025/7/31 14:07
 */

@Configuration
public class LoadBalancerConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        /*
        通过 @LoadBalanced 注解启用负载均衡的 RestTemplate
        如果不开启 @LoadBalanced 注解，不会开启微服务的调用模式

        支持的负载均衡算法有：
            - RoundRobin	默认策略，轮询每个实例
            - Random	随机选择一个实例
            - WeightedResponseTime	基于实例的响应时间加权选择
            - 自定义策略	可以扩展接口实现你自己的策略

        此版本的 @LoadBalanced 会通过 LoadBalancerClient 寻找负载均衡策略，最终找到合适的服务

        开启 @LoadBalanced 之后，RestTemplate调用时创建的 ClientHttpRequest 会有所不同
        后续在真正调用的时候会根据服务名找到真正的ip信息来进行调用
         */
        // return new RestTemplateBuilder().rootUri("http://eureka-client/").build();
        return new RestTemplate();
    }
}

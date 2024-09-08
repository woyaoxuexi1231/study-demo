package com.hundsun.demo.springcloud.eureka.feign.client.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.feign.client.config
 * @className: FeignConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 13:18
 */

@Configuration
public class FeignConfig {

    @Bean
    public Retryer feignRetryer() {
        /*
        这行代码是在配置 Feign 客户端的重试策略。在 Feign 客户端中，你可以通过配置 Retryer 来指定在发生请求失败时的重试行为。
        具体来说，`Retryer.Default(100, SECONDS.toMillis(1), 5)` 表示创建了一个默认的 Retryer 实例，其中参数的含义如下：
        - 第一个参数 `100`：表示初始的重试间隔时间（单位为毫秒）。也就是说，当第一次请求失败后，会等待 100 毫秒后进行第一次重试。
        - 第二个参数 `SECONDS.toMillis(1)`：表示重试的最大间隔时间（单位为毫秒）。也就是说，重试间隔时间不会超过 1 秒。
        - 第三个参数 `5`：表示最大的重试次数。也就是说，如果请求失败，最多会进行 5 次重试。
        这样配置了 Retryer 后，当使用 Feign 客户端进行服务调用时，如果发生请求失败的情况，Feign 将会根据配置的重试策略进行重试，直到达到最大重试次数或者请求成功为止。
        需要注意的是，重试策略的配置可以根据实际需求进行调整，比如调整重试的间隔时间、最大重试次数等，以满足不同场景下的需求。
         */
        return new Retryer.Default(100, SECONDS.toMillis(1), 5);
    }
}

package com.hundsun.demo.common.autoconfigure.curator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.duubo.common.curator.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-27 13:39
 */

@Slf4j
@Configuration
@EnableConfigurationProperties({CuratorConfiguration.class})
// 2024年4月29日 IDEA 2022.1 这个版本识别不了这种配置方式,当我自动注入CuratorFramework的时候会提示我找不到对应的bean
//@ConditionalOnProperty(name = "curator.enable", havingValue = "true")
@Import(CuratorBeanConfiguration.class)
public class CuratorAutoConfiguration {
}

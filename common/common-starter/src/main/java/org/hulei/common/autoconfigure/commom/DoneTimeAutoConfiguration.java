package org.hulei.common.autoconfigure.commom;


import org.hulei.commom.core.aop.DoneTimeAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-23 17:41
 */

@ConditionalOnProperty(value = {"done.time.enable"}, havingValue = "true")
@Configuration
@Import({DoneTimeAspect.class})
public class DoneTimeAutoConfiguration {
}
